package main;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    ServerSocket serverSocket;
    List<Player> players;

    Server( int port ) throws Exception
    {
        System.out.println( "Uruchamianie serwera..." );
        try
        {
            serverSocket = new ServerSocket( port );
        }
        catch( Exception e )
        {
            throw new Exception( "Nie można uzyskać dostępu to portu " + port );
        }
    }

    void createMatch(int playersNum, int botsNum)
    {
        //TODO tymczasowo testowane jest połączenie z jednym klientem
        players = new ArrayList<>();
        try
        {
            System.out.println( "Oczekiwanie na klienta" );
            players.add( new RealPlayer( serverSocket.accept(), "R" ) );
        }
        catch (Exception e )
        {
            System.out.println("Wystąpił błąd przy próbie połączenia z klientem");
        }

        System.out.println("Połączono z klientem");
        System.out.println("Nasłuchiwanie klienta...");

        while( true )
        {
            try
            {
                String response = players.get( 0 ).readResponse();
                System.out.println( "Odebrano: " + response );
            }
            catch( Exception e )
            {
                System.out.println( "Utracono połączenie z klientem" );
                break;
            }
        }
    }

    private void startMatch()
    {
        //TODO implement
    }
}
