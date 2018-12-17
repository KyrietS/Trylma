package player;

import movement.BasicMovementStrategy;
import movement.GameMaster;
import org.junit.jupiter.api.Test;
import serverboard.*;
import shared.Coord;
import shared.PlayerColor;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ClassicBotTest
{

    @Test
    void activeStrayMode()
    {
        ClassicBot bot = createDummyBot();
        assertFalse(bot.strayMode);
        bot.readResponse();
        bot.readResponse();
        assertTrue(bot.strayMode);
    }

    @Test
    void noPossibleMoves()
    {
        GameMaster gm = new GameMaster(new BasicMovementStrategy(), new ImpossibleBoardFactory());
        gm.initializeBoard(0);
        ClassicBot bot = new ClassicBot(PlayerColor.RED, gm);
        bot.sendCommand("YOU");
        assertEquals(0, bot.moves.size());
        assertEquals(bot.readResponse(), "SKIP");
    }

    @Test
    void evaluateTest()
    {
        ClassicBot bot = createDummyBot();
        bot.listMoves();
        bot.evaluateMoves();
        assertNotNull(bot.bestMove());
        assert (bot.bestMove().getValue() > 0);
    }

    private ClassicBot createDummyBot()
    {
        GameMaster gm = new GameMaster(new BasicMovementStrategy(), new ClassicBoardFactory());
        gm.initializeBoard(1);
        return new ClassicBot(PlayerColor.RED, gm);
    }


    class ImpossibleBoardFactory implements BoardFactory
    {
        ClassicBoard board = new ClassicBoard();

        @Override
        public Board createBoard(int numberOfPlayers)
        {
            board.setField(4, 8, new Field(PlayerColor.RED, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(3, 8, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(5, 8, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(4, 7, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(5, 7, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(4, 9, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(5, 9, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(2, 8, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(6, 8, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(3, 6, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(5, 6, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(3, 10, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));
            board.setField(5, 10, new Field(PlayerColor.BLUE, PlayerColor.NONE, PlayerColor.NONE, true));

            return board;
        }
    }
}