package shared;

/**
 * Interfejs łączący implementację pól po stronie serwera i klienta
 */
public interface IField
{
    /** Zwraca kolor pionka na polu */
    PlayerColor getCurrentColor();

    /** Zwraca bazowy kolor pola (obramowanie pola) */
    PlayerColor getNativeColor();

    /** Zwraca kolor pionka, który musi się znaleźć na tym polu, aby spełnić warunek zwycięstwa. */
    PlayerColor getTargetColor();

    /** Informuje, czy pole uczestniczy w rozgrywce (jest częścią grywalnej planszy) */
    boolean isPlayable();
}
