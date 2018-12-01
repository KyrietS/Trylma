package main;

public abstract class Board
{
    protected Field[][] fields;

    Board()
    {

    }

    //Dodaje pionek(ustawia kolor) o podanym kolorze na pole (x,y)
    void addPiece(int x, int y, String color) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            fields[x][y].setCurrentColor(color);
        } else
        {
            throw new UnplayableFieldException();
        }
    }

    //Usuwa pionek(kolor) z pola (x,y)
    void removePiece(int x, int y) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            fields[x][y].setCurrentColor("none");
        } else
        {
            throw new UnplayableFieldException();
        }
    }

    //Sprawdza czy pole (x,y) jest puste
    boolean isEmpty(int x, int y) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            return fields[x][y].getCurrentColor().contentEquals("none");
        } else
        {
            throw new UnplayableFieldException();
        }
    }

    //Zwraca kolor pola (x,y)
    String getColor(int x, int y) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            return fields[x][y].getCurrentColor();
        } else
        {
            throw new UnplayableFieldException();
        }
    }

    //Zwraca board w postaci stringa
    public abstract String getAsString();


    public Field[][] getFields()
    {
        return fields;
    }
}
