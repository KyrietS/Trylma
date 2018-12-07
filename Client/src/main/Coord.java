package main;

/**
 * Klasa pomocnicza do przechowywania współrzędnych
 */
class Coord
{
    private int x;
    private int y;
    Coord( int x, int y )
    {
        this.x = x;
        this.y = y;
    }

    int getX()
    {
        return x;
    }

    int getY()
    {
        return y;
    }

}
