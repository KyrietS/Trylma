package main;

import shared.JumpStatusVerifyCondition;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    private ServerSocket serverSocket;
    private List<Player> players;
    private GameMaster gameMaster;
    private CommandBuilder commandBuilder;


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
        gameMaster = new GameMaster(new BasicMovementStrategy(), new ClassicBoardFactory());
        commandBuilder = new CommandBuilder();
        players = new ArrayList<>();
        try
        {
            System.out.println( "Oczekiwanie na klienta" );
            players.add( new RealPlayer( serverSocket.accept(), "R" ) );
        } catch (Exception e)
        {
            System.out.println("Wystąpił błąd przy próbie połączenia z klientem");
        }

        System.out.println("Połączono z klientem");
        System.out.println("Nasłuchiwanie klienta...");


    }

    private void startMatch()
    {
        //TODO implement
        commandBuilder.addCommand("START");
        commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
        sendToAll(commandBuilder.getCommand());

        JumpStatusVerifyCondition jumpStatus;
        int prevX, prevY;
        while( true )
        {
            try
            {
                String response = players.get( 0 ).readResponse();
                System.out.println( "Odebrano: " + response );
            } catch (Exception e)
            {
                System.out.println( "Utracono połączenie z klientem" );
                break;
            }
        }
    }

    private void sendToAll(String command)
    {
        for (Player temp : players)
        {
            temp.sendCommand(command);
        }
    }
}
