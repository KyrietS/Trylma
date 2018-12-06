package main;

import shared.IField;

class Field implements IField
{
    private String currentColor;
    private String nativeColor;
    private String targetColor;
    private boolean playable;

    Field(boolean playable)
    {

    }

    Field(String currentColor, String nativeColor, String targetColor, boolean playable)
    {
        this.currentColor = currentColor;
        this.nativeColor = nativeColor;
        this.targetColor = targetColor;
        this.playable = playable;
    }

    @Override
    public String getCurrentColor()
    {
        return currentColor;
    }

    void setCurrentColor(String currentColor)
    {
        this.currentColor = currentColor;
    }

    @Override
    public String getNativeColor()
    {
        return nativeColor;
    }

    void setNativeColor(String nativeColor)
    {
        this.nativeColor = nativeColor;
    }

    @Override
    public String getTargetColor()
    {
        return targetColor;
    }

    void setTargetColor(String targetColor)
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
