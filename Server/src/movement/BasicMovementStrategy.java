package movement;

import serverboard.Board;
import serverboard.UnplayableFieldException;
import shared.AdditionalVerifyCondition;
import shared.BasicMovementStrategyVerify;
import shared.IBoard;

/**
 * Opisuje podstawowy wariant zasad gry w chińskie warcaby
 * 1.Można wykonać ruch na dowolne wolne przyległe pole
 * 2.Można przeskakiwać dowolne pionki
 * 3.Po przeskoku można wykonać kolejny skok
 * 4.Nie ma zbijania
 */
public class BasicMovementStrategy implements MovementStrategy
{
    /**
     * Weryfikuje czy dany ruch z pola (x1,y1) na (x2,y2) dla danej planszy jest poprawny
     * Zwraca 0 jeżeli ruch był niepoprawny, 1 jeżeli był to poprawny ruch na przykegłe pole (ruch krótki), albo 2 gdy był to poprawny ruch przeskakujący
     * Do poprawnego działania wykorzystuje tablicę 2 dodatkowych warunków: JumpStatusVerifyCondition oraz PreviousPawnVerifyCondition
     * Funkcja musi przyjąć dokładnie te dwa warunki w podanej kolejności, w przeciwnym wypadku jej działanie będzie nieprzewidywalne i prawdopodobnie błędne
     * (Funckja oddelegowana do kasy BasicMovementStrategyVerify)
     */
    @Override
    public int verifyMove(IBoard board, int x1, int y1, int x2, int y2, AdditionalVerifyCondition[] additionalVerifyConditions)
    {
        return BasicMovementStrategyVerify.verifyMove(board, x1, y1, x2, y2, additionalVerifyConditions);
    }

    /**
     * Wykonuje ruch pionkiem z pola (x1,y1) na (x2,y2) na danej planszy. Zwraca planszę po wykonaniu ruchu
     * Funkcja powinna być wykonana po weryfikacji poprawności ruchu
     */
    @Override
    public Board makeMove(Board board, int x1, int y1, int x2, int y2)
    {
        try
        {
            board.addPiece(x2, y2, board.getColor(x1, y1));
            board.removePiece(x1, y1);
            return board;
        } catch (UnplayableFieldException ufexc)
        {
            return board;
        }

    }
}
