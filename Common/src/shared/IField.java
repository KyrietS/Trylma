package shared;

public interface IField
{
    PlayerColor getCurrentColor();

    PlayerColor getNativeColor();

    PlayerColor getTargetColor();

    boolean isPlayable();
}
