package board;

import javafx.scene.shape.Circle;
import org.junit.jupiter.api.Test;
import shared.PlayerColor;

import static org.junit.jupiter.api.Assertions.*;

class FieldCommonAdapterTest
{

    @Test
    void getCurrentColor()
    {
        Field field = new Field( 0, 0, new Circle() );
        FieldCommonAdapter adapter = new FieldCommonAdapter( field );

        assertEquals( PlayerColor.NONE, adapter.getCurrentColor() );

        field.setColor( PlayerColor.VIOLET );
        assertEquals( PlayerColor.VIOLET, adapter.getCurrentColor() );
    }

    @Test
    void isPlayable()
    {
        Circle circle = new Circle();
        circle.setDisable( true );
        Field field = new Field( 0, 0, circle );
        FieldCommonAdapter adapter = new FieldCommonAdapter( field );

        assertFalse( adapter.isPlayable() );

        circle.setDisable( false );
        assertTrue( adapter.isPlayable() );
    }

}