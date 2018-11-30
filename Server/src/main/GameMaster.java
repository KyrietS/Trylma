package main;

public class GameMaster
{
    public Board board;
    private MovementStrategy movementStrategy;

    public GameMaster(MovementStrategy ms)
    {
        board = new Board();
        movementStrategy = ms;
    }

    void initializeBoardWith(String color)
    {
        //TODO implement

    }

    //weryfikuje poprawność ruchu z pola (x1,y1) na pole (x2,y2) na podstawie podanych zasad movementstrategy
    boolean verifyMove(int x1, int y1, int x2, int y2)
    {
        return movementStrategy.verifyMove(board, x1, y1, x2, y2);
    }

    //wykonuje ruch pionkiem z pola (x1,y1) na pole (x2,y2) na podstawie podanych zasad movementstrategy
    void makeMove(int x1, int y1, int x2, int y2)
    {
        if (verifyMove(x1, y1, x2, y2))
        {
            board = movementStrategy.makeMove(board, x1, y1, x2, y2);
        }
    }

    //Zwraca kolo pola (x,y)
    String getColorAtPos(int x, int y)
    {
        try
        {
            return board.getColor(x, y);
        } catch (UnplayableFieldException ufexc)
        {
            return null;
        }
    }

    // sprawdza czy gracz o podanym kolorze jest zwycięzcą
    boolean isWinner(String color)
    {
        //TODO implement
        return false;
    }

    String getBoardAsString()
    {
        return board.getAsString();
    }
}
