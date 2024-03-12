package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1))));

        assertEquals(new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)),
                new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), 4, Set.of(), Set.of(42,53)))),
                messageBoard.withClosedForestWithMenhir(PlayerColor.RED, new Area<Zone.Forest>(Set.of(new Zone.Forest(421, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(532, Zone.Forest.Kind.PLAIN)), List.of(), 0)));
    }


    @Test
    void addMessageToMessageList() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1))));
        MessageBoard.Message message = new MessageBoard.Message("test", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 1));

        List<MessageBoard.Message> list = List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)), new MessageBoard.Message("test", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 1)));

        assertEquals(list, messageBoard.messagesWithNewMessage(message));
    }

    @Test
    void scoredRiverWithUnoccupiedRiver() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredRiver(new Area<Zone.River>(Set.of(), List.of(), 0)));
    }

    @Test
    void scoredRiverWithOccupiedRiver() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playersScoredRiver(Set.of(PlayerColor.RED, PlayerColor.GREEN), 5, 5, 2), 5, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(81, 42)))),
                messageBoard.withScoredRiver(new Area<Zone.River>(Set.of(new Zone.River(815, 3, null), new Zone.River(426, 2, null)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0)));
    }

    @Test
    void scoredHuntingTrapWithUnoccupiedHuntingTrap() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredHuntingTrap(PlayerColor.RED, new Area<Zone.Meadow>(Set.of(), List.of(), 0)));
    }


    @Test
    void scoredHuntingTrapWithOccupiedHuntingTrap() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playerScoredHuntingTrap(PlayerColor.RED, 2, Map.of(Animal.Kind.DEER, 0, Animal.Kind.TIGER, 0, Animal.Kind.AUROCHS, 1, Animal.Kind.MAMMOTH, 0)), 2, Set.of(PlayerColor.RED), Set.of(81, 42)))),
                messageBoard.withScoredHuntingTrap(PlayerColor.RED, new Area<Zone.Meadow>(Set.of(
                        new Zone.Meadow(815, List.of(new Animal(20, Animal.Kind.DEER),
                                                        new Animal(21, Animal.Kind.TIGER)), null),
                        new Zone.Meadow(426, List.of(new Animal(22, Animal.Kind.AUROCHS)), null)), List.of(PlayerColor.RED), 0)));
    }

    @Test
    void scoredLogboatTest1() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playerScoredLogboat(PlayerColor.RED, 2, 1), 2, Set.of(PlayerColor.RED), Set.of(34, 75)))),
                messageBoard.withScoredLogboat(PlayerColor.RED, new Area<Zone.Water>(Set.of(new Zone.River(341, 0, new Zone.Lake(754, 0, null))), List.of(), 0)));
    }
    @Test
    void scoredLogboatTest2() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                        new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                        new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                        new MessageBoard.Message(textMaker.playerScoredLogboat(PlayerColor.RED, 4, 2), 4, Set.of(PlayerColor.RED), Set.of(34, 76)))),
                messageBoard.withScoredLogboat(PlayerColor.RED, new Area<Zone.Water>(Set.of(new Zone.River(341, 0, new Zone.Lake(754, 0, null)), new Zone.Lake(764, 0, null)), List.of(), 0)));
    }

    @Test
    void scoredMeadowWithUnoccupiedMeadow() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredMeadow(new Area<Zone.Meadow>(Set.of(new Zone.Meadow(249, List.of(), null)), List.of(), 0), Set.of()));
    }

    @Test
    void scoredMeadowWithOccupiedMeadow() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playersScoredMeadow(Set.of(PlayerColor.RED, PlayerColor.GREEN), 3, Map.of(Animal.Kind.TIGER, 1, Animal.Kind.MAMMOTH, 0, Animal.Kind.DEER, 1, Animal.Kind.AUROCHS, 1)), 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(81, 42)))),
                messageBoard.withScoredMeadow(new Area<Zone.Meadow>(Set.of(new Zone.Meadow(815, List.of(new Animal(20, Animal.Kind.DEER),new Animal(21, Animal.Kind.TIGER)),null),
                        new Zone.Meadow(426, List.of(new Animal(22, Animal.Kind.AUROCHS)), null)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0), Set.of()));
    }

    @Test
    void scoredMeadowWithOccupiedMeadowButNoPoints() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredMeadow(new Area<Zone.Meadow>(Set.of(new Zone.Meadow(815, List.of(),null),
                        new Zone.Meadow(426, List.of(), null)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0), Set.of()));
    }

    @Test
    void scoredRiverSystemWithUnoccupiedRiverSystem() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredRiverSystem(new Area<Zone.Water>(Set.of(new Zone.River(548, 2, null)), List.of(), 0)));
    }

    @Test
    void scoredRiverSystemNullPoints() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredRiverSystem(new Area<Zone.Water>(Set.of(new Zone.River(548, 0, null)), List.of(PlayerColor.RED), 0)));
    }

    @Test
    void scoredRiverSystem() {
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playersScoredRiverSystem(Set.of(PlayerColor.RED, PlayerColor.GREEN), 5, 5), 5, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(54, 76)))),
                messageBoard.withScoredRiverSystem(new Area<Zone.Water>(Set.of(new Zone.River(548, 3, new Zone.Lake(754, 2, null)), new Zone.Lake(764, 0, null)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0)));
    }

}
