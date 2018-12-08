package main;

import board.Board;
import board.BoardCommonAdapter;
import coord.Coord;
import shared.*;

import java.util.List;

public class Player
{
    private Board board;
    private PlayerColor color;
    private Coord selected;
    private CommunicationManager communicationManager;

    Player( CommunicationManager cm, Board board, PlayerColor color )
    {
        this.communicationManager = cm;
        this.board = board;
        this.color = color;
    }

    /**
     * Oznaczenie pola jako aktywnego. Aktywne pole jest podświetlone i
     * wyświetlane są do niego sugerowane miejsca skoków.
     */
    void selectPiece( int x, int y )
    {
        // jeśli kliknięto w pionka swojego koloru, to go zaznacz
        if( !board.isEmpty( x, y ) && board.getColor( x, y ).equals( color ) )
        {
            // zaznaczenie pionka
            board.select( x, y );
            selected = new Coord( x, y );
            // podświetlenie pól, na które można skoczyć
            markPossibleJumps( x, y );
        }
        // jakiś pionek jest zaznaczony i kliknięto w puste pole (wykonanie skoku)
        else if( selected != null && board.isEmpty( x, y ) )
        {
            moveSelectedTo( x, y );

            // odznaczanie pól
            board.deselect();
            selected = null;

            // TODO musi być asynchroniczna (?)
            waitAndListen();
        }
        else // kliknięto gdzieś tam, odznacz pole (jeśli było zaznaczone)
        {
            board.deselect();
            selected = null;
        }
    }

    /**
     * Zleca serwerowi przesunięcie zaznaczonego pionka (celected)
     * na pozycję (x, y)
     */
    private void moveSelectedTo( int x, int y )
    {
        // wysyła propozycję ruchu do serwera
        int fromX = selected.getX();
        int fromY = selected.getY();

        String msg = "MOVE " + fromX + " " + fromY + " " + x + " " + y;
        //communicationManager.writeLine( msg );
    }

    /**
     * Wysyoła do serwera komunikat o zakończeniu tury
     */
    public void skipTurn()
    {
        // TODO implement
    }

    /**
     * Nasłuchuje wszystkich przycodzących komunikatów. Nasłuchiwanie się
     * kończy, gdy zostanie odebrany komunikat 'YOU'
     */
    private void waitAndListen()
    {
        // TODO implement
        System.out.println("Czekanie...");
    }

    /**
     * Podświetla pola w pobliżu pionka (x, y), na które potencjalnie można skoczyć.
     * Przed wykonaniem, usuwa wcześniejsze powietlenie (jeśli istniało)
     */
    private void markPossibleJumps( int x, int y )
    {
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
}

