package main;

import shared.AdditionalVerifyCondition;
import shared.BasicMovementStrategyVerify;
import shared.JumpStatusVerifyCondition;
import shared.PreviousPawnVerifyCondition;

import java.util.List;

public class Player
{
    private Board board;
    private String color;
    private Coord selected;
    private CommunicationManager communicationManager;

    public Player( CommunicationManager cm, Board board, String color )
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

    private void moveSelectedTo( int x, int y )
    {
        // wysyła propozycję ruchu do serwera
        int fromX = selected.getX();
        int fromY = selected.getY();

        String msg = "MOVE " + fromX + " " + fromY + " " + x + " " + y;
        //communicationManager.writeLine( msg );
    }

    public void skipTurn()
    {
        // TODO implement
    }

    private void waitAndListen()
    {
        // TODO implement
        System.out.println("Czekanie...");
    }

    private void markPossibleJumps( int x, int y )
    {
        JumpStatusVerifyCondition jumpStatusVerifyCondition = new JumpStatusVerifyCondition(0);
        PreviousPawnVerifyCondition previousPawnVerifyCondition = new PreviousPawnVerifyCondition();
        AdditionalVerifyCondition[] conditions = {jumpStatusVerifyCondition, previousPawnVerifyCondition};

        // wyczyść obecne zaznaczenie
        board.unmarkAll();

        //BasicMovementStrategyVerify.verifyMove( board, x, y, ..., ..., conditions );
        int result; // rezultat funkcji verifyMove (jeśli 0, to ruch niepoprawny)


        List<Coord> nearbyCoords = board.getNearbyCoords( x, y );
        for( Coord coord : nearbyCoords )
        {
            result = BasicMovementStrategyVerify.verifyMove( board, x, y, coord.getX(), coord.getY(), conditions );
            if( result != 0 )
                board.mark( coord.getX(), coord.getY() );
        }
    }
}

