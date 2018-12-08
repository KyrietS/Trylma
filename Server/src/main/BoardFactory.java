package main;

import shared.PlayerColor;

public interface BoardFactory
{
    Board createBoard( PlayerColor[] colors);
}
