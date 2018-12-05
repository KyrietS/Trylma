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
        board.select( x, y );
        selected = new Coord( x, y );
    }

    public void moveSelectedTo( int x, int y )
    {
        // TODO implement
    }

    public void skipTurn()
    {
        // TODO implement
    }

    public void waitAndListen()
    {
        // TODO implement
    }
}

/**
 * Klasa pomocnicza do przechowywania współrzędnych
 */
class Coord
{
    public int x;
    public int y;
    public Coord( int x, int y )
    {
        this.x = x;
        this.y = y;
    }

}