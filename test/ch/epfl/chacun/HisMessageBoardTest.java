package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HisMessageBoardTest {


    private final HisClassToTestMessageBoardTest myClassToTestMessageBoardTest = new HisClassToTestMessageBoardTest();
    private final Set<PlayerColor> scorers1 = Set.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN);
    private final Set<PlayerColor> scorers2 = Set.of(PlayerColor.RED, PlayerColor.BLUE);
    private final Set<PlayerColor> scorers3 = Set.of(PlayerColor.RED);
    private final Set<Integer> tileIds = Set.of(30, 54, 66, 90, 31, 55, 67);
    private final List<MessageBoard.Message> messages1 = new ArrayList<>(List.of(
                    new MessageBoard.Message(
                            myClassToTestMessageBoardTest.playerName(PlayerColor.RED), 2, scorers1, tileIds
                    ),
                    new MessageBoard.Message(
                            myClassToTestMessageBoardTest.playerName(PlayerColor.BLUE), 4, scorers2, tileIds
                    ),
                    new MessageBoard.Message(
                            myClassToTestMessageBoardTest.playerName(PlayerColor.GREEN), 7, scorers3, tileIds
                    ),
                    new MessageBoard.Message(
                            myClassToTestMessageBoardTest.playerName(PlayerColor.RED), 10, scorers2, tileIds
                    ),
                    new MessageBoard.Message(
                            myClassToTestMessageBoardTest.playerName(PlayerColor.RED), 3, scorers3, tileIds
                    )
    ));
    private final Set<PlayerColor> scorers4 = Set.of(PlayerColor.PURPLE, PlayerColor.BLUE, PlayerColor.YELLOW);
    private final Set<PlayerColor> scorers5 = Set.of(PlayerColor.PURPLE, PlayerColor.BLUE);
    private final List<MessageBoard.Message> messages2 = new ArrayList<>(List.of(
            new MessageBoard.Message(
                    myClassToTestMessageBoardTest.playerName(PlayerColor.RED), 2, scorers4, tileIds
            ),
            new MessageBoard.Message(
                    myClassToTestMessageBoardTest.playerName(PlayerColor.BLUE), 4, scorers5, tileIds
            ),
            new MessageBoard.Message(
                    myClassToTestMessageBoardTest.playerName(PlayerColor.GREEN), 7, scorers3, tileIds
            )
    ));
    private final MessageBoard messageBoard1 = new MessageBoard(myClassToTestMessageBoardTest, messages1);
    private final MessageBoard messageBoard2 = new MessageBoard(myClassToTestMessageBoardTest, messages2);

    // ----------------- Forests ----------------- //
    private final Area<Zone.Forest> unoccupiedForest = new Area<>(Set.of(
            new Zone.Forest(0, Zone.Forest.Kind.PLAIN),
            new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)
            ), List.of(), 0);
    private final Area<Zone.Forest> occupiedForestByOnePlayerColor = new Area<>(Set.of(
            new Zone.Forest(50, Zone.Forest.Kind.PLAIN),
            new Zone.Forest(27, Zone.Forest.Kind.WITH_MENHIR)
    ), List.of(PlayerColor.RED), 0);
    private final Area<Zone.Forest> occupiedForestByTwoPlayerColors = new Area<>(Set.of(
            new Zone.Forest(20, Zone.Forest.Kind.PLAIN),
            new Zone.Forest(34, Zone.Forest.Kind.WITH_MENHIR)
    ), List.of(PlayerColor.GREEN, PlayerColor.BLUE), 0);
    // ----------------- Forests ----------------- //

    // ----------------- Rivers ----------------- //
    private final Area<Zone.River> unoccupiedRiver = new Area<>(Set.of(
            new Zone.River(90, 0, null),
            new Zone.River(50, 40, new Zone.Lake(
                    80, 0, Zone.SpecialPower.LOGBOAT
            ))
    ), List.of(), 0);
    private final Area<Zone.River> occupiedRiverByOnePlayerColor = new Area<>(Set.of(
            new Zone.River(10, 0, null),
            new Zone.River(30, 50, new Zone.Lake(
                    20, 0, Zone.SpecialPower.SHAMAN
            ))
    ), List.of(PlayerColor.RED), 0);
    private final Area<Zone.River> occupiedRiverByTwoPlayerColors = new Area<>(Set.of(
            new Zone.River(40, 20, null),
            new Zone.River(50, 10, new Zone.Lake(
                    50, 0, Zone.SpecialPower.HUNTING_TRAP
            ))
    ), List.of(PlayerColor.RED, PlayerColor.BLUE), 0);
    // ----------------- Rivers ----------------- //

    // ----------- Forests with Menhir ----------- //
    private final Area<Zone.Forest> forestWithMenhirWithOneOccupant = new Area<>(Set.of(
            new Zone.Forest(21, Zone.Forest.Kind.WITH_MENHIR),
            new Zone.Forest(32, Zone.Forest.Kind.WITH_MENHIR),
            new Zone.Forest(43, Zone.Forest.Kind.WITH_MENHIR),
            new Zone.Forest(54, Zone.Forest.Kind.WITH_MENHIR)
    ), List.of(PlayerColor.RED), 4);
    private final Area<Zone.Forest> forestWithMenhirWithTwoOccupants = new Area<>(Set.of(
            new Zone.Forest(21, Zone.Forest.Kind.WITH_MENHIR),
            new Zone.Forest(32, Zone.Forest.Kind.WITH_MENHIR),
            new Zone.Forest(43, Zone.Forest.Kind.WITH_MENHIR),
            new Zone.Forest(54, Zone.Forest.Kind.WITH_MENHIR)
    ), List.of(PlayerColor.PURPLE, PlayerColor.BLUE), 4);
    // ------------- Forests with Menhir ------------- //

    // ----------- Meadow with Hunting Trap ----------- //
    private final Area<Zone.Meadow> meadowWithHuntingTrapOnlyWithOneOccupantWithoutAnimals = new Area<>(Set.of(
            new Zone.Meadow(21, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP),
            new Zone.Meadow(32, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP),
            new Zone.Meadow(43, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP),
            new Zone.Meadow(54, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP)
    ), List.of(PlayerColor.RED), 4);

    private final Area<Zone.Meadow> meadowWithMixedHuntingTrapWithOneOccupantWithoutAnimals = new Area<>(Set.of(
            new Zone.Meadow(21, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP),
            new Zone.Meadow(32, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP),
            new Zone.Meadow(43, new ArrayList<>(), null),
            new Zone.Meadow(54, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP)
    ), List.of(PlayerColor.PURPLE, PlayerColor.BLUE), 4);
    private final Area<Zone.Meadow> meadowWithMixedHuntingTrapOnlyWithTwoOccupantsWithoutAnimals = new Area<>(Set.of(
            new Zone.Meadow(21, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP),
            new Zone.Meadow(32, new ArrayList<>(), null),
            new Zone.Meadow(43, new ArrayList<>(), null),
            new Zone.Meadow(54, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP)
    ), List.of(PlayerColor.PURPLE, PlayerColor.BLUE), 4);
    private final Area<Zone.Meadow> meadowWithHuntingTrapWithAnimals = new Area<>(Set.of(
            new Zone.Meadow(21, new ArrayList<>(
                    List.of(
                            new Animal(21, Animal.Kind.AUROCHS),
                            new Animal(32, Animal.Kind.MAMMOTH)
                    )
            ), Zone.SpecialPower.HUNTING_TRAP),
            new Zone.Meadow(32, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP),
            new Zone.Meadow(43, new ArrayList<>(
                    List.of(
                            new Animal(24, Animal.Kind.AUROCHS),
                            new Animal(39, Animal.Kind.AUROCHS),
                            new Animal(40, Animal.Kind.DEER)
                    )
            ), null),
            new Zone.Meadow(54, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP)
    ), List.of(PlayerColor.PURPLE, PlayerColor.YELLOW), 4);
    // ------------- Meadow with Hunting Trap ------------- //

    // ----------- River Systems  ----------- //
    private final Area<Zone.Water> riverSystemWithNoOccupant = new Area<>(Set.of(
            new Zone.River(21, 0, null),
            new Zone.River(32, 50, new Zone.Lake(
                    43, 0, Zone.SpecialPower.LOGBOAT
            )),
            new Zone.River(54, 10, new Zone.Lake(
                    65, 0, Zone.SpecialPower.LOGBOAT
            )),
            new Zone.Lake(76, 0, null)
    ), List.of(), 3);
    private final Area<Zone.Water> riverSystemWithOneOccupantWithFish = new Area<>(Set.of(
            new Zone.River(21, 0, null),
            new Zone.Lake(32, 50, Zone.SpecialPower.LOGBOAT),
            new Zone.River(54, 10, new Zone.Lake(
                    65, 0, Zone.SpecialPower.LOGBOAT
            ))
    ), List.of(PlayerColor.RED), 3);
    private final Area<Zone.Water> riverSystemWithTwoOccupantsWithFish = new Area<>(Set.of(
            new Zone.River(21, 0, null),
            new Zone.River(32, 50, new Zone.Lake(
                    43, 0, Zone.SpecialPower.LOGBOAT
            )),
            new Zone.River(54, 30, new Zone.Lake(
                    65, 0, Zone.SpecialPower.LOGBOAT
            )),
            new Zone.Lake(76, 0, null)
    ), List.of(PlayerColor.PURPLE, PlayerColor.BLUE), 3);
    private final Area<Zone.Water> riverSystemWithTwoOccupantsWithoutFish = new Area<>(Set.of(
            new Zone.River(21, 0, null),
            new Zone.River(32, 0, new Zone.Lake(
                    43, 0, null
            )),
            new Zone.River(54, 0, new Zone.Lake(
                    65, 0, null
            )),
            new Zone.Lake(76, 0, null)
    ), List.of(PlayerColor.PURPLE, PlayerColor.BLUE), 3);
    private final Area<Zone.Water> riverSystemWithoutLogBoatWithoutOccupant = new Area<>(Set.of(
            new Zone.River(21, 0, null),
            new Zone.River(32, 0, new Zone.Lake(
                    43, 0, null
            )),
            new Zone.River(54, 0, new Zone.Lake(
                    65, 0, null
            )),
            new Zone.Lake(76, 0, null)
    ), List.of(), 3);
    // ------------- River Systems  ------------- //

    // ----------- Animals  ----------- //
    private final Set<Animal> animals1 = Set.of(
            new Animal(21, Animal.Kind.AUROCHS),
            new Animal(32, Animal.Kind.MAMMOTH),
            new Animal(24, Animal.Kind.AUROCHS),
            new Animal(39, Animal.Kind.AUROCHS),
            new Animal(40, Animal.Kind.DEER)
    );
    private final Set<Animal> animals2 = Set.of(
            new Animal(21, Animal.Kind.AUROCHS),
            new Animal(32, Animal.Kind.MAMMOTH)
    );
    private final Set<Animal> animals3 = Set.of(
            new Animal(76, Animal.Kind.MAMMOTH),
            new Animal(200, Animal.Kind.AUROCHS)
    );
    // ------------- Animals  ------------- //

    @Test
    void pointsIsCorrectlyDefined() {
        Map<PlayerColor, Integer> points1 = Map.of(
                PlayerColor.RED, 26,
                PlayerColor.BLUE, 16,
                PlayerColor.GREEN, 2
        );
        assertEquals(points1, messageBoard1.points());

        Map<PlayerColor, Integer> points2 = Map.of(
                PlayerColor.YELLOW, 2,
                PlayerColor.BLUE, 6,
                PlayerColor.PURPLE, 6,
                PlayerColor.RED, 7
        );
        assertEquals(points2, messageBoard2.points());
    }

    @Test
    void withScoredForestIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard1.withScoredForest(unoccupiedForest);
        assertEquals(messageBoard, messageBoard1);

        messageBoard = messageBoard2.withScoredForest(unoccupiedForest);
        assertEquals(messageBoard, messageBoard2);

        messageBoard = messageBoard1.withScoredForest(occupiedForestByOnePlayerColor);
        assertNotEquals(messageBoard, messageBoard1);
        assertEquals(messageBoard.messages().size() , messageBoard1.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        messageBoard = messageBoard2.withScoredForest(occupiedForestByTwoPlayerColors);
        assertNotEquals(messageBoard, messageBoard2);
        assertEquals(messageBoard.messages().size() , messageBoard2.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }
    }

    @Test
    void withClosedForestWithMenhirIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard1.withClosedForestWithMenhir(PlayerColor.RED, forestWithMenhirWithOneOccupant);
        assertNotEquals(messageBoard, messageBoard1);
        assertEquals(messageBoard.messages().size() , messageBoard1.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        messageBoard = messageBoard2.withClosedForestWithMenhir(PlayerColor.BLUE, forestWithMenhirWithTwoOccupants);
        assertNotEquals(messageBoard, messageBoard2);
        assertEquals(messageBoard.messages().size() , messageBoard2.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        messageBoard = messageBoard2.withClosedForestWithMenhir(PlayerColor.PURPLE, forestWithMenhirWithTwoOccupants);
        assertNotEquals(messageBoard, messageBoard2);
        assertEquals(messageBoard.messages().size() , messageBoard2.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }
    }

    @Test
    void withScoredRiverIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard1.withScoredRiver(unoccupiedRiver);
        assertEquals(messageBoard, messageBoard1);

        messageBoard = messageBoard2.withScoredRiver(unoccupiedRiver);
        assertEquals(messageBoard, messageBoard2);

        messageBoard = messageBoard1.withScoredRiver(occupiedRiverByOnePlayerColor);
        assertNotEquals(messageBoard, messageBoard1);
        assertEquals(messageBoard.messages().size() , messageBoard1.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        messageBoard = messageBoard2.withScoredRiver(occupiedRiverByTwoPlayerColors);
        assertNotEquals(messageBoard, messageBoard2);
        assertEquals(messageBoard.messages().size() , messageBoard2.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }
    }

    @Test
    void withScoredHuntingTrapIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard1.withScoredHuntingTrap(PlayerColor.RED, meadowWithHuntingTrapOnlyWithOneOccupantWithoutAnimals);
        assertEquals(messageBoard, messageBoard1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        messageBoard = messageBoard2.withScoredHuntingTrap(PlayerColor.PURPLE, meadowWithMixedHuntingTrapOnlyWithTwoOccupantsWithoutAnimals);
        assertEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        messageBoard = messageBoard1.withScoredHuntingTrap(PlayerColor.PURPLE, meadowWithMixedHuntingTrapOnlyWithTwoOccupantsWithoutAnimals);
        assertEquals(messageBoard, messageBoard1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        messageBoard = messageBoard2.withScoredHuntingTrap(PlayerColor.BLUE, meadowWithMixedHuntingTrapOnlyWithTwoOccupantsWithoutAnimals);
        assertEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        messageBoard = messageBoard1.withScoredHuntingTrap(PlayerColor.PURPLE, meadowWithHuntingTrapWithAnimals);
        assertNotEquals(messageBoard, messageBoard1);
        assertEquals(messageBoard.messages().size() , messageBoard1.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        assertEquals(10, messageBoard.messages().getLast().points());


        messageBoard = messageBoard2.withScoredHuntingTrap(PlayerColor.YELLOW, meadowWithHuntingTrapWithAnimals);
        assertNotEquals(messageBoard, messageBoard2);
        assertEquals(messageBoard.messages().size() , messageBoard2.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(10, messageBoard.messages().getLast().points());
    }

    @Test
    void withScoredLogboatIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard1.withScoredLogboat(PlayerColor.RED, riverSystemWithOneOccupantWithFish);
        assertNotEquals(messageBoard, messageBoard1);
        assertEquals(messageBoard.messages().size() , messageBoard1.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        assertEquals(4, messageBoard.messages().getLast().points());

        messageBoard = messageBoard2.withScoredLogboat(PlayerColor.PURPLE, riverSystemWithTwoOccupantsWithFish);
        assertNotEquals(messageBoard, messageBoard2);
        assertEquals(messageBoard.messages().size() , messageBoard2.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(6, messageBoard.messages().getLast().points());
    }

    @Test
    void withScoredMeadowIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard1.withScoredMeadow(meadowWithHuntingTrapOnlyWithOneOccupantWithoutAnimals, animals1);
        assertEquals(messageBoard, messageBoard1);

        messageBoard = messageBoard2.withScoredMeadow(meadowWithHuntingTrapWithAnimals, new HashSet<>());
        assertNotEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(10, messageBoard.messages().getLast().points());

        messageBoard = messageBoard2.withScoredMeadow(meadowWithHuntingTrapWithAnimals, animals1);
        assertEquals(messageBoard, messageBoard2);

        messageBoard = messageBoard2.withScoredMeadow(meadowWithHuntingTrapWithAnimals, animals2);
        assertNotEquals(messageBoard, messageBoard2);
        assertEquals(messageBoard.messages().size() , messageBoard2.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(5, messageBoard.messages().getLast().points());

        messageBoard = messageBoard2.withScoredMeadow(meadowWithHuntingTrapWithAnimals, animals3);
        assertNotEquals(messageBoard, messageBoard2);
        assertEquals(messageBoard.messages().size() , messageBoard2.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(10, messageBoard.messages().getLast().points());

    }
    @Test
    void withScoredRiverSystemIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard1.withScoredRiverSystem(riverSystemWithNoOccupant);
        assertEquals(messageBoard, messageBoard1);

        messageBoard = messageBoard1.withScoredRiverSystem(riverSystemWithOneOccupantWithFish);
        assertNotEquals(messageBoard, messageBoard1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        assertEquals(60, messageBoard.messages().getLast().points());

        messageBoard = messageBoard2.withScoredRiverSystem(riverSystemWithoutLogBoatWithoutOccupant);
        assertEquals(messageBoard, messageBoard2);

        messageBoard = messageBoard2.withScoredRiverSystem(riverSystemWithOneOccupantWithFish);
        assertNotEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(60, messageBoard.messages().getLast().points());

        messageBoard = messageBoard1.withScoredRiverSystem(riverSystemWithTwoOccupantsWithFish);
        assertNotEquals(messageBoard, messageBoard1);
        assertEquals(messageBoard.messages().size() , messageBoard1.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        assertEquals(80, messageBoard.messages().getLast().points());

        messageBoard = messageBoard2.withScoredRiverSystem(riverSystemWithTwoOccupantsWithFish);
        assertEquals(messageBoard.messages().size() , messageBoard2.messages().size() + 1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(80, messageBoard.messages().getLast().points());

        messageBoard = messageBoard2.withScoredRiverSystem(riverSystemWithTwoOccupantsWithoutFish);
        assertEquals(messageBoard, messageBoard2);
    }

    @Test
    void withScoredPitTrapIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard1.withScoredPitTrap(meadowWithHuntingTrapOnlyWithOneOccupantWithoutAnimals, animals1);
        assertEquals(messageBoard, messageBoard1);

        messageBoard = messageBoard2.withScoredPitTrap(meadowWithHuntingTrapWithAnimals, new HashSet<>());
        assertNotEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(10, messageBoard.messages().getLast().points());

        messageBoard = messageBoard2.withScoredPitTrap(meadowWithHuntingTrapOnlyWithOneOccupantWithoutAnimals, animals1);
        assertEquals(messageBoard, messageBoard2);

        messageBoard = messageBoard2.withScoredPitTrap(meadowWithHuntingTrapWithAnimals, animals2);
        assertNotEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(5, messageBoard.messages().getLast().points());

        messageBoard = messageBoard2.withScoredPitTrap(meadowWithHuntingTrapWithAnimals, animals3);
        assertNotEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(10, messageBoard.messages().getLast().points());
    }

    @Test
    void withScoredRaftIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard2.withScoredRaft(riverSystemWithoutLogBoatWithoutOccupant);
        assertEquals(messageBoard, messageBoard2);

        messageBoard = messageBoard1.withScoredRaft(riverSystemWithNoOccupant);
        assertEquals(messageBoard, messageBoard1);

        messageBoard = messageBoard2.withScoredRaft(riverSystemWithTwoOccupantsWithoutFish);
        assertNotEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(3, messageBoard.messages().getLast().points());
        assertEquals(Set.of(PlayerColor.PURPLE, PlayerColor.BLUE), messageBoard.messages().getLast().scorers());

        messageBoard = messageBoard1.withScoredRaft(riverSystemWithOneOccupantWithFish);
        assertNotEquals(messageBoard, messageBoard1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        assertEquals(2, messageBoard.messages().getLast().points());
        assertEquals(Set.of(PlayerColor.RED), messageBoard.messages().getLast().scorers());

        messageBoard = messageBoard2.withScoredRaft(riverSystemWithTwoOccupantsWithFish);
        assertNotEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(3, messageBoard.messages().getLast().points());
        assertEquals(Set.of(PlayerColor.PURPLE, PlayerColor.BLUE), messageBoard.messages().getLast().scorers());
    }

    @Test
    void withWinnersIsCorrectlyDefined() {
        MessageBoard messageBoard = messageBoard1.withWinners(Set.of(PlayerColor.RED, PlayerColor.BLUE), 10);
        assertNotEquals(messageBoard, messageBoard1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        assertEquals(0, messageBoard.messages().getLast().points());
        assertEquals(Set.of(PlayerColor.BLUE, PlayerColor.RED), messageBoard.messages().getLast().scorers());

        messageBoard = messageBoard2.withWinners(Set.of(PlayerColor.RED), 20);
        assertNotEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(0, messageBoard.messages().getLast().points());
        assertEquals(Set.of(PlayerColor.RED), messageBoard.messages().getLast().scorers());

        messageBoard = messageBoard1.withWinners(Set.of(), 10);
        assertNotEquals(messageBoard, messageBoard1);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard1.messages().get(i));
        }

        assertEquals(0, messageBoard.messages().getLast().points());
        assertEquals(Set.of(), messageBoard.messages().getLast().scorers());

        messageBoard = messageBoard2.withWinners(Set.of(), 0);
        assertNotEquals(messageBoard, messageBoard2);

        for (int i = 0 ; i < messageBoard.messages().size() - 1 ; i++) {
            assertEquals(messageBoard.messages().get(i), messageBoard2.messages().get(i));
        }

        assertEquals(0, messageBoard.messages().getLast().points());
        assertEquals(Set.of(), messageBoard.messages().getLast().scorers());
    }
}