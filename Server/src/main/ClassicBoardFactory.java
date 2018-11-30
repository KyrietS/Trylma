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
            case 2:
            {
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
            }
        }
        return board;
    }

}
