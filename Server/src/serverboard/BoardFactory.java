package serverboard;

import serverboard.Board;

/**
 * Fabryka klasy Board
 */
public interface BoardFactory
{
    Board createBoard(int numberOfPlayers);
}
