package main;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationManager
{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    CommunicationManager(Socket s)
    {
        //TODO implement
        socket = s;
    }

    String readLine()
    {
        //TODO implement
        return "readline";
    }

    String writeLine()
    {
        //TODO implement
        return "writeline";
    }
}
