package main;

class Field
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

    String getCurrentColor()
    {
        return currentColor;
    }

    void setCurrentColor(String currentColor)
    {
        this.currentColor = currentColor;
    }

    String getNativeColor()
    {
        return nativeColor;
    }

    void setNativeColor(String nativeColor)
    {
        this.nativeColor = nativeColor;
    }

    String getTargetColor()
    {
        return targetColor;
    }

    void setTargetColor(String targetColor)
    {
        this.targetColor = targetColor;
    }

    boolean isPlayable()
    {
        return playable;
    }

    void setPlayable(boolean playable)
    {
        this.playable = playable;
    }
}
