package main;

import player.RealPlayer;
import shared.PlayerColor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Server
{
    private ServerSocket serverSocket;
    private List<Socket> playerSockets;
    private Match match;

    Server( int port ) throws Exception
    {
        System.out.println("Uruchamianie serwera...");
        try
        {
            serverSocket = new ServerSocket(port);
            playerSockets = new ArrayList<>();
        } catch (Exception e)
        {
            throw new Exception("Nie można uzyskać dostępu to portu " + port);
        }
    }

    void startMatch( int numberOfRealPlayers, int numberOfBots ) throws Exception
    {
        int totalNumberOfPlayers = numberOfRealPlayers + numberOfBots;
        int numberOfAvailableColors = PlayerColor.values().length - 1;

        if( totalNumberOfPlayers < 1 || totalNumberOfPlayers > numberOfAvailableColors )
        {
            throw new RuntimeException( "Nieprawidłowe parametry dot. liczby graczy" );
        }
        else
        {
            createMatch( numberOfRealPlayers, numberOfBots );
            match.start();
        }

    }

    private void createMatch( int numberOfRealPlayers, int numberOfBots ) throws Exception
    {
        connectPlayers( numberOfRealPlayers );
        match = new Match( playerSockets, numberOfBots );
    }

    private void connectPlayers( int numberOfPlayersToConnect ) throws Exception
    {
        try
        {
            for( int i = 0; i < numberOfPlayersToConnect; i++ )
            {
                System.out.println( "Oczekiwanie na gracza nr " + i );
                playerSockets.add( serverSocket.accept() );
            }
        }
        catch( Exception e )
        {
            throw new Exception( "Wystąpił błąd przy próbie połączenia z klientem: " + e.getMessage() );
        }
    }
}
