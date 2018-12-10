package shared;

/**
 * Interfejs łączący implementację plansz po stronie serwera i klienta
 */
public interface IBoard
{
    /**
     * Zwraca pole o podanych koordynatach
     */
    IField getField(int x, int y);
}
