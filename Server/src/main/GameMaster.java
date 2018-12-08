package main;

import shared.AdditionalVerifyCondition;
import shared.PlayerColor;

class GameMaster
{
    private Board board;
    private MovementStrategy movementStrategy;
    private BoardFactory boardFactory;

    GameMaster(MovementStrategy ms, BoardFactory bf)
    {
        board = new ClassicBoard();
        movementStrategy = ms;
        boardFactory = bf;
    }

    void initializeBoard( int numberOfPlayers )
    {
        board = boardFactory.createBoard( numberOfPlayers );
    }

    //weryfikuje poprawność ruchu z pola (x1,y1) na pole (x2,y2) na podstawie podanych zasad movementstrategy
    int verifyMove(int x1, int y1, int x2, int y2, AdditionalVerifyCondition[] additionalVerifyConditions)
    {
        return movementStrategy.verifyMove(board, x1, y1, x2, y2, additionalVerifyConditions);
    }

    //wykonuje ruch pionkiem z pola (x1,y1) na pole (x2,y2) na podstawie podanych zasad movementstrategy
    void makeMove(int x1, int y1, int x2, int y2)
    {
            board = movementStrategy.makeMove(board, x1, y1, x2, y2);
    }

    //Zwraca kolor pola (x,y)
    PlayerColor getColorAtPos(int x, int y)
    {
        try
        {
            return board.getColor(x, y);
        } catch (UnplayableFieldException ufexc)
        {
            return null;
        }
    }


    /*
        sprawdza czy gracz o podanym kolorze jest zwycięzcą
        implementacja funckji przeniesiona do boarda (żeby gamemaster nie musiał mieć wszystkich pól)
     */
    boolean isWinner(PlayerColor color)
    {
        return board.isWinner(color);
    }

    String getBoardAsString()
    {
        return board.getAsString();
    }
}
