package main;

import java.net.Socket;

public class RealPlayer extends Player
{
    protected CommunicationManager communicationManager;

    RealPlayer(Socket socket, String color) throws Exception
    {
        this.color = color;
        communicationManager = new CommunicationManager(socket);
    }

    @Override
    void sendCommand(String command)
    {
        communicationManager.writeLine( command );
    }

    @Override
    String readResponse() throws Exception
    {
        return communicationManager.readLine();
    }
}
