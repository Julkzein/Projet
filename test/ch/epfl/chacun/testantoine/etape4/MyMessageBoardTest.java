package ch.epfl.chacun.testantoine.etape4;

import ch.epfl.chacun.*;
import ch.epfl.chacun.testantoine.MyTextMaker;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyMessageBoardTest {

    @Test
    void pointsCorrectlyReturned() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)), new MessageBoard.Message("test", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 1))));
        Map<PlayerColor, Integer> map = new HashMap<>();
        map.put(PlayerColor.RED, 5);
        map.put(PlayerColor.BLUE, 2);
        map.put(PlayerColor.GREEN, 3);
        assertEquals(map, messageBoard.points());
    }

    @Test
    void pointsCorrectlyReturnedWithNoMessages() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of());

        assertEquals("{}", messageBoard.points().toString());
    }

    @Test
    void scoredForestWithUnoccupiedForest() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)), new MessageBoard.Message("test", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 1))));

        assertEquals(messageBoard, messageBoard.withScoredForest(new Area<Zone.Forest>(Set.of(), List.of(), 0)));
    }

    @Test
    void scoredForestWithOccupiedForest() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)), new MessageBoard.Message("test2", 5, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(2, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                        new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)),
                        new MessageBoard.Message("test2", 5, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(2, 4)),
                        new MessageBoard.Message(textMaker.playersScoredForest(new HashSet<>(List.of(PlayerColor.RED, PlayerColor.GREEN)), 7, 1, 2), 7, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(81, 42)))),
                messageBoard.withScoredForest(new Area<Zone.Forest>(Set.of(new Zone.Forest(815, Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(426, Zone.Forest.Kind.PLAIN)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0)));
    }

    @Test
    void closedForestWithMenhirCorrectlySendsMessage() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1))));

        assertEquals(new MessageBoard(textMaker, List.of(
                        new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)),
                        new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), 0, Set.of(), Set.of(42, 53)))),
                messageBoard.withClosedForestWithMenhir(PlayerColor.RED, new Area<Zone.Forest>(Set.of(new Zone.Forest(421, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(532, Zone.Forest.Kind.PLAIN)), List.of(), 0)));
    }

    @Test
    void scoredRiverWithUnoccupiedRiver() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredRiver(new Area<Zone.River>(Set.of(), List.of(), 0)));
    }

    @Test
    void scoredRiverWithOccupiedRiver() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        MessageBoard.Message message1 = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playersScoredRiver(new HashSet<>(List.of(PlayerColor.RED, PlayerColor.GREEN)),
                        7, 5, 2), 7, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(81, 42)))).messages().getLast();

        MessageBoard.Message message2 = messageBoard.withScoredRiver(new Area<>(Set.of(new Zone.River(815, 3, null), new Zone.River(426, 2, null)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0)).messages().getLast();

        assertEquals(message1.points(), message2.points());
        assertEquals(message1.scorers(), message2.scorers());
        assertEquals(message1.tileIds(), message2.tileIds());
    }

    @Test
    void scoredHuntingTrapWithUnoccupiedHuntingTrap() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredHuntingTrap(PlayerColor.RED, new Area<Zone.Meadow>(Set.of(), List.of(), 0)));
    }

    @Test
    void scoredHuntingTrapWithOccupiedHuntingTrap() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        MessageBoard.Message message1 = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playerScoredHuntingTrap(PlayerColor.RED, 3, Map.of(Animal.Kind.DEER,
                        1, Animal.Kind.AUROCHS, 1)), 3, Set.of(PlayerColor.RED), Set.of(81, 42)))).messages().getLast();

        MessageBoard.Message message2 = messageBoard.withScoredHuntingTrap(PlayerColor.RED, new Area<>(Set.of(
                new Zone.Meadow(815, List.of(new Animal(20, Animal.Kind.DEER),
                        new Animal(21, Animal.Kind.TIGER)), null),
                new Zone.Meadow(426, List.of(new Animal(22, Animal.Kind.AUROCHS)), null)),
                List.of(PlayerColor.RED), 0)).messages().getLast();

        // 3M + 2A + 1D = 1 + 2

        assertEquals(message1.points(), message2.points());
        assertEquals(message1.scorers(), message2.scorers());
        assertEquals(message1.tileIds(), message2.tileIds());
    }

    @Test
    void scoredLogboatIsCorrectlyDefined1() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                        new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                        new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                        new MessageBoard.Message(textMaker.playerScoredLogboat(PlayerColor.RED, 2, 1), 2, Set.of(PlayerColor.RED), Set.of(34)))),
                messageBoard.withScoredLogboat(PlayerColor.RED, new Area<Zone.Water>(Set.of(new Zone.River(341, 0, new Zone.Lake(754, 0, null))), List.of(), 0)));
    }

    @Test
    void scoredLogboatIsCorrectlyDefined2() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(new MessageBoard(textMaker, List.of(
                        new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                        new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                        new MessageBoard.Message(textMaker.playerScoredLogboat(PlayerColor.RED, 4, 2), 4, Set.of(PlayerColor.RED), Set.of(34, 76)))),
                messageBoard.withScoredLogboat(PlayerColor.RED, new Area<Zone.Water>(Set.of(new Zone.River(341, 0, new Zone.Lake(754, 0, null)), new Zone.Lake(764, 0, null)), List.of(), 0)));
    }

    @Test
    void scoredMeadowWithUnoccupiedMeadow() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredMeadow(new Area<Zone.Meadow>(Set.of(new Zone.Meadow(249, List.of(), null)), List.of(), 0), Set.of()));
    }

    @Test
    void scoredMeadowWithOccupiedMeadow() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        MessageBoard.Message message1 = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playersScoredMeadow(new HashSet<>(List.of(PlayerColor.RED, PlayerColor.GREEN)), 3, Map.of(Animal.Kind.TIGER, 2, Animal.Kind.DEER, 1, Animal.Kind.AUROCHS, 1)), 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(81, 42)))).messages().getLast();

        MessageBoard.Message message2 = messageBoard.withScoredMeadow(new Area<>(Set.of(new Zone.Meadow(815, List.of(new Animal(21, Animal.Kind.TIGER), new Animal(22, Animal.Kind.TIGER), new Animal(20, Animal.Kind.DEER)), null),
                new Zone.Meadow(426, List.of(new Animal(22, Animal.Kind.AUROCHS)), null)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0), Set.of()).messages().getLast();

       assertEquals(message1.points(), message2.points());
       assertEquals(message1.scorers(), message2.scorers());
       assertEquals(message1.tileIds(), message2.tileIds());
    }

    @Test
    void scoredMeadowWithOccupiedMeadowButNoPoints() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredMeadow(new Area<Zone.Meadow>(Set.of(new Zone.Meadow(815, List.of(), null),
                new Zone.Meadow(426, List.of(), null)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0), Set.of()));
    }

    @Test
    void scoredRiverSystemWithUnoccupiedRiverSystem() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredRiverSystem(new Area<Zone.Water>(Set.of(new Zone.River(548, 2, null)), List.of(), 0)));
    }

    @Test
    void scoredRiverSystemNullPoints() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredRiverSystem(new Area<Zone.Water>(Set.of(new Zone.River(548, 0, null)), List.of(PlayerColor.RED), 0)));
    }

    @Test
    void scoredRiverSystemIsCorrectlyDefined() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)), new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        MessageBoard.Message message1 = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playersScoredRiverSystem(new HashSet<>(List.of(PlayerColor.RED, PlayerColor.GREEN)),
                        3, 3), 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(54, 76)))).messages().getLast();


        MessageBoard.Message message2 = messageBoard.withScoredRiverSystem(new Area<>(Set.of(new Zone.River(548, 3,
                new Zone.Lake(754, 2, null)), new Zone.Lake(764, 0, null)),
                List.of(PlayerColor.RED, PlayerColor.GREEN), 0)).messages().getLast();

        assertEquals(message1.text(), message2.text());
        assertEquals(message1.points(), message2.points());
        assertEquals(message1.scorers(), message2.scorers());
        assertEquals(message1.tileIds(), message2.tileIds());
    }

    @Test
    void scoredPitTrapWithUnoccupiedMeadow() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredPitTrap(new Area<Zone.Meadow>(Set.of(), List.of(), 0), Set.of()));
    }

    @Test
    void scoredPitTrapWithOccupiedMeadowButNoPoints() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        assertEquals(messageBoard, messageBoard.withScoredPitTrap(new Area<Zone.Meadow>(Set.of(new Zone.Meadow(815, List.of(), null),
                new Zone.Meadow(426, List.of(), null)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0), Set.of()));
    }

    @Test
    void scoredPitTrapIsCorrectlyDefined() {
        MyTextMaker textMaker = new MyTextMaker();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4))));

        MessageBoard.Message message1 = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message("test", 2, new HashSet<>(List.of(PlayerColor.RED, PlayerColor.BLUE)), Set.of(1, 2)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 4)),
                new MessageBoard.Message(textMaker.playersScoredPitTrap(new HashSet<>(List.of(PlayerColor.RED, PlayerColor.GREEN)),
                        3, Map.of(Animal.Kind.TIGER, 1, Animal.Kind.DEER, 1, Animal.Kind.AUROCHS, 1)), 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(81, 42)))).messages().getLast();

        MessageBoard.Message message2 = messageBoard.withScoredPitTrap(new Area<>(Set.of(new Zone.Meadow(815, List.of(new Animal(20, Animal.Kind.DEER), new Animal(21, Animal.Kind.TIGER)), null),
                new Zone.Meadow(426, List.of(new Animal(22, Animal.Kind.AUROCHS)), null)), List.of(PlayerColor.RED, PlayerColor.GREEN), 0), Set.of()).messages().getLast();

        assertEquals(message1.points(), message2.points());
        assertEquals(message1.scorers(), message2.scorers());
        assertEquals(message1.tileIds(), message2.tileIds());

    }
}