package serverboard;

import serverboard.Board;

public interface BoardFactory
{
    Board createBoard(int numberOfPlayers);
}
