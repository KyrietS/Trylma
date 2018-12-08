package main;

import shared.PlayerColor;

public class ClassicBoardFactory implements BoardFactory
{
    public Board createBoard( PlayerColor[] colors)
    {
        ClassicBoard board = new ClassicBoard();
        //
        int numberOfColors = colors.length;
        switch (numberOfColors)
        {
            //tworzy board dla 2 graczy
            case 2:
            {
                //górny promień
                board.setField(7, 1, new Field(colors[0], colors[0], colors[1], true));
                board.setField(6, 2, new Field(colors[0], colors[0], colors[1], true));
                board.setField(7, 2, new Field(colors[0], colors[0], colors[1], true));
                board.setField(6, 3, new Field(colors[0], colors[0], colors[1], true));
                board.setField(7, 3, new Field(colors[0], colors[0], colors[1], true));
                board.setField(8, 3, new Field(colors[0], colors[0], colors[1], true));
                board.setField(5, 4, new Field(colors[0], colors[0], colors[1], true));
                board.setField(6, 4, new Field(colors[0], colors[0], colors[1], true));
                board.setField(7, 4, new Field(colors[0], colors[0], colors[1], true));
                board.setField(8, 4, new Field(colors[0], colors[0], colors[1], true));
                //dolny promień
                board.setField(7, 17, new Field(colors[1], colors[1], colors[0], true));
                board.setField(6, 16, new Field(colors[1], colors[1], colors[0], true));
                board.setField(7, 16, new Field(colors[1], colors[1], colors[0], true));
                board.setField(6, 15, new Field(colors[1], colors[1], colors[0], true));
                board.setField(7, 15, new Field(colors[1], colors[1], colors[0], true));
                board.setField(8, 15, new Field(colors[1], colors[1], colors[0], true));
                board.setField(5, 14, new Field(colors[1], colors[1], colors[0], true));
                board.setField(6, 14, new Field(colors[1], colors[1], colors[0], true));
                board.setField(7, 14, new Field(colors[1], colors[1], colors[0], true));
                board.setField(8, 14, new Field(colors[1], colors[1], colors[0], true));

                //reszta pól
                //zmienne pomocnicze do inicjalizacji pól
                int[] beginnings = {1, 1, 2, 3, 3, 2, 2, 1, 1};
                int[] endings = {13, 12, 12, 11, 11, 11, 12, 12, 13};
                //iteracja po j - rzędach
                for (int j = 5; j <= 13; j++)
                {
                    //iteracja po kolumnach
                    for (int i = beginnings[j - 5]; i <= endings[j - 5]; i++)
                    {
                        board.setField(i, j, new Field(PlayerColor.NONE, PlayerColor.NONE, PlayerColor.NONE, true));
                    }
                }
            }
            //TODO implementacja dla innej liczby graczy
        }
        return board;
    }

}
