package main;

import shared.IField;
import shared.PlayerColor;

class Field implements IField
{
    private PlayerColor currentColor;
    private PlayerColor nativeColor;
    private PlayerColor targetColor;
    private boolean playable;

    Field(boolean playable)
    {

    }

    Field(PlayerColor currentColor, PlayerColor nativeColor, PlayerColor targetColor, boolean playable)
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

    void setNativeColor(PlayerColor nativeColor)
    {
        this.nativeColor = nativeColor;
    }

    @Override
    public PlayerColor getTargetColor()
    {
        return targetColor;
    }

    void setTargetColor(PlayerColor targetColor)
    {
        this.targetColor = targetColor;
    }

    @Override
    public boolean isPlayable()
    {
        return playable;
    }

    void setPlayable(boolean playable)
    {
        this.playable = playable;
    }
}
