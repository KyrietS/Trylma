package main;

import shared.IBoard;
import shared.IField;

public abstract class Board implements IBoard

{
    int columns, rows;
    Field[][] fields;

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

    //Sprawdza czy wszyskie pionki danego koloru są już w swoim celu
    public abstract boolean isWinner(String color);

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

    void setField(int x, int y, Field f)
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
