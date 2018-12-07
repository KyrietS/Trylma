package main;

import shared.BasicMovementStrategyVerify;
import shared.IBoard;

import java.util.ArrayList;
import java.util.List;

public class Board implements IBoard
{
    private List<Field> fields;
    private Field selectedField = null;

    public Board( List<Field> fields )
    {
        this.fields = fields;
    }

    public void select( int x, int y )
    {
        if( selectedField != null )
            selectedField.setSelected( false );

        Field field = getField( x, y );
        field.setSelected( true );
        selectedField = field;
    }

    void deselect()
    {
        if( selectedField != null )
        {
            selectedField.setSelected( false );
            selectedField = null;
            unmarkAll();
        }
    }

    public void addPiece( int x, int y, String color )
    {
        Field field = getField( x, y );
        field.setColor( color );
    }

    public boolean isEmpty( int x, int y )
    {
        Field field = getField( x, y );
        return field.getColor().equals( "" );
    }

    public String getColor( int x, int y )
    {
        Field field = getField( x, y );
        return field.getColor();
    }

    public void mark( int x, int y )
    {
        Field field = getField( x, y );
        if( field != null )
        {
            field.setMarked( true );
        }
    }

    public void unmarkAll()
    {
        for( Field field : fields )
        {
            field.setMarked( false );
        }
    }

    public List<Coord> getNerbyCoords( int x, int y )
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
        //na prawo
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

    @Override
    public Field getField( int x, int y )
    {
        for( Field field : fields )
        {
            if( field.getX() == x && field.getY() == y )
                return field;
        }
        //throw new RuntimeException( "Nie znaleziono pionka o współrzędnych: (" + x + ", " + y + ")" );
        return null;
    }
}
