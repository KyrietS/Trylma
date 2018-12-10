package main;

import movement.BasicMovementStrategy;
import serverboard.ClassicBoardFactory;
import movement.GameMaster;
import player.Player;
import player.RealPlayer;
import shared.*;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

class Server
{
    private ServerSocket serverSocket;
    private List<Player> players;
    private GameMaster gameMaster;

    Server(int port) throws Exception
    {
        System.out.println("Uruchamianie serwera...");
        try
        {
            serverSocket = new ServerSocket(port);
        } catch (Exception e)
        {
            throw new Exception("Nie można uzyskać dostępu to portu " + port);
        }
    }

    /**
     * Utworzenie nowej planszy i przyłączenie odpowiedniej ilości graczy
     * @param playersNum liczba prawdziwych graczy biorących udział w meczu
     * @param botsNum liczba botów w meczu
     */
    void createMatch(int playersNum, int botsNum)
    {
        if( playersNum + botsNum < 1 || playersNum+botsNum > PlayerColor.values().length-1 )
            throw new RuntimeException( "Nieprawidłowe parametry dot. liczby graczy" );

        gameMaster = new GameMaster(new BasicMovementStrategy(), new ClassicBoardFactory());
        players = new ArrayList<>();

        // Utworzenie planszy dla 'playersNum' graczy
        System.out.println( "Genrowanie planszy dla " + ( playersNum + botsNum ) + " graczy" );
        PlayerColor[] colors = gameMaster.initializeBoard( playersNum );

        int playersConnected = 0;
        do
        {
            try
            {
                System.out.println( "Oczekiwanie na gracza " + colors[playersConnected].toString() );
                players.add( new RealPlayer( serverSocket.accept(), colors[ playersConnected ] ) );

                // przywitaj nowego gracza nadaniem kolorka
                PlayerColor color = colors[ playersConnected ];
                players.get( playersConnected ).sendCommand( "WELCOME " + color.toString() );
                playersConnected++;
            }
            catch( Exception e )
            {
                System.out.println( "Wystąpił błąd przy próbie połączenia z klientem" );
            }
        } while( playersConnected < playersNum );

        System.out.println( "Wczytano wszystkich " + playersConnected + " graczy" );

        startMatch( players );

        System.out.println("Mecz zakończony");
    }

    /**
     * Główny algorytm przeprowadzający rozgrywkę z wieloma graczami.
     * Dokładny opis działania, patrz: "Dokuemtacja drzewa decyzyjnego serwera"
     */
    private void startMatch( List<Player> players )
    {
        System.out.println("Inicjalizacja meczu");
        // miejsce, które zajmie następny wygrany gracz
        int place = 1;
        // Rozpoczęcie meczu i pierwsze wysyłanie planszy
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addCommand( "START" );
        commandBuilder.addCommand( "BOARD", gameMaster.getBoardAsString() );
        sendToAll( commandBuilder.getCommand() );

        // id gracza, którego aktualnie trwa tura
        int activePlayer = 0;

        // czy kolejną turę będzie wykonywał następny (true) czy obecny (false) gracz
        boolean nextPlayer = true;

        do
        {
            // Rozpoczęcie tury gracza
            players.get( activePlayer ).sendCommand( "YOU" );

            System.out.println( "Turę zaczyna gracz " + players.get( activePlayer ).getColor().toString() );

            // Zresetowanie warunków
            JumpStatusVerifyCondition jumpStatus = new JumpStatusVerifyCondition( 0 );
            PreviousPawnVerifyCondition previousPawn = new PreviousPawnVerifyCondition();
            AdditionalVerifyCondition[] conditions = { jumpStatus, previousPawn };

            do
            {

                System.out.print("Oczekiwanie na odpowiedź od gracza... ");
                // Wczytywanie odpowiedzi od klienta
                Response response = null;
                try
                {
                    String line = players.get( activePlayer ).readResponse();
                    System.out.println("odebrano: " + line);
                    Response[] responses = ResponseInterpreter.getResponses( line );
                    if( responses.length != 1 )
                    {
                        System.out.println( "Otrzymano nieprawidłowy komunikat od gracza id = " + activePlayer );
                        System.exit( -1 );
                    }
                    response = responses[ 0 ];
                }
                catch( Exception e )
                {
                    System.out.println( "Rozłączono gracza " + players.get(activePlayer).getColor().toString() );
                    System.out.println( e.getMessage() );
                    System.exit( -1 );
                }

                if( response.getCode().equals( "SKIP" ) )
                {
                    players.get( activePlayer ).sendCommand( "STOP" );
                    nextPlayer = true;
                }
                if( response.getCode().equals( "MOVE" ) )
                {
                    int x1 = response.getNumbers()[ 0 ];
                    int y1 = response.getNumbers()[ 1 ];
                    int x2 = response.getNumbers()[ 2 ];
                    int y2 = response.getNumbers()[ 3 ];
                    previousPawn.setCurrentXY( x1, y1 );

                    // zweryfikuj ruch
                    int result = gameMaster.verifyMove( x1, y1, x2, y2, conditions );

                    if( result == 0 ) // ruch niepoprawny
                    {
                        System.out.println( "Ruch niepoprawny" );
                        players.get( activePlayer ).sendCommand( "NOK" );
                        nextPlayer = false;
                    }
                    else // ruch poprawny
                    {
                        System.out.println("Ruch poprawny");
                        // aktualizacja conditions
                        jumpStatus.setStatus( result );
                        previousPawn.setPreviousXY( x2, y2 );

                        // wykonaj ruch na planszy
                        gameMaster.makeMove( x1, y1, x2, y2 );

                        // sprawdź czy gracz zwyciężył
                        boolean winner = gameMaster.isWinner( players.get( activePlayer ).getColor() );

                        if( winner ) // gracz wygrał
                        {
                            System.out.println( "WYGRANA" );
                            // wyślij komunikat o zwycięstwie
                            players.get( activePlayer ).sendCommand( "END " + place );

                            // oznacz gracza jako zwycięzcę
                            players.get( activePlayer ).setFinished( true );
                            place++;    // następny gracz zajmie kolejne miejsce

                            sendToAll( "BOARD " + gameMaster.getBoardAsString() );

                            nextPlayer = true;

                        }
                        else if( result == 1 ) // wykonany ruch nie był skokiem (przemieszczenie o 1 pole)
                        {
                            System.out.println("Wykonano krótki ruch. Koniec tury");
                            players.get( activePlayer ).sendCommand( "OK@STOP" );
                            sendToAll( "BOARD " + gameMaster.getBoardAsString() );

                            nextPlayer = true;
                        }
                        else // ruch był skokiem, gracz kontynuuje turę
                        {
                            System.out.println("Wykonano skok nad pionkiem");
                            // wysyłanie OK@BOARD do aktywnego gracza
                            commandBuilder = new CommandBuilder();
                            commandBuilder.addCommand( "OK" );
                            commandBuilder.addCommand( "BOARD", gameMaster.getBoardAsString() );
                            players.get( activePlayer ).sendCommand( commandBuilder.getCommand() );

                            // wysyłanie BOARD to reszty graczy (za wyjątkiem activePlayer)
                            sendToOthers( "BOARD " + gameMaster.getBoardAsString(), players.get( activePlayer ) );

                            nextPlayer = false;
                        }
                    }

                }
            } while( !nextPlayer ); // kontynuuj turę dla obecnego gracza (nie zmieniaj gracza)


            // pobierz id kolejnego w kolejce gracza
            activePlayer = getNextPlayer( players, activePlayer );
            if( activePlayer < 0 ) // wczyscy gracze ukończyli mecz
                return;

        } while( true ); // wykonuj tury dla kolejnych graczy
    }

    /**
     * Zwraca id gracza, którego tura nastąpi po 'activePlayer'.
     * @param players wszyscy gracze biorący udział w meczu
     * @param activePlayer id gracza, które tura ostatnio trwała
     * @return id kolejnego gracza w kolejne lub -1 gdy wszyscy gracze skończyli mecz
     */
    private int getNextPlayer( List<Player> players, int activePlayer )
    {
        int nextPlayer = activePlayer;
        boolean nextPlayerFinished;
        do
        {
            nextPlayer++;
            if( nextPlayer >= players.size() )
            {
                nextPlayer = 0;
            }

            nextPlayerFinished = players.get( nextPlayer ).isFinished();

            if( nextPlayerFinished ) // sprawdź czy pozostali jeszcze jacyś gracze
            {
                int howManyFinished = 0;
                for( Player p : players )
                {
                    if( p.isFinished() )
                        howManyFinished++;
                }
                if( howManyFinished == players.size() )
                {
                    //TODO zakończ mecz
                    return -1;
                }
            }
        } while( nextPlayerFinished ); // dopóki nie znajdziesz gracza, który jeszcze nie skończył

        return nextPlayer;
    }

    /**
     * Wysyła komendę do wszystkich graczy z tablicy 'players'
     */
    private void sendToAll(String command)
    {
        for (Player temp : players)
        {
            temp.sendCommand(command);
        }
    }

    /**
     * Wysyła komendę do wszystkich graczy z tablicy 'players',
     * pomijając gracza 'excluded'
     */
    private void sendToOthers(String command, Player excluded)
    {
        for (Player temp : players)
        {
            if (temp != excluded)
                temp.sendCommand(command);
        }
    }
}
