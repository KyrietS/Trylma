package main;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Controller
{
    /** Referencja do panelu, w którym znajdują się pola typu Circle */
    @FXML
    private Pane boardPane;


    private List<Field> fields = new ArrayList<>();
    private CommunicationManager communicationManager;

    /**
     * Funkcja uruchamiana przy starcie aplikacji
     */
    @FXML
    private void initialize()
    {
        loadFields();

        try
        {
            System.out.println("Podłączanie do serwera...");
            communicationManager = new CommunicationManager( "localhost", 4444 );
        }
        catch( Exception e )
        {
            System.out.println( "Błąd: " + e.getMessage() );
        }
        if( communicationManager != null )
            System.out.println("Połączono z serwerem");
    }

    /**
     * Wczytuje pola do tablicy fields
     */
    private void loadFields()
    {
        int x = 1;  // Współrzędna x pola (kolumna)
        int y = 1;  // Współrzędna y pola (wiersz)

        // Wczytywanie referencji i uzupełnianie nimi tablicy fields
        for( Node node : boardPane.getChildren() )
        {
            if( node instanceof Circle )
            {
                // Dodaj nowe pole
                fields.add( new Field( x, y, (Circle)node ) );
                // Ustaw handler na dodane pole
                node.setOnMouseClicked( this :: onFieldClick );

                // Zmiana współrzędnych
                if( x == 13 )
                {
                    x = 1;
                    y++;
                }
                else
                {
                    x++;
                }
            }
            else // Informacja dla programistów
            {
                throw new RuntimeException( "Jeden z elementów wewnątrz Pane jest typu innego nić Circle" );
            }
        }
    }

    /**
     * Obsługuje zdarzenie kliknięcia w pole (typu Circle)
     */
    private void onFieldClick( MouseEvent event )
    {
        // Ten handler jest przeznaczony tylko dla kliknięć w Circle
        if( !(event.getSource() instanceof Circle ) )
            return;

        // Pobierz referencję do Circle, w które kliknięto
        Circle circle = (Circle)event.getSource();

        // Pobierz Field, do którego przypisana jest referencja na kliknięte Circle
        Field field = getFieldByCircleReference( circle );

        // Funkcja powyżej powinna ZAWSZE odnaleźć pole. Zabezpieczenie to jest na wszelki wypadek.
        if( field == null )
            return;

        // TODO Obsługa kliknięć w pole. Tymczasowo jest zmieniane wypełnienie przy kliknięciu.

        System.out.println( "Kliknięto w pole: (" + field.getX() + ", " + field.getY() + ")" );
        if( communicationManager != null )
            communicationManager.writeLine( "Kliknięto w pole: (" + field.getX() + ", " + field.getY() + ")" );

        if( field.isSelected() )
            field.setSelected( false );
        else
            field.setSelected( true );
    }

    /**
     * Zwraca referencję do pola, któremu odpowiada dany circle.
     */
    private Field getFieldByCircleReference( Circle circle )
    {
        for( Field field : fields )
        {
            if( field.getCircle() == circle )
                return field;
        }

        return null;
    }
}
