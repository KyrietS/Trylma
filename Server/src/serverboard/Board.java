package serverboard;


import shared.IBoard;
import shared.IField;
import shared.PlayerColor;

/**
 * Reprezentuje planszę na której odbywa się rozgrywka
 */
public abstract class Board implements IBoard

{
    int columns, rows;
    Field[][] fields;

    Board()
    {

    }

    /**
     * Dodaje pionek o podanym kolorze na pole (x,y)
     */
    public void addPiece(int x, int y, PlayerColor color) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            fields[x][y].setCurrentColor(color);
        } else
        {
            throw new UnplayableFieldException();
        }
    }

    /**
     * Usuwa pionek z pola (x,y)
     */
    public void removePiece(int x, int y) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            // TODO CHECK UNEXPECTED BEHAVIOR
            fields[x][y].setCurrentColor(PlayerColor.NONE);
            //setField(x, y, new Field(PlayerColor.NONE, fields[x][y].getNativeColor(), fields[x][y].getTargetColor(), true));

        } else
        {
            throw new UnplayableFieldException();
        }
    }

    /**
     * Zwraca kolor pionka na polu (x,y)
     */

    public PlayerColor getColor(int x, int y) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            return fields[x][y].getCurrentColor();
        } else
        {
            throw new UnplayableFieldException();
        }
    }

    /**
     * Zwraca board w postaci stringa
     */
    public abstract String getAsString();

    /**
     * Sprawdza czy wszyskie pionki danego koloru są już w swoim celu (warunek zwycięstwa)
     */
    public abstract boolean isWinner(PlayerColor color);

    @Override
    public IField getField(int x, int y)
    {
        if (x < 1 || y < 1 || x > columns || y > rows)
        {
            return null;
        } else
        {
            return fields[x][y];
        }
    }

    public void setField(int x, int y, Field f)
    {
        if (x < 1 || y < 1 || x > columns || y > rows)
        {
            throw new NullPointerException();
        } else
        {
            fields[x][y] = f;
        }
    }
}
