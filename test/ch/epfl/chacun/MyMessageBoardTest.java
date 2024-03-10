package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyMessageBoardTest {

    @Test
    void pointsCorrectlyReturned(){
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)), new MessageBoard.Message("test", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 1))));

        assertEquals("{GREEN=3, RED=5, BLUE=2}", messageBoard.points());
    }

    @Test
    void pointsCorrectlyReturnedWithNoMessages(){
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of());

        assertEquals("{}", messageBoard.points().toString());
    }

    @Test
    void scoredForestWithUnoccupiedForest() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)), new MessageBoard.Message("test", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 1))));

        assertEquals(messageBoard, messageBoard.withScoredForest(new Area<Zone.Forest>(Set.of(), List.of(), 0)));
    }

    @Test
    void scoredForestWithOccupiedForest() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)), new MessageBoard.Message("test2", 5, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(2, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)),
                new MessageBoard.Message("test2", 5, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(2, 4)),
                new MessageBoard.Message(textMaker.playersScoredForest(Set.of(PlayerColor.RED, PlayerColor.GREEN), 7, 1, 2), 7, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(81, 42)))),
                messageBoard.withScoredForest(new Area<Zone.Forest>(Set.of(new Zone.Forest(815, Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(426, Zone.Forest.Kind.PLAIN)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0)));
    }

    @Test
    void closedForestWithMenhirCorrectlySendsMessage() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1))));

        assertEquals(messageBoard, messageBoard.withClosedForestWithMenhir(PlayerColor.RED, new Area<Zone.Forest>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.PLAIN)), List.of(), 0)));
    }


    @Test
    void addMessageToMessageList() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1))));
        MessageBoard.Message message = new MessageBoard.Message("test", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 1));

        List<MessageBoard.Message> list = List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)), new MessageBoard.Message("test", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 1)));

        assertEquals(list, messageBoard.messagesWithNewMessage(message));
    }


}
