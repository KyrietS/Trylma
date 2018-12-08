package board;

import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import shared.IField;
import shared.PlayerColor;

/**
 * Klasa reprezentująca pojedyncze, klikalne pole w grze
 */
public class Field
{
    private int x;                                  // Współrzędna X pola (kolumna)
    private int y;                                  // Współrzędna Y pola (wiersz)
    private Circle circle;                          // Referencja do odpowiadającego Circle w GUI
    private PlayerColor color = PlayerColor.NONE;   // Kolor pionka na danym polu ("" oznacza pole puste)

    public Field( int x, int y, Circle circle )
    {
        this.x = x;
        this.y = y;
        this.circle = circle;
    }

    /** Zwraca współrzędną X pola */
    public int getX()
    {
        return this.x;
    }

    /** Zwraca współrzędną Y pola */
    public int getY()
    {
        return this.y;
    }

    /**
     * Ustawia na polu pionka o kolorze 'color'
     * Uwaga: color="" oznacza, że na polu nie stoi zaden pionek
     */
    void setColor( PlayerColor color )
    {
        Stop[] stops = { new Stop(0, Color.WHITE), null };

        switch( color )
        {
        case NONE:
            circle.setFill( Color.WHITE );
            return;
        case RED:
            stops[ 1 ] = new Stop( 1, Color.RED );
            break;
        case GREEN:
            stops[ 1 ] = new Stop( 1, Color.GREEN );
            break;
        case YELLOW:
            stops[ 1 ] = new Stop( 1, Color.YELLOW );
            break;
        case BLUE:
            stops[ 1 ] = new Stop( 1, Color.BLUE );
            break;
        case ORANGE:
            stops[ 1 ] = new Stop( 1, Color.ORANGE );
            break;
        case VIOLET:
            stops[ 1 ] = new Stop( 1, Color.VIOLET );
            break;
        default:
            throw new RuntimeException( "Podany kolor nie istnieje: '" + color + "'" );

        }
        this.color = color;
        RadialGradient gradient = new RadialGradient( 0, 0,
                0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops );
        circle.setFill( gradient );
    }

    PlayerColor getColor()
    {
        return color;
    }

    /**
     * Oznacza pole jako zaznaczone lub nie,
     * lekko powiększając kółko
     */
    void setSelected( boolean state )
    {
        if( circle.isDisabled() )
            return;

        if( state )
        {
            circle.setStrokeType( StrokeType.OUTSIDE );
        }
        else
        {
            circle.setStrokeType( StrokeType.INSIDE );
        }

    }

    /** Porównuje referencję do FX'owego Cirlce */
    public boolean circleEquals( Circle circle )
    {
        return this.circle == circle;
    }

    void setMarked( boolean state )
    {
        if( circle.isDisabled() )
            return;

        if( state && color == PlayerColor.NONE )
        {
            circle.setFill( Paint.valueOf( "#47F2FF" ) );
        }
        else if( color == PlayerColor.NONE )
        {
            circle.setFill( Color.WHITE );
        }
    }

    boolean isDisabled()
    {
        return circle.isDisabled();
    }
}
