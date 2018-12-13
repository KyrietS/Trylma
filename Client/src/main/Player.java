package main;

import board.Board;
import javafx.application.Platform;
import shared.Coord;
import shared.*;

import java.util.function.Consumer;

public class Player
{
    private Board board;
    private PlayerColor color;
    private Coord selected;
    private CommunicationManager communicationManager;
    private Consumer<Boolean> blockGUI;
    private Consumer<String> printSuccess;
    private Consumer<String> printAlert;
    private Consumer<String> printError;

    private boolean turnActive;

    Player( CommunicationManager cm, Board board,
            Consumer<Boolean> blockGUI,
            Consumer<String> printSuccess,
            Consumer<String> printAlert,
            Consumer<String> printError ) throws Exception
    {
        this.communicationManager = cm;
        this.board = board;
        this.color = PlayerColor.RED;
        this.blockGUI = blockGUI;

        this.printSuccess = printSuccess;
        this.printAlert = printAlert;
        this.printError = printError;

        readColor();
        turnActive = false;
        printSuccess.accept( "Połączono. Oczekiwanie na pozostałych graczy..." );

        listen();
    }

    /**
     * Obsługa kliknięcia w pole. Zaznaczanie pola, wykonywanie skoków itp.
     */
    void selectPiece( int x, int y )
    {
        // jeśli kliknięto w pionka swojego koloru
        if( !board.isFieldEmpty( x, y ) && board.getColor( x, y ).equals( color ) )
        {
            // kliknięto w zaznaczonego już pionka. Zakończ turę
            if( selected != null && selected.getX() == x && selected.getY() == y )
            {
                // odznaczanie pól
                board.deselectAndUnmarkAllFields();
                selected = null;

                skipTurn();
                listen();
            }
            else
            {
                System.out.println("Pytam o CLUES");
                // zaznaczenie pionka
                board.selectField( x, y );
                selected = new Coord( x, y );
                // poproś o pola, na które można skoczyć
                askServerForClues( x, y );
                listen();
            }

        }
        // jakiś jakiś pionek jest zaznaczony i kliknięto w puste pole (wykonanie skoku)
        else if( selected != null && board.isFieldEmpty( x, y ) )
        {
            moveSelectedTo( x, y );
            // odznaczanie pól
            board.deselectAndUnmarkAllFields();
            selected = null;

            listen();
        }
        else // kliknięto gdzieś tam, odznacz pole (jeśli było zaznaczone)
        {
            board.deselectAndUnmarkAllFields();
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
    private void skipTurn()
    {
        communicationManager.writeLine( "SKIP" );
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
            String line;
            try
            {
                line = communicationManager.readLine();
            }
            catch( Exception e )
            {
                printError.accept( "Utracono połączenie z serwerem" );
                return;
            }

            Response[] responses = ResponseInterpreter.getResponses( line );

            for( Response response : responses )
            {
                System.out.println( "Odebrano: " + response.getCode() );
                switch( response.getCode() )
                {
                case "YOU":
                    printSuccess.accept( "Twoja tura" );
                    turnActive = true;
                    break;
                case "BOARD":
                    loadBoard( response );
                    break;
                case "CLUES":
                    loadClues( response );
                    break;
                case "END":
                    printSuccess.accept( "Koniec meczu. Zajmujesz " + response.getNumbers()[ 0 ] + " miejsce" );
                    turnActive = false;
                    break;
                case "OK":
                    System.out.println( "Ruch poprawny" );
                    break;
                case "NOK":
                    //printAlert.accept( "Ruch niepoprawny!" );
                    System.out.println( "Ruch NIE poprawny" );
                    break;
                case "STOP":
                    printAlert.accept( "Trwa tura innego gracza..." );
                    turnActive = false;
                    break;
                }
            }

        } while( !turnActive ); // nasłuchuj dopóki nie nastąpi twoja tura

        blockGUI.accept( false );
    }

    private void askServerForClues( int x, int y )
    {
        communicationManager.writeLine( "CLUES " + x + " " + y );
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
            board.removeAllPieces();

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

    private void loadClues( Response response )
    {
        if( response.getCode().equals( "CLUES" ) )
        {
            Platform.runLater( () -> board.unmarkAllPossibleJumpTargets() );

            int numbers[] = response.getNumbers();

            System.out.print("Odebrano: ");
            for( int n : numbers )
            {
                System.out.print( n + " " );
            }
            System.out.println("");

            for( int i = 0; i < numbers.length - 1; i += 2 )
            {

                int x = numbers[ i ];
                int y = numbers[ i+1 ];
                Platform.runLater( () -> board.markFieldAsPossibleJumpTarget( x, y ) );
            }
        }
    }
}

