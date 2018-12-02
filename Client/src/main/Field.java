package main;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Klasa reprezentująca pojedyncze, klikalne pole w grze
 */
class Field
{
    private int x;                      // Współrzędna X pola (kolumna)
    private int y;                      // Współrzędna Y pola (wiersz)
    private Circle circle;              // Referencja do odpowiadającego Circle w GUI
    private boolean selected = false;   // Czy pole jest zaznaczone. (Zaznaczone pole wygląda inaczej)

    Field( int x, int y, Circle circle )
    {
        this.x = x;
        this.y = y;
        this.circle = circle;
    }

    /**
     * Zwraca referencję do Circle w GUI, która odpowiada temu polu
     */
    Circle getCircle()
    {
        return circle;
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
     * Oznacza pole jako zaznaczone lub nie,
     * zmieniając przy tym jego wygląd
     */
    void setSelected( boolean state )
    {
        if( state )
        {
            selected = true;
            circle.setFill( Paint.valueOf( "#7afffa" ) );
        }
        else
        {
            selected = false;
            circle.setFill( Paint.valueOf( "#ffffff" ) );
        }
    }

    /** Sprawdza czy pole jest obecnie zaznaczone */
    boolean isSelected()
    {
        return selected;
    }
}
