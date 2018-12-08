package board;

import shared.PlayerColor;

import java.util.List;

public class Board
{
    private Field selectedField = null;
    protected List<Field> fields;

    public Board( List<Field> fields )
    {
        this.fields = fields;
    }

    public void select( int x, int y )
    {
        if( selectedField != null )
            selectedField.setSelected( false );

        Field field = getField( x, y );
        if( field != null )
        {
            field.setSelected( true );
            selectedField = field;
        }

    }

    public void deselect()
    {
        if( selectedField != null )
        {
            selectedField.setSelected( false );
            selectedField = null;
            unmarkAll();
        }
    }

    public void addPiece( int x, int y, PlayerColor color )
    {
        Field field = getField( x, y );
        if( field != null )
            field.setColor( color );
    }

    public boolean isEmpty( int x, int y )
    {
        Field field = getField( x, y );
        if( field != null )
            return field.getColor() == PlayerColor.NONE;
        else
            return true;
    }

    public PlayerColor getColor( int x, int y )
    {
        Field field = getField( x, y );
        if( field != null )
            return field.getColor();
        else
            return PlayerColor.NONE;
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

    private Field getField( int x, int y )
    {
        for( Field field : fields )
        {
            if( field.getX() == x && field.getY() == y )
                return field;
        }

        return null;
    }
}
