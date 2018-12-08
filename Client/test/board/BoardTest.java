package board;

import javafx.scene.shape.Circle;
import org.junit.jupiter.api.Test;
import shared.PlayerColor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest
{
    @Test
    void isEmpty_notExistingField()
    {
        Board board = createDummyBoard();

        assertTrue( board.isEmpty( -1, -1 ) );
    }

    @Test
    void isEmpty()
    {
        Board board = createDummyBoard();

        assertTrue( board.isEmpty( 0, 0 ) );

        board.addPiece( 0, 1, PlayerColor.RED );
        assertFalse( board.isEmpty( 0, 1 ) );
    }

    @Test
    void getColor_notExistingField()
    {
        Board board = createDummyBoard();

        assertEquals( PlayerColor.NONE, board.getColor( -1, -1 ) );
    }

    @Test
    void getColor()
    {
        Board board = createDummyBoard();

        assertEquals( PlayerColor.NONE, board.getColor( 1, 1 ) );

        board.addPiece( 1, 1, PlayerColor.RED );
        assertEquals( PlayerColor.RED, board.getColor( 1, 1 ) );
    }

    @Test
    void select_notExistingField()
    {
        Board board = createDummyBoard();

        // Zaznaczenie nieistniejącego pola powinno nie rzucić wyjątku
        board.select( -1, -1 );
    }

    @Test
    void mark_notExistingField()
    {
        Board board = createDummyBoard();

        // Podświetlenie nieistniejącego pola powinno nie rzucić wyjątku
        board.mark( -1, -1 );
    }

    private Board createDummyBoard()
    {
        List<Field> fields = new ArrayList<>();

        fields.add( new Field( 0, 0, new Circle() ) );
        fields.add( new Field( 0, 1, new Circle() ) );
        fields.add( new Field( 0, 2, new Circle() ) );
        fields.add( new Field( 1, 0, new Circle() ) );
        fields.add( new Field( 1, 1, new Circle() ) );
        fields.add( new Field( 1, 2, new Circle() ) );

        Board board = new Board( fields );

        return board;
    }
}