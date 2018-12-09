package main;

import board.Board;
import board.BoardCommonAdapter;
import coord.Coord;
import shared.*;

import java.util.List;
import java.util.function.Consumer;

public class Player
{
    private Board board;
    private PlayerColor color;
    private Coord selected;
    private CommunicationManager communicationManager;
    private Consumer<Boolean> blockGUI;

    private boolean turnActive;

    Player( CommunicationManager cm, Board board, Consumer<Boolean> blockGUI ) throws Exception
    {
        this.communicationManager = cm;
        this.board = board;
        this.color = PlayerColor.RED;
        this.blockGUI = blockGUI;

        readColor();
        turnActive = false;

        listen();
    }

    /**
     * Obsługa kliknięcia w pole. Zaznaczanie pola, wykonywanie skoków itp.
     */
    void selectPiece( int x, int y )
    {
        // jeśli kliknięto w pionka swojego koloru
        if( !board.isEmpty( x, y ) && board.getColor( x, y ).equals( color ) )
        {
            // kliknięto w zaznaczonego już pionka. Zakończ turę
            if( selected != null && selected.getX() == x && selected.getY() == y )
            {
                // odznaczanie pól
                board.deselect();
                selected = null;

                communicationManager.writeLine( "SKIP" );
                listen();
            }
            else
            {
                // zaznaczenie pionka
                board.select( x, y );
                selected = new Coord( x, y );
                // podświetlenie pól, na które można skoczyć
                markPossibleJumps( x, y );
            }

        }
        // jakiś jakiś pionek jest zaznaczony i kliknięto w puste pole (wykonanie skoku)
        else if( selected != null && board.isEmpty( x, y ) )
        {
            moveSelectedTo( x, y );

            // odznaczanie pól
            board.deselect();
            selected = null;

            listen();
        }
        else // kliknięto gdzieś tam, odznacz pole (jeśli było zaznaczone)
        {
            board.deselect();
            selected = null;
        }
    }

    /**
     * Zleca serwerowi przesunięcie zaznaczonego pionka (selected)
     * na pozycję (x, y)
     */
    private void moveSelectedTo( int x, int y )
    {
        // wysyła propozycję ruchu do serwera
        int fromX = selected.getX();
        int fromY = selected.getY();

        String msg = "MOVE " + fromX + " " + fromY + " " + x + " " + y;
        communicationManager.writeLine( msg );
    }

    /**
     * Wysyoła do serwera komunikat o zakończeniu tury
     */
    public void skipTurn()
    {
        // TODO implement
    }

    /**
     * Funkcja asynchronicznie wywołuje funkcję listenAndExecute()
     */
    private void listen()
    {
        blockGUI.accept( true );
        Thread thread = new Thread( this:: listenAndExecute );
        thread.setDaemon( true );
        thread.start();
    }

    /**
     * Nasłuchuje wszystkich przycodzących komunikatów. Nasłuchiwanie się
     * kończy, gdy zostanie odebrany komunikat 'YOU'
     */
    private void listenAndExecute()
    {
        do
        {
            String line = null;
            try
            {
                line = communicationManager.readLine();
            }
            catch( Exception e )
            {
                System.out.println( "Utracono połączenie z serwerem: " + e.getMessage() );
                System.exit( -1 );
            }

            Response[] responses = ResponseInterpreter.getResponses( line );

            for( Response response : responses )
            {
                switch( response.getCode() )
                {
                case "YOU":
                    turnActive = true;
                    break;
                case "BOARD":
                    loadBoard( response );
                    break;
                case "END":
                    System.out.println("Zakończyłeś na miejscu " + response.getNumbers()[0]);
                    turnActive = false;
                    break;
                case "OK":
                    System.out.println( "Ruch poprawny" );
                    break;
                case "NOK":
                    System.out.println( "Ruch NIE poprawny" );
                    break;
                case "STOP":
                    turnActive = false;
                    break;
                }
            }

        } while( !turnActive ); // nasłuchuj dopóki nie nastąpi twoja tura

        blockGUI.accept( false );
    }

    /**
     * Podświetla pola w pobliżu pionka (x, y), na które potencjalnie można skoczyć.
     * Przed wykonaniem, usuwa wcześniejsze powietlenie (jeśli istniało)
     */
    private void markPossibleJumps( int x, int y )
    {
        //TODO uwzględniać conditions
        JumpStatusVerifyCondition jumpStatusVerifyCondition = new JumpStatusVerifyCondition(0);
        PreviousPawnVerifyCondition previousPawnVerifyCondition = new PreviousPawnVerifyCondition();
        AdditionalVerifyCondition[] conditions = {jumpStatusVerifyCondition, previousPawnVerifyCondition};

        // wyczyść obecne podświetlenie
        board.unmarkAll();


        // Utworzenie adaptera, aby użyć funkcji z Common
        BoardCommonAdapter commonBoard = new BoardCommonAdapter( board );
        List<Coord> nearbyCoords = commonBoard.getNearbyCoords( x, y );

        int result; // rezultat funkcji verifyMove (jeśli 0, to ruch niepoprawny)
        for( Coord coord : nearbyCoords )
        {
            result = BasicMovementStrategyVerify.verifyMove( commonBoard, x, y, coord.getX(), coord.getY(), conditions );
            if( result != 0 )
                board.mark( coord.getX(), coord.getY() );
        }
    }

    private void readColor() throws Exception
    {
        String line = communicationManager.readLine();
        Response[] responses = ResponseInterpreter.getResponses( line );
        Response response;
        if( responses.length != 1
                || !responses[ 0 ].getCode().equals( "WELCOME" )
                || responses[ 0 ].getWords().length != 1 )
        {
            throw new Exception( "Otrzymano niepoprawny komunikat: " + line );
        }

        color = PlayerColor.valueOf( responses[ 0 ].getWords()[ 0 ] );

        System.out.println( "Jestem graczem: " + color.toString() );
    }

    private void loadBoard( Response response )
    {
        if( response.getCode().equals( "BOARD" ) )
        {
            // czyszczenie planszy
            board.clearBoard();

            // wypełnianie planszy pionkami
            int coordNum = 0;
            for( String word : response.getWords() )
            {
                PlayerColor color = PlayerColor.valueOf( word );
                int x = response.getNumbers()[ coordNum ];
                int y = response.getNumbers()[ coordNum+1 ];

                board.addPiece( x, y, color );
                coordNum += 2;
            }
        }

    }
}

