package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa obsługująca komunikację z serwerem.
 */
public class CommunicationManager
{
    private int port;           // domyślnie 4444
    private String host;        // domyślnie "localhost"
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public CommunicationManager( String host, int port ) throws Exception
    {
        this.host = host;
        this.port = port;

        try
        {
            socket = new Socket( host, port );
            out = new PrintWriter( socket.getOutputStream(), true );
            in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
        }
        catch( Exception e )
        {
            throw new Exception( "Nie można nawiązać połączenia z serwerem" );
        }
    }

    /**
     * Odebranie komunikatu od serwera
     * @return komunikat w odpowiednim formacie
     * @throws Exception zerwano połączenie
     */
    synchronized String readLine() throws Exception
    {
        return in.readLine();
    }

    /**
     * Wysłanie komunikatu do serwera
     * @param line komunikat w odpowiednim formacie
     */
    void writeLine( String line )
    {
        out.println( line );
    }
}
