package main;

import java.net.Socket;

public class RealPlayer extends Player
{
    protected CommunicationManager communicationManager;

    RealPlayer(Socket socket, String color)
    {
        this.color = color;
        communicationManager = new CommunicationManager(socket);
    }

    @Override
    void sendCommand(String command)
    {
        //TODO implement
    }

    @Override
    String readResponse()
    {
        //TODO implement
        return null;
    }
}
