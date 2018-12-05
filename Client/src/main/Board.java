package main;

import java.util.List;

public class Board
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

    private Field getField( int x, int y )
    {
        for( Field field : fields )
        {
            if( field.getX() == x && field.getY() == y )
                return field;
        }
        throw new RuntimeException( "Nie znaleziono pionka o współrzędnych: (" + x + ", " + y + ")" );
    }
}
