package main;

import org.junit.jupiter.api.Test;
import shared.IField;

import static org.junit.jupiter.api.Assertions.*;

class ClassicBoardTest
{

    @Test
    void constructorTest()
    {
        ClassicBoard classicBoard = new ClassicBoard();
        IField field = classicBoard.getField(5, 5);
        assertFalse(field.isPlayable());
        assertNull(field.getCurrentColor());
        assertNull(field.getNativeColor());
        assertNull(field.getTargetColor());
    }

    @Test
    void addingToUnplayableFieldException()
    {

    }

    @Test
    void addPieceTest()
    {
        ClassicBoard classicBoard = new ClassicBoard();
        //początkowo pole ma currentColor = "none"
        classicBoard.setField(1, 1, new Field("none", "none", "none", true));
        try
        {
            //dodajemy pionek koloru [G]reen
            classicBoard.addPiece(1, 1, "G");
        } catch (UnplayableFieldException ex)
        {
            fail();
        }
        assertEquals(classicBoard.getField(1, 1).getCurrentColor(), "G");
    }

    @Test
    void removePieceTest()
    {
        ClassicBoard classicBoard = new ClassicBoard();
        //początkowo pole ma currentColor = "G"
        classicBoard.setField(1, 1, new Field("G", "none", "none", true));
        try
        {
            //usuwamy pionek
            classicBoard.removePiece(1, 1);
        } catch (UnplayableFieldException ex)
        {
            fail();
        }
        assertEquals(classicBoard.getField(1, 1).getCurrentColor(), "none");
    }
}