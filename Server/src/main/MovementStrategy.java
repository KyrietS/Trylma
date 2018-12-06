package main;

import shared.AdditionalVerifyCondition;
import shared.IBoard;

public interface MovementStrategy
{
    /*

     */
    int verifyMove(IBoard board, int x1, int y1, int x2, int y2, AdditionalVerifyCondition[] additionalVerifyConditions);

    Board makeMove(Board board, int x1, int y1, int x2, int y2);
}
