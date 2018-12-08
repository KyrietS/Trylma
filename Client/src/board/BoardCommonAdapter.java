package board;

import coord.Coord;
import shared.IBoard;
import shared.IField;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter umożliwiający użycie klasy Board z klienta w publicznych
 * metodach modułu Common.
 */
public class BoardCommonAdapter implements IBoard
{
    /** Adaptowana plansza */
    private Board board;

    public BoardCommonAdapter( Board adaptedBoard )
    {
        this.board = adaptedBoard;
    }

    /**
     * Zwraca współrzędne wszystkich pól znajdujących się w odległości 1 lub 2
     * od pola (x, y), na któe można potencjalnie skoczyć (w linii prostej)
     */
    public List<Coord> getNearbyCoords( int x, int y )
    {
        List<Coord> coords = new ArrayList<>();

        // pola odległe o 1

        // na lewo
        coords.add( new Coord( x - 1, y ) );
        // na prawo
        coords.add( new Coord( x + 1, y ) );
        // góra lewo (dla wiersza nieparzystego, aby dojść do pola góra lewo trzeba zmniejszyć x o 1)
        coords.add( new Coord( ( y % 2 == 0 ? x : x - 1 ), y - 1 ) );
        // góra prawo (dla wiersza parzystego, aby dojść do pola góra prawo trzeba zwiększyć x o 1)
        coords.add( new Coord( ( y % 2 == 0 ? x + 1 : x ), y - 1 ) );
        // dół lewo (dla wiersza nieparzystego trzeba zmniejszyć x o 1)
        coords.add( new Coord( ( y % 2 == 0 ? x : x - 1 ), y + 1 ) );
        // dół prawo (dla wiersza parzystego trzeba zwiększyc x o 1)
        coords.add( new Coord( ( y % 2 == 0 ? x + 1 : x ), y + 1 ) );

        // pola odległe o 2

        // na lewo
        coords.add( new Coord( x - 2, y ) );
        // na prawo
        coords.add( new Coord( x + 2, y ) );
        // góra lewo
        coords.add( new Coord( x - 1, y - 2 ) );
        // góra prawo
        coords.add( new Coord( x + 1, y - 2 ) );
        // dół lewo
        coords.add( new Coord( x - 1, y + 2 ) );
        // dół prawo
        coords.add( new Coord( x + 1, y + 2 ) );

        return coords;
    }

    /**
     * Zwraca pole znajdujące się na pozycji (x, y)
     */
    @Override
    public IField getField( int x, int y )
    {
        for( Field field : board.fields )
        {
            if( field.getX() == x && field.getY() == y )
                return new FieldCommonAdapter( field );
        }

        return null;
    }
}
