package main;

import movement.*;
import serverboard.ClassicBoardFactory;
import player.Player;
import player.RealPlayer;
import shared.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Match
{
    private GameMaster gameMaster;
    private List<Player> players;
    private PlayerColor[] availableColors;

    private JumpStatusVerifyCondition jumpStatus;
    private PreviousPawnVerifyCondition previousPawn;
    private AdditionalVerifyCondition[] conditions;
    private boolean turnFinished;
    private int place;

    Match( List<Socket> playerSockets, int numberOfBots ) throws Exception
    {
        gameMaster = new GameMaster(new BasicMovementStrategy(), new ClassicBoardFactory());
        players = new ArrayList<>();

        int numberOfRealPlayers = playerSockets.size();
        gameMaster.initializeBoard( numberOfRealPlayers );

        int totalNumberOfPlayers = numberOfRealPlayers + numberOfBots;
        availableColors = gameMaster.getPossibleColorsForPlayers( totalNumberOfPlayers );

        turnFinished = true;
        place = 1;

        addRealPlayers( playerSockets );
        //TODO addBotPlayers( numberOfBots )
    }

    private void addRealPlayers( List<Socket> playerSockets ) throws Exception
    {
        int numberOfRealPlayers = playerSockets.size();

        for( int i = 0; i < numberOfRealPlayers; i++ )
        {
            players.add( new RealPlayer( playerSockets.get( i ), availableColors[ i ] ) );
        }
    }

    void start()
    {
        try
        {
            sendWelcomeToPlayers();
            sendStartBoardToPlayers();
            play();
        }
        catch( Exception e)
        {
            endMatchWithError( e.getMessage() );
        }
    }

    private void sendWelcomeToPlayers() throws Exception
    {
        for( int i = 0; i < players.size(); i++ )
        {
            players.get( i ).sendCommand( "WELCOME " + availableColors[ i ] );
        }
    }

    private void sendStartBoardToPlayers() throws Exception
    {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addCommand( "START" );
        commandBuilder.addCommand( "BOARD", gameMaster.getBoardAsString() );
        sendToAll( commandBuilder.getCommand() );
    }

    private void play() throws Exception
    {
        int indexOfPlayerHavingTurn = 0;
        do
        {
            playTurnForPlayer( players.get( indexOfPlayerHavingTurn ) );
            indexOfPlayerHavingTurn = getNextPlayer( indexOfPlayerHavingTurn );
        } while( !allPlayersFinished() );
        System.out.println("Koniec meczu");
    }

    private void playTurnForPlayer( Player player ) throws Exception
    {
        setDefaultSettingsForNewTurn();
        player.sendCommand( "YOU" );
        System.out.println("Turę zaczyna gracz: " + player.getColor().toString());
        readResponsesAndExecute(player);
    }

    private void setDefaultSettingsForNewTurn()
    {
        jumpStatus = new JumpStatusVerifyCondition( 0 );
        previousPawn = new PreviousPawnVerifyCondition();
        conditions = new AdditionalVerifyCondition[]{ jumpStatus, previousPawn };

        turnFinished = true;
    }

    private void readResponsesAndExecute(Player player) throws Exception
    {
        do
        {
            Response response = readProperResponseFromPlayer( player );
            executeResponse( player, response );
        } while( !turnFinished );
    }

    private Response readProperResponseFromPlayer( Player player ) throws Exception
    {
        System.out.print("Oczekiwanie na odpowiedź od gracza... ");
        String line = player.readResponse();
        System.out.println("odebrano: " + line);
        Response[] responses = ResponseInterpreter.getResponses( line );
        if( responses.length != 1 )
        {
            System.out.println( "Otrzymano nieprawidłowy komunikat od gracza " + player.getColor().toString() );
            player.sendCommand( "NOK" );
        }
        return responses[0];
    }

    private void executeResponse( Player player, Response response ) throws Exception
    {
        if( response.getCode().equals( "SKIP" ) )
        {
            player.sendCommand( "STOP" );
            turnFinished = true;
        }
        if( response.getCode().equals( "CLUES" ) )
        {
            int x = response.getNumbers()[0];
            int y = response.getNumbers()[1];
            sendClues( player, x, y, conditions );
            turnFinished = false;
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
                player.sendCommand( "NOK" );
                turnFinished = false;
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
                boolean winner = gameMaster.isWinner( player.getColor() );

                if( winner ) // gracz wygrał
                {
                    System.out.println( "WYGRANA" );
                    // wyślij komunikat o zwycięstwie
                    player.sendCommand( "END " + place );

                    // oznacz gracza jako zwycięzcę
                    player.setFinished( true );
                    place++;    // następny gracz zajmie kolejne miejsce

                    sendToAll( "BOARD " + gameMaster.getBoardAsString() );

                    turnFinished = true;

                }
                else if( result == 1 ) // wykonany ruch nie był skokiem (przemieszczenie o 1 pole)
                {
                    System.out.println("Wykonano krótki ruch. Koniec tury");
                    player.sendCommand( "OK@STOP" );
                    sendToAll( "BOARD " + gameMaster.getBoardAsString() );

                    turnFinished = true;
                }
                else // ruch był skokiem, gracz kontynuuje turę
                {
                    System.out.println("Wykonano skok nad pionkiem");
                    // wysyłanie OK@BOARD do aktywnego gracza
                    CommandBuilder commandBuilder = new CommandBuilder();

                    commandBuilder.addCommand( "OK" );
                    commandBuilder.addCommand( "BOARD", gameMaster.getBoardAsString() );
                    player.sendCommand( commandBuilder.getCommand() );

                    // wysyłanie BOARD to reszty graczy (za wyjątkiem activePlayer)
                    sendToOthers( "BOARD " + gameMaster.getBoardAsString(), player );

                    turnFinished = false;
                }
            }

        }
    }

    private boolean allPlayersFinished()
    {
        for( Player player : players )
        {
            if( !player.isFinished() )
                return false;
        }
        return true;
    }

    /**
     * Zwraca id gracza, którego tura nastąpi po 'activePlayer'.
     * @param activePlayer id gracza, które tura ostatnio trwała
     * @return id kolejnego gracza w kolejne lub -1 gdy wszyscy gracze skończyli mecz
     */
    private int getNextPlayer( int activePlayer )
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
    private void sendToAll(String command) throws Exception
    {
        for (Player player : players)
        {
            player.sendCommand(command);
        }
    }

    /**
     * Wysyła komendę do wszystkich graczy z tablicy 'players',
     * pomijając gracza 'excluded'
     */
    private void sendToOthers(String command, Player excluded) throws Exception
    {
        for (Player player : players)
        {
            if (player != excluded)
                player.sendCommand(command);
        }
    }

    private void sendClues( Player player, int x, int y, AdditionalVerifyCondition[] conditions ) throws Exception
    {

        ((PreviousPawnVerifyCondition)conditions[1]).setCurrentXY( x, y );

        List<Coord> possibleMoves = gameMaster.getPossibleMovesForPos( x, y, conditions );
        String command = getCluesCommand( possibleMoves );

        player.sendCommand( command );
    }

    private String getCluesCommand( List<Coord> possibleMoves )
    {
        StringBuilder sb = new StringBuilder();
        for( Coord c : possibleMoves )
        {
            if( !sb.toString().equals( "" ) )
                sb.append( " " );
            sb.append( c.getX() ).append( " " ).append( c.getY() );
        }
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addCommand( "CLUES", sb.toString() );

        return commandBuilder.getCommand();
    }

    private void endMatchWithError( String message )
    {
        System.out.println("Przerwanie meczu: " + message);
        for( Player player : players )
        {
            try
            {
                player.sendCommand( "ERROR " + message );
            }
            catch( Exception ignored ){}
        }

        players.clear();
        players = null;
    }
}
