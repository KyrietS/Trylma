package main;

public abstract class Player
{
    String color;
    private boolean finished;

    Player()
    {

    }

    Player(String color)
    {
        this.color = color;
    }

    abstract void sendCommand(String command);

    abstract String readResponse() throws Exception;

    public String getColor()
    {
        return color;
    }

    public boolean isFinished()
    {
        return finished;
    }
}
