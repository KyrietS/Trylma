package main;

public class BasicMovementStrategy implements MovementStrategy
{
    private BasicMovementStrategyVerify basicMovementStrategyVerify = new BasicMovementStrategyVerify();
    @Override
    public int verifyMove(IBoard board, int x1, int y1, int x2, int y2)
    {
        return basicMovementStrategyVerify.verifyMove(board, x1, y1, x2, y2);
    }

    @Override
    public Board makeMove(Board board, int x1, int y1, int x2, int y2)
    {
        if (verifyMove(board, x1, y1, x2, y2) != 0)
        {
            try
            {
                board.addPiece(x2, y2, board.getColor(x1, y1));
                board.removePiece(x1, y1);
                return board;
            } catch (UnplayableFieldException ufexc)
            {
                return board;
            }
        } else
            return board;
    }
}
