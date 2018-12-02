package main;

import static java.lang.System.exit;

public class Main
{
    public static void main(String[] args)
    {
        Server server;
        try
        {
            server = new Server( 4444 );
        }
        catch(Exception e )
        {
            System.out.println( e.getMessage() );
            return;
        }

        server.createMatch( 1, 0 );
    }
}
