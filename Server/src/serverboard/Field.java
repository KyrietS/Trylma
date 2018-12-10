package serverboard;

import shared.IField;
import shared.PlayerColor;

public class Field implements IField
{
    private PlayerColor currentColor;
    private PlayerColor nativeColor;
    private PlayerColor targetColor;
    private boolean playable;

    public Field(boolean playable)
    {

    }

    public Field(PlayerColor currentColor, PlayerColor nativeColor, PlayerColor targetColor, boolean playable)
    {
        this.currentColor = currentColor;
        this.nativeColor = nativeColor;
        this.targetColor = targetColor;
        this.playable = playable;
    }

    @Override
    public PlayerColor getCurrentColor()
    {
        return currentColor;
    }

    void setCurrentColor(PlayerColor currentColor)
    {
        this.currentColor = currentColor;
    }

    @Override
    public PlayerColor getNativeColor()
    {
        return nativeColor;
    }

    @Override
    public PlayerColor getTargetColor()
    {
        return targetColor;
    }

    @Override
    public boolean isPlayable()
    {
        return playable;
    }

}
