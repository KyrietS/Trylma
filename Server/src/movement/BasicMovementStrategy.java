package movement;

import serverboard.Board;
import serverboard.UnplayableFieldException;
import shared.AdditionalVerifyCondition;
import shared.BasicMovementStrategyVerify;
import shared.IBoard;

public class BasicMovementStrategy implements MovementStrategy
{
    @Override
    public int verifyMove(IBoard board, int x1, int y1, int x2, int y2, AdditionalVerifyCondition[] additionalVerifyConditions)
    {
        return BasicMovementStrategyVerify.verifyMove(board, x1, y1, x2, y2, additionalVerifyConditions);
    }

    @Override
    public Board makeMove(Board board, int x1, int y1, int x2, int y2)
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

    }
}
