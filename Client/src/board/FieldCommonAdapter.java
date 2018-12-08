package board;

import shared.IField;
import shared.PlayerColor;

/**
 * Adapter umożliwiający korzystanie ze współdzielonych funkcji z modułu Common.
 * Adapter jest wykorzystywany przez BoardCommonAdapter
 * @see BoardCommonAdapter
 */
public class FieldCommonAdapter implements IField
{
    /** Adaptowane pole */
    private Field field;

    FieldCommonAdapter( Field adaptedField )
    {
        this.field = adaptedField;
    }

    /** Zwraca kolor pionka na polu */
    @Override
    public PlayerColor getCurrentColor()
    {
        if( field.getColor() == null || field.getColor() == PlayerColor.NONE )
            return PlayerColor.NONE;
        else
            return field.getColor();
    }

    /** Zwraca bazowy kolor pola (obramowanie pola) */
    @Override
    public PlayerColor getNativeColor()
    {
        // TODO implement
        return null;
    }

    /**
     * Zwraca kolor pionka, który musi się znaleźć na tym polu, aby spełnić warunek zwycięstwa.
     */
    @Override
    public PlayerColor getTargetColor()
    {
        // TODO implement
        return null;
    }

    /** Informuje, czy pole uczestniczy w rozgrywce (jest częścią grywalnej planszy) */
    @Override
    public boolean isPlayable()
    {
        return !field.isDisabled();
    }
}
