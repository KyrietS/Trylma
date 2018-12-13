package board;

import javafx.scene.shape.Circle;
import org.junit.jupiter.api.Test;
import shared.PlayerColor;

import static org.junit.jupiter.api.Assertions.*;

class FieldCommonAdapterTest
{

    @Test
    void getColor()
    {
        Field field = new Field( 0, 0, new Circle() );

        assertEquals( PlayerColor.NONE, field.getColor() );

        field.setColor( PlayerColor.VIOLET );
        assertEquals( PlayerColor.VIOLET, field.getColor() );
    }

}