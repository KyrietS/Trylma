package board;

import javafx.application.Platform;
import shared.PlayerColor;

import java.util.List;

/**
 * Reprezentacja planszy wraz z polami i pionkami
 */
public class Board
{
    /** obecnie zaznaczone pole z pionkiem */
    private Field selectedField = null;

    /** wszystkie pola na planszy */
    List<Field> fields;

    public Board( List<Field> fields )
    {
        this.fields = fields;
    }

    /**
     * Czyści całą planszę z pionków
     */
    public void clearBoard()
    {
        for( Field field : fields )
        {
            if( !field.isDisabled() )
                field.setColor( PlayerColor.NONE );
        }
    }

    /**
     * Zaznaczenie pionka (pola) na pozycji (x, y).
     * Funkcja odznacza również uprzednio zaznaczone pole.
     */
    public void select( int x, int y )
    {
        deselect();

        Field field = getField( x, y );
        if( field != null )
        {
            field.setSelected( true );
            selectedField = field;
        }

    }

    /**
     * Odznacza obecnie zaznaczone pole oraz wyłącza podświetlenie
     * pól dookoła tego pola
     */
    public void deselect()
    {
        if( selectedField != null )
        {
            selectedField.setSelected( false );
            selectedField = null;
            unmarkAll();
        }
    }

    /**
     * Dodaje pionek na pozycję (x, y) o kolorze color
     * @param x pozycja x pola
     * @param y pozycja y pola
     * @param color kolor pionka do wstawienia w pole
     */
    public void addPiece( int x, int y, PlayerColor color )
    {
        Field field = getField( x, y );
        if( field != null )
            Platform.runLater( () -> field.setColor( color ) );
    }

    /**
     * Sprawdza czy na polu (x, y) nie stoi pionek
     * @return false, gdy na polu stoi pionek, true gdy pole jest puste lub nie istnieje
     */
    public boolean isEmpty( int x, int y )
    {
        Field field = getField( x, y );
        if( field != null )
            return field.getColor() == PlayerColor.NONE;
        else
            return true;
    }

    /**
     * Zwraca kolor pionka na polu (x, y)
     * @return kolor pionka na (x, y) lub NONE, gdy pionka nie ma lub pole nie istnieje
     */
    public PlayerColor getColor( int x, int y )
    {
        Field field = getField( x, y );
        if( field != null )
            return field.getColor();
        else
            return PlayerColor.NONE;
    }

    /**
     * Podświetla pole (x, y)
     * Używane do podświetlania pól, na które można skoczyć.
     */
    public void mark( int x, int y )
    {
        Field field = getField( x, y );
        if( field != null )
        {
            field.setMarked( true );
        }
    }

    /**
     * Wyłącza podświetlenie wszystkich pól na planszy.
     */
    public void unmarkAll()
    {
        for( Field field : fields )
        {
            field.setMarked( false );
        }
    }

    /**
     * Zwraca pole Field znajdujące się na współrzędnych (x, y)
     * @return pole na (x, y) lub null, gdy pole nie istnieje
     */
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
