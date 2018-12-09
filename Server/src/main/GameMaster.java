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

    /**
     * Tworzy planszę początkową na 'numberOfPlayers' graczy.
     * @param numberOfPlayers liczba graczy w meczu
     * @return tablica kolorów do przydzielenia
     */
    PlayerColor[] initializeBoard( int numberOfPlayers )
    {
        board = boardFactory.createBoard( numberOfPlayers );
        switch( numberOfPlayers )
        {
        case 1:
            return new PlayerColor[]{ PlayerColor.RED };
        case 2:
            return new PlayerColor[]{ PlayerColor.RED, PlayerColor.GREEN };
        case 3:
            return new PlayerColor[]{ PlayerColor.RED, PlayerColor.BLUE, PlayerColor.YELLOW };
        case 4:
            return new PlayerColor[]{ PlayerColor.RED, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.ORANGE };
        case 6:
            if( PlayerColor.values().length == 6 )
                return PlayerColor.values();
        }
        throw new RuntimeException( "Podano nieprawidłową liczbę graczy: " + numberOfPlayers );
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
