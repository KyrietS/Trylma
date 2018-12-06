package main;

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
    public void selectPiece( int x, int y )
    {
        // jeśli kliknięto w pionka swojego koloru, to go zaznacz
        if( !board.isEmpty( x, y ) && board.getColor( x, y ).equals( color ) )
        {
            board.select( x, y );
            selected = new Coord( x, y );
        }
        else if( selected != null && board.isEmpty( x, y ) ) // po zaznaczeniu pionka kliknięto w puste pole
        {
            moveSelectedTo( x, y );

            // TODO musi być asynchroniczna (?)
            waitAndListen();
        }
        else // kliknięto gdzieś tam, odznacz pole (jeśli było zaznaczone)
        {
            board.unselectSelected();
        }
    }

    private void moveSelectedTo( int x, int y )
    {
        // wysyła propozycję ruchu do serwera
        int fromX = selected.getX();
        int fromY = selected.getY();

        String msg = "MOVE " + fromX + " " + fromY + " " + x + " " + y;
        communicationManager.writeLine( msg );
    }

    public void skipTurn()
    {
        // TODO implement
    }

    private void waitAndListen()
    {
        // TODO implement
        System.out.println();
    }
}

/**
 * Klasa pomocnicza do przechowywania współrzędnych
 */
class Coord
{
    private int x;
    private int y;
    Coord( int x, int y )
    {
        this.x = x;
        this.y = y;
    }

    int getX()
    {
        return x;
    }

    int getY()
    {
        return y;
    }

}