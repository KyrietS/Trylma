package main;

import shared.IBoard;

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
