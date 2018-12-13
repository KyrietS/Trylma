package main;

import board.Board;
import board.Field;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller
{
    /** Referencja do panelu, w którym znajdują się pola typu Circle */
    @FXML
    private Pane boardPane;
    @FXML
    private Label infoBar;


    private List<Field> fields = new ArrayList<>();
    private CommunicationManager communicationManager;
    private Board board;
    private Player player;
    private boolean guiBlocked = false;

    /**
     * Funkcja uruchamiana przy starcie aplikacji
     */
    @FXML
    private void initialize()
    {
        loadFields();
        showAlert( "Połącz się z serwerem, aby zagrać" );
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
                throw new RuntimeException( "Jeden z elementów wewnątrz Pane jest typu innego niż Circle" );
            }
        }
    }

    /**
     * Obsługuje zdarzenie kliknięcia w pole (typu Circle)
     */
    private void onFieldClick( MouseEvent event )
    {
        if( guiBlocked )
            return;

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

        System.out.println( "Kliknięto w pole: (" + field.getX() + ", " + field.getY() + ")" );

        player.selectPiece( field.getX(), field.getY() );
    }

    @FXML
    private void onNewConnection()
    {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Nowe połączenie");
        dialog.setHeaderText("Nazwiązywanie nowego połączenia");

        // Set the icon (must be included in the project).
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Połącz", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField ipAddressField = new TextField();
        ipAddressField.setPromptText("adres IP hosta");
        ipAddressField.setText( "localhost" );
        TextField portField = new TextField();
        portField.setPromptText("port");
        portField.setText("4444");


        grid.add(new Label("IP:"), 0, 0);
        grid.add(ipAddressField, 1, 0);
        grid.add(new Label("Port:"), 0, 1);
        grid.add(portField, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(false);

        // Do some validation (using the Java 8 lambda syntax).
        ipAddressField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> ipAddressField.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(ipAddressField.getText(), portField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        String host;
        int port;
        if( result.isPresent() )
        {
            Pair<String, String> r = result.get();
            host = r.getKey();
            try
            {
                port = Integer.parseInt( r.getValue() );
            }
            catch( Exception e )
            {
                Alert alert = new Alert( Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Podano nieprawidłowy port");

                alert.showAndWait();
                return;
            }
            showAlert( "Podłączanie do serwera..." );
            Platform.runLater( () -> connect( host, port ) );
        }

    }

    @FXML
    private void onExit()
    {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie");
        alert.setHeaderText("Czy na pewno chcesz wyjść");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            Platform.exit();
        }
    }

    private void showError( String text )
    {
        Platform.runLater( () -> {
            infoBar.setStyle( "-fx-background-color: red; -fx-alignment: center" );
            infoBar.setTextFill( Color.WHITE );
            infoBar.setText( text );
        } );
    }

    private void showAlert( String text )
    {
        Platform.runLater( () -> {
            infoBar.setStyle("-fx-background-color: orange; -fx-alignment: center");
            infoBar.setTextFill( Color.WHITE );
            infoBar.setText( text );
        } );

    }

    private void showSuccess( String text )
    {
        Platform.runLater( () -> {
            infoBar.setStyle( "-fx-background-color: green; -fx-alignment: center" );
            infoBar.setTextFill( Color.WHITE );
            infoBar.setText( text );
        } );
    }

    private void connect( String host, int port )
    {
        try
        {
            communicationManager = new CommunicationManager( host, port );
        }
        catch( Exception e )
        {
            showError( e.getMessage() );
            return;
        }

        //System.out.println("Połączono z serwerem");
        //showSuccess( "Połączono z serwerem" );

        board = new Board( fields );

        try
        {
            player = new Player( communicationManager, board,
                    this :: blockGUI, this :: showSuccess, this :: showAlert, this :: showError );
        }
        catch( Exception e )
        {
            System.out.println("Problem z utworzeniem gracza: " + e.getMessage());
        }
    }

    /**
     * Zwraca referencję do pola, któremu odpowiada dany circle.
     */
    private Field getFieldByCircleReference( Circle circle )
    {
        for( Field field : fields )
        {
            if( field.circleEquals( circle ) )
                return field;
        }

        return null;
    }

    private void blockGUI( boolean state )
    {
        this.guiBlocked = state;
    }
}
