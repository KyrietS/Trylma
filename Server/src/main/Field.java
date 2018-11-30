package main;

public class Field
{
    private String currentColor;
    private String nativeColor;
    private String targetColor;
    private boolean playable;

    public Field()
    {
        //czy potrzebny konstruktor?
    }

    public String getCurrentColor()
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
