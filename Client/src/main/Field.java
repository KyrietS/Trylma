package main;

import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import shared.IField;
import shared.PlayerColor;

/**
 * Klasa reprezentująca pojedyncze, klikalne pole w grze
 */
class Field implements IField
{
    private int x;                                  // Współrzędna X pola (kolumna)
    private int y;                                  // Współrzędna Y pola (wiersz)
    private Circle circle;                          // Referencja do odpowiadającego Circle w GUI
    private PlayerColor color = PlayerColor.NONE;   // Kolor pionka na danym polu ("" oznacza pole puste)
    private boolean selected = false;               // Czy pole jest zaznaczone. (Zaznaczone pole wygląda inaczej)
    private boolean marked   = false;               // Czy pole jest podświetlone.

    Field( int x, int y, Circle circle )
    {
        this.x = x;
        this.y = y;
        this.circle = circle;
    }

    /** Zwraca współrzędną X pola */
    int getX()
    {
        return this.x;
    }

    /** Zwraca współrzędną Y pola */
    int getY()
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
        case R:
            stops[ 1 ] = new Stop( 1, Color.RED );
            break;
        case G:
            stops[ 1 ] = new Stop( 1, Color.GREEN );
            break;
        case Y:
            stops[ 1 ] = new Stop( 1, Color.YELLOW );
            break;
        case B:
            stops[ 1 ] = new Stop( 1, Color.BLUE );
            break;
        case O:
            stops[ 1 ] = new Stop( 1, Color.ORANGE );
            break;
        case V:
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
     * zmieniając przy tym jego wygląd
     */
    void setSelected( boolean state )
    {
        //TODO znaznaczenie pola zmienia ramkę
        if( state )
        {
            selected = true;
            circle.setStrokeType( StrokeType.OUTSIDE );
        }
        else
        {
            selected = false;
            circle.setStrokeType( StrokeType.INSIDE );
        }

    }

    /** Sprawdza czy pole jest obecnie zaznaczone */
    boolean isSelected()
    {
        return selected;
    }

    /** Porównuje referencję do FX'owego Cirlce */
    boolean circleEquals( Circle circle )
    {
        return this.circle == circle;
    }

    void setMarked( boolean state )
    {
        if( state && color == PlayerColor.NONE )
        {
            circle.setFill( Paint.valueOf( "#47F2FF" ) );
            marked = true;
        }
        else if( marked )
        {
            circle.setFill( Color.WHITE );
            marked = false;
        }

    }

    @Override
    public PlayerColor getCurrentColor()
    {
        if( color == null || color == PlayerColor.NONE )
            return PlayerColor.NONE;
        else
            return color;
    }

    @Override
    public PlayerColor getNativeColor()
    {
        // TODO implement
        return null;
    }

    @Override
    public PlayerColor getTargetColor()
    {
        // TODO implement
        return null;
    }

    @Override
    public boolean isPlayable()
    {
        return !circle.isDisabled();
    }
}
