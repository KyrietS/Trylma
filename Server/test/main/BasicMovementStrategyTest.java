package main;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicMovementStrategyTest
{
    @Test
    void verifyMoveTest()
    {
        Board b = createDummyBoard();
        BasicMovementStrategy ms = new BasicMovementStrategy();

        //niepoprawny ruch na to samo pole
        assertEquals(0, ms.verifyMove(b, 1, 1, 1, 1));
        //niepoprawny ruch z pustego pola
        assertEquals(0, ms.verifyMove(b, 1, 2, 2, 1));
        //niepoprawny ruch na niegrywalne pole
        assertEquals(0, ms.verifyMove(b, 2, 2, 3, 2));
        //niepoprawny krótki ruch na zajęte pole
        assertEquals(0, ms.verifyMove(b, 2, 2, 2, 3));
        //niepoprawny ruch na zbyt odległe pole
        assertEquals(0, ms.verifyMove(b, 2, 2, 4, 4));

        //poprawny krótki ruch w dół
        assertEquals(1, ms.verifyMove(b, 1, 1, 1, 2));
        //poprawny krótki ruch w prawo
        assertEquals(1, ms.verifyMove(b, 1, 1, 2, 1));

        //poprawny przeskakujący ruch w lewo
        assertEquals(2, ms.verifyMove(b, 5, 5, 3, 5));
        //poprawny przeskakujący ruch w prawo
        assertEquals(2, ms.verifyMove(b, 5, 5, 7, 5));

        //poprawny przeskakujący ruch w lewo, w górę (rząd nieparzysty)
        assertEquals(2, ms.verifyMove(b, 5, 5, 4, 3));
        //poprawny przeskakujący ruch w prawo, w górę (rząd nieparzysty)
        assertEquals(2, ms.verifyMove(b, 5, 5, 6, 3));
        //poprawny przeskakujący ruch w lewo w dół (rząd nieparzysty)
        assertEquals(2, ms.verifyMove(b, 5, 5, 4, 7));
        //poprawny przeskakujący ruch w prawo w dół (rząd nieparzysty)
        assertEquals(2, ms.verifyMove(b, 5, 5, 6, 7));

        //poprawny przeskakujący ruch w lewo, w górę (rząd parzysty)
        assertEquals(2, ms.verifyMove(b, 8, 8, 7, 6));
        //poprawny przeskakujący ruch w prawo, w górę (rząd parzysty)
        assertEquals(2, ms.verifyMove(b, 8, 8, 9, 6));
        //poprawny przeskakujący ruch w lewo w dół (rząd parzysty)
        assertEquals(2, ms.verifyMove(b, 8, 8, 7, 10));
        //poprawny przeskakujący ruch w prawo w dół (rząd parzysty)
        assertEquals(2, ms.verifyMove(b, 8, 8, 9, 10));

        //niepoprawnych przeskakujący ruch - brak pionka miedzy polami
        assertEquals(0, ms.verifyMove(b, 2, 2, 1, 1));
        assertEquals(1, ms.verifyMove(b, 1, 1, 1, 2));


    }

    private Board createDummyBoard()
    {
        Board board = new ClassicBoard();

        //Dla przejrzystości - pionki które będą się poruszać są koloru BLUE, pionki blokujące ruch - RED , pionki do przeskakiwania - GREEN


        //pola do krótkich ruchów
        board.setField(1, 1, new Field("BLUE", null, null, true));
        board.setField(2, 1, new Field("none", null, null, true));
        board.setField(1, 2, new Field("none", null, null, true));

        //pola do blędnych krótkich ruchów
        board.setField(2, 2, new Field("BLUE", null, null, true));
        board.setField(2, 3, new Field("RED", null, null, true));
        board.setField(3, 2, new Field(false));
        board.setField(4, 4, new Field("none", null, null, true));

        //pola do poprawnych ruchów przeskakujących:

        //pola w jednym rzędzie:
        //pole z którego wykonujemy ruch
        board.setField(5, 5, new Field("BLUE", null, null, true));
        //pola w jednym rzędzie z pionkami pomiędzy:
        board.setField(7, 5, new Field("none", null, null, true));
        board.setField(6, 5, new Field("GREEN", null, null, true));
        board.setField(3, 5, new Field("none", null, null, true));
        board.setField(4, 5, new Field("GREEN", null, null, true));

        //pola w oddalonych rzędach - ruch z rzędu nieparzystego
        //pole z którego wykonujemy ruch: (5,5)
        board.setField(4, 3, new Field("none", null, null, true));
        board.setField(4, 4, new Field("GREEN", null, null, true));
        board.setField(6, 3, new Field("none", null, null, true));
        board.setField(5, 4, new Field("GREEN", null, null, true));
        board.setField(4, 7, new Field("none", null, null, true));
        board.setField(4, 6, new Field("GREEN", null, null, true));
        board.setField(6, 7, new Field("none", null, null, true));
        board.setField(5, 6, new Field("GREEN", null, null, true));

        //pola w oddalonych rzędach - ruch z rzędu parzystego:
        //pole z którego wykonujemy ruch
        board.setField(8, 8, new Field("BLUE", null, null, true));
        board.setField(7, 6, new Field("none", null, null, true));
        board.setField(8, 7, new Field("GREEN", null, null, true));
        board.setField(9, 6, new Field("none", null, null, true));
        board.setField(9, 7, new Field("GREEN", null, null, true));
        board.setField(7, 10, new Field("none", null, null, true));
        board.setField(8, 9, new Field("GREEN", null, null, true));
        board.setField(9, 10, new Field("none", null, null, true));
        board.setField(9, 9, new Field("GREEN", null, null, true));

        return board;
    }
}