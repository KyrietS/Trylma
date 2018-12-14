package main;

public class Main
{
    public static void main(String[] args)
    {
        Server server;
        try
        {
            server = new Server( 4444 );
            server.startMatch( 1, 0 );
        }
        catch(Exception e )
        {
            System.out.println( e.getMessage() );
        }
    }
}
