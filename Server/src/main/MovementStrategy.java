package main;

public interface MovementStrategy
{
    /*
        sprawdza czy ruch jest poprawny
        zwraca 0 jeśli niepoprawny, 1 jeśli ruch pojedynczy, 2 jeśli ruch przesakujący (można wykonać kolejny ruch)
     */
    int verifyMove(Board board, int x1, int y1, int x2, int y2);

    Board makeMove(Board board, int x1, int y1, int x2, int y2);
}
