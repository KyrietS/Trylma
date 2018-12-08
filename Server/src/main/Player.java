package main;

import shared.PlayerColor;

public abstract class Player
{
    PlayerColor color;
    private boolean finished;

    Player()
    {

    }

    Player( PlayerColor color)
    {
        this.color = color;
    }

    abstract void sendCommand(String command);

    abstract String readResponse() throws Exception;

    public PlayerColor getColor()
    {
        return color;
    }

    public boolean isFinished()
    {
        return finished;
    }
}
