package board;

import shared.IField;
import shared.PlayerColor;

public class FieldCommonAdapter implements IField
{
    private Field field;

    FieldCommonAdapter( Field adaptedField )
    {
        this.field = adaptedField;
    }

    @Override
    public PlayerColor getCurrentColor()
    {
        if( field.getColor() == null || field.getColor() == PlayerColor.NONE )
            return PlayerColor.NONE;
        else
            return field.getColor();
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
        return !field.isDisabled();
    }
}
