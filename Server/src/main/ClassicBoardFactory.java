package main;

public class ClassicBoardFactory implements BoardFactory
{
    public Board createBoard(String[] colors)
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
                board.getFields()[6][0] = new Field(colors[0], colors[0], colors[1], true);
                board.getFields()[5][1] = new Field(colors[0], colors[0], colors[1], true);
                board.getFields()[6][1] = new Field(colors[0], colors[0], colors[1], true);
                board.getFields()[5][2] = new Field(colors[0], colors[0], colors[1], true);
                board.getFields()[6][2] = new Field(colors[0], colors[0], colors[1], true);
                board.getFields()[7][2] = new Field(colors[0], colors[0], colors[1], true);
                board.getFields()[4][3] = new Field(colors[0], colors[0], colors[1], true);
                board.getFields()[5][3] = new Field(colors[0], colors[0], colors[1], true);
                board.getFields()[6][3] = new Field(colors[0], colors[0], colors[1], true);
                board.getFields()[7][3] = new Field(colors[0], colors[0], colors[1], true);
                //dolny promień
                board.getFields()[6][16] = new Field(colors[1], colors[1], colors[0], true);
                board.getFields()[5][15] = new Field(colors[1], colors[1], colors[0], true);
                board.getFields()[6][15] = new Field(colors[1], colors[1], colors[0], true);
                board.getFields()[5][14] = new Field(colors[1], colors[1], colors[0], true);
                board.getFields()[6][14] = new Field(colors[1], colors[1], colors[0], true);
                board.getFields()[7][14] = new Field(colors[1], colors[1], colors[0], true);
                board.getFields()[4][13] = new Field(colors[1], colors[1], colors[0], true);
                board.getFields()[5][13] = new Field(colors[1], colors[1], colors[0], true);
                board.getFields()[6][13] = new Field(colors[1], colors[1], colors[0], true);
                board.getFields()[7][13] = new Field(colors[1], colors[1], colors[0], true);

                //reszta pól
                //zmienne pomocnicze do inicjalizacji pól
                int[] beginnings = {0, 0, 1, 1, 2, 1, 1, 0, 0};
                int[] endings = {12, 11, 11, 10, 10, 10, 11, 11, 12};
                //iteracja po j - rzędach
                for (int j = 4; j <= 12; j++)
                {
                    //iteracja po kolumnach
                    for (int i = beginnings[j - 4]; i <= endings[j - 4]; i++)
                    {
                        board.getFields()[i][j] = new Field("none", "none", "none", true);
                    }
                }
            }
        }
        return board;
    }

}
