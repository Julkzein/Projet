package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ch.epfl.chacun.Tiles.TILES;
import static org.junit.jupiter.api.Assertions.*;

public class MyGameStateTest {

    // Help for constructing the test cases
    private final Zone.Meadow meadow1 = new Zone.Meadow(374, List.of(), null);
    private final Zone.Meadow meadow2 = new Zone.Meadow(231, List.of(), null);
    private final Zone.Meadow meadow3 = new Zone.Meadow(768, List.of(new Animal(7680, Animal.Kind.DEER), new Animal(7681, Animal.Kind.DEER)), null);
    private final Zone.Meadow meadow4 = new Zone.Meadow(180, List.of(new Animal(1800, Animal.Kind.TIGER)), null);
    private final Zone.Meadow meadow5 = new Zone.Meadow(542, List.of(new Animal(5420, Animal.Kind.TIGER), new Animal(5421, Animal.Kind.DEER)), null);
    private final Zone.Meadow meadow6 = new Zone.Meadow(952, List.of(new Animal(9520, Animal.Kind.AUROCHS), new Animal(9521, Animal.Kind.TIGER)), null);
    private final Zone.Meadow meadowWithHuntingTrap = new Zone.Meadow(123, List.of(), Zone.SpecialPower.HUNTING_TRAP);
    private final Zone.Meadow meadowWithPitTrap = new Zone.Meadow(850, List.of(), Zone.SpecialPower.PIT_TRAP);
    private final Zone.Meadow meadowWithShaman = new Zone.Meadow(228, List.of(), Zone.SpecialPower.SHAMAN);
    private final Zone.Meadow meadowWithWildFire = new Zone.Meadow(607, List.of(), Zone.SpecialPower.WILD_FIRE);
    private final Zone.Forest forest1 = new Zone.Forest(468, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest2 = new Zone.Forest(212, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest3 = new Zone.Forest(329, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest4 = new Zone.Forest(791, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest5 = new Zone.Forest(907, Zone.Forest.Kind.WITH_MENHIR);
    private final Zone.Forest forest6 = new Zone.Forest(568, Zone.Forest.Kind.WITH_MUSHROOMS);
    private final Zone.Lake lake1 = new Zone.Lake(427, 2, null);
    private final Zone.Lake lake2 = new Zone.Lake(437, 1, null);
    private final Zone.Lake lakeWithLogboat = new Zone.Lake(317, 0, Zone.SpecialPower.LOGBOAT);
    private final Zone.Lake lakeWithRaft = new Zone.Lake(878, 2, Zone.SpecialPower.RAFT);
    private final Zone.River river1 = new Zone.River(172, 0, null);
    private final Zone.River river2 = new Zone.River(174, 0, null);
    private final Zone.River river3 = new Zone.River(564, 0, null);
    private final Zone.River river4 = new Zone.River(426, 0, null);
    private final Zone.River riverWithLake1 = new Zone.River(272, 0, lake1);
    private final Zone.River riverWithLake2 = new Zone.River(706, 0, lake2);
    private final Zone.River riverWithLakeWithLogboat1 = new Zone.River(716, 0, lakeWithLogboat);
    private final Zone.River riverWithLakeWithLogboat2 = new Zone.River(717, 0, lakeWithLogboat);
    private final Zone.River riverWithLakeWithRaft = new Zone.River(726, 0, lakeWithRaft);
    private final TileSide[] sides1 = new TileSide[]{
            new TileSide.Meadow(meadow1), new TileSide.Meadow(meadow1),
            new TileSide.Forest(forest1), new TileSide.Forest(forest1)};

    private final TileSide[] sides2 = new TileSide[]{
            new TileSide.Meadow(meadow3), new TileSide.River(meadow3, river1, meadow4),
            new TileSide.River(meadow4, river1, meadow3), new TileSide.Meadow(meadow3)};

    private final TileSide[] sides3 = new TileSide[]{
            new TileSide.River(meadow1, riverWithLake1, meadow2), new TileSide.Meadow(meadow2),
            new TileSide.Forest(forest1), new TileSide.Forest(forest1)};


    private final TileSide[] sides4 = new TileSide[]{
            new TileSide.Forest(forest5), new TileSide.Forest(forest5),
            new TileSide.Forest(forest5), new TileSide.Forest(forest5)};

    private final TileSide[] sides5 = new TileSide[]{
            new TileSide.Meadow(meadowWithHuntingTrap), new TileSide.Forest(forest3),
            new TileSide.Meadow(meadowWithHuntingTrap), new TileSide.Meadow(meadowWithHuntingTrap)};

    private final TileSide[] sides6 = new TileSide[]{
            new TileSide.River(meadow5, riverWithLakeWithLogboat1, meadow6), new TileSide.Meadow(meadow6),
            new TileSide.Meadow(meadow6), new TileSide.River(meadow6, riverWithLakeWithLogboat2, meadow5)};

    private final TileDecks tileDecks = new TileDecks(
            List.of(new Tile(341, Tile.Kind.START, sides3[0], sides3[1], sides3[2], sides3[3])),
            List.of(new Tile(342, Tile.Kind.NORMAL, sides1[0], sides1[1], sides1[2], sides1[3]),
                    new Tile(343, Tile.Kind.NORMAL, sides2[0], sides2[1], sides2[2], sides2[3]),
                    new Tile(344, Tile.Kind.NORMAL, sides4[0], sides4[1], sides4[2], sides4[3])),
            List.of(new Tile(345, Tile.Kind.MENHIR, sides5[0], sides5[1], sides5[2], sides5[3]),
                    new Tile(346, Tile.Kind.MENHIR, sides6[0], sides6[1], sides6[2], sides6[3])));

    private final BasicTextMaker BASIC_TEXT_MAKER = new BasicTextMaker();
    private final GameState INITIAL_GAME_STATE = GameState.initial(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks, BASIC_TEXT_MAKER);
    private final List<Tile> tileList = TILES;

    private TileDecks deckGenerator() {
        var startTile = List.of(tileList.get(56));
        var normalTiles = List.of(tileList.get(14), tileList.get(17), tileList.get(31), tileList.get(57), tileList.get(41), tileList.get(65));
        var menhirTiles = List.of(tileList.get(82), tileList.get(83), tileList.get(84), tileList.get(85));

        return new TileDecks(startTile, normalTiles, menhirTiles);
    }

    @Test
    public void gameStateConstructorCorrectlyDefined() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(), tileDecks, null, Board.EMPTY, GameState.Action.START_GAME, new MessageBoard(new BasicTextMaker(), new ArrayList<>())));
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks, null, Board.EMPTY, GameState.Action.PLACE_TILE, new MessageBoard(new BasicTextMaker(), new ArrayList<>())));
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks, tileDecks.topTile(Tile.Kind.NORMAL), Board.EMPTY, GameState.Action.START_GAME, new MessageBoard(new BasicTextMaker(), new ArrayList<>())));
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), null, null, Board.EMPTY, GameState.Action.START_GAME, new MessageBoard(new BasicTextMaker(), new ArrayList<>())));
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks, null, null, GameState.Action.START_GAME, new MessageBoard(new BasicTextMaker(), new ArrayList<>())));
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks, null, Board.EMPTY, null, new MessageBoard(new BasicTextMaker(), new ArrayList<>())));
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks, null, Board.EMPTY, GameState.Action.START_GAME, null));
    }

    @Test
    public void withPlacedTileThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks, tileDecks.topTile(Tile.Kind.NORMAL), Board.EMPTY, GameState.Action.START_GAME, new MessageBoard(new BasicTextMaker(), new ArrayList<>()))
                        .withPlacedTile(new PlacedTile(new Tile(341, Tile.Kind.NORMAL, sides3[0], sides3[1], sides3[2], sides3[3]), PlayerColor.RED, Rotation.NONE, new Pos(0,0))));
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks, tileDecks.topTile(Tile.Kind.NORMAL), Board.EMPTY, GameState.Action.PLACE_TILE, new MessageBoard(new BasicTextMaker(), new ArrayList<>()))
                        .withPlacedTile(new PlacedTile(new Tile(341, Tile.Kind.NORMAL, sides3[0], sides3[1], sides3[2], sides3[3]), PlayerColor.RED, Rotation.NONE, new Pos(0,0)).withOccupant(new Occupant(Occupant.Kind.PAWN, 12))));
    }

    //good
    @Test
    public void withPlacedTileNoPower() {
        GameState gameState = INITIAL_GAME_STATE.withStartingTilePlaced();
        gameState = gameState.withPlacedTile(new PlacedTile(new Tile(342, Tile.Kind.NORMAL, sides1[0], sides1[1], sides1[2], sides1[3]), PlayerColor.RED, Rotation.RIGHT, new Pos(0,1)));
        PlacedTile[] placedTiles = {new PlacedTile(new Tile(341, Tile.Kind.START, sides3[0], sides3[1], sides3[2], sides3[3]), null, Rotation.NONE, new Pos(0,0)), new PlacedTile(new Tile(342, Tile.Kind.NORMAL, sides1[0], sides1[1], sides1[2], sides1[3]), PlayerColor.RED, Rotation.RIGHT, new Pos(0,1))};
        TileDecks tileDecks1 = tileDecks.withTopTileDrawn(Tile.Kind.NORMAL).withTopTileDrawn(Tile.Kind.START);
        GameState gameState1 = new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks1, null,
                Board.EMPTY
                        .withNewTile(new PlacedTile(new Tile(342, Tile.Kind.NORMAL, sides1[0], sides1[1], sides1[2], sides1[3]), PlayerColor.RED, Rotation.RIGHT, new Pos(0,1)))
                        .withNewTile(new PlacedTile(new Tile(341, Tile.Kind.START, sides3[0], sides3[1], sides3[2], sides3[3]), null, Rotation.NONE, new Pos(0,0))),
                GameState.Action.OCCUPY_TILE, new MessageBoard(BASIC_TEXT_MAKER, new ArrayList<>()));

        assertEquals(gameState, gameState1);
    }

    @Test
    void withOccupantRemoved() {

        List<PlayerColor> colorList = List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.BLUE);
        MessageBoard messageBoard = new MessageBoard(new BasicTextMaker(), List.of());
        TileDecks deck = deckGenerator().withTopTileDrawn(Tile.Kind.START);

        var t56 = new PlacedTile(tileList.get(56), PlayerColor.GREEN, Rotation.NONE, new Pos(0, 0));
        var t17 = new PlacedTile(tileList.get(17), PlayerColor.GREEN, Rotation.NONE, new Pos(-1, 0));
        var t27 = new PlacedTile(tileList.get(27), PlayerColor.GREEN, Rotation.NONE, new Pos(-2, 0));


        var occupant56 = new Occupant(Occupant.Kind.PAWN, 560);
        var occupant17 = new Occupant(Occupant.Kind.PAWN, 171);
        var occupant27 = new Occupant(Occupant.Kind.PAWN, 273);


        var board = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t17)
                .withOccupant(occupant17)
                .withNewTile(t27)
                .withOccupant(occupant27);

        var boardEmpty = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t17)
                .withOccupant(occupant17)
                .withNewTile(t27);

        GameState removedState = new GameState(colorList, deck, null, board, GameState.Action.RETAKE_PAWN, messageBoard).withOccupantRemoved(occupant27);

        GameState expectedState = new GameState(colorList, deck, null, boardEmpty, GameState.Action.OCCUPY_TILE, messageBoard);

        assertEquals(expectedState, removedState);

    }

    @Test
    void withOccupantRemoveAndNoOccupationPossible(){

        List<PlayerColor> colorList = List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.BLUE);

        List<PlayerColor> colorSwitch = new ArrayList<>(colorList);
        colorSwitch.addLast(colorSwitch.getFirst());
        colorSwitch.removeFirst();

        MessageBoard messageBoard = new MessageBoard(new BasicTextMaker(), List.of());
        TileDecks deck = deckGenerator().withTopTileDrawn(Tile.Kind.START);

        var t56 = new PlacedTile(tileList.get(56), PlayerColor.GREEN, Rotation.NONE, new Pos(0, 0));
        var t37 = new PlacedTile(tileList.get(37), PlayerColor.GREEN, Rotation.NONE, new Pos(0, 1));
        var t36 = new PlacedTile(tileList.get(36), PlayerColor.GREEN, Rotation.RIGHT, new Pos(1, 0));
        var t62 = new PlacedTile(tileList.get(62), PlayerColor.GREEN, Rotation.NONE, new Pos(2, 0));
        var t88 = new PlacedTile(tileList.get(88), PlayerColor.GREEN, Rotation.NONE, new Pos(3, 0));
        var t61 = new PlacedTile(tileList.get(61), PlayerColor.GREEN, Rotation.RIGHT, new Pos(3, 0));


        var occupant56 = new Occupant(Occupant.Kind.PAWN, 561);
        var occupant37 = new Occupant(Occupant.Kind.PAWN, 371);
        var occupant36 = new Occupant(Occupant.Kind.PAWN, 360);
        var occupant63 = new Occupant(Occupant.Kind.PAWN, 630);


        var board = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t37)
                .withOccupant(occupant37)
                .withNewTile(t36)
                .withOccupant(occupant36)
                .withNewTile(t62);

        var boardEmpty = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t37)
                .withOccupant(occupant37)
                .withNewTile(t36)
                .withOccupant(occupant36)
                .withNewTile(t62)
                .withNewTile(t88);

        var lastBoard = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t37)
                .withOccupant(occupant37)
                .withNewTile(t36)
                .withNewTile(t62)
                .withNewTile(t88);

        var secondBoard = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t37)
                .withOccupant(occupant37)
                .withNewTile(t36)
                .withOccupant(occupant36)
                .withNewTile(t62)
                .withNewTile(t61);

        var secondBoardCleaned = Board.EMPTY
                .withNewTile(t56)
                .withNewTile(t37)
                .withOccupant(occupant37)
                .withNewTile(t36)
                .withOccupant(occupant36)
                .withNewTile(t62)
                .withNewTile(t61);

        GameState removedState = new GameState(colorList, deck, t88.tile(), board, GameState.Action.PLACE_TILE, messageBoard).withPlacedTile(t88).withOccupantRemoved(null).withNewOccupant(null);
        GameState nullRemove = new GameState(colorSwitch, deck.withTopTileDrawn(Tile.Kind.NORMAL), deck.topTile(Tile.Kind.NORMAL), boardEmpty, GameState.Action.PLACE_TILE, messageBoard);
        assertEquals(nullRemove, removedState);

        GameState state = new GameState(colorList, deck, null, secondBoard, GameState.Action.RETAKE_PAWN, messageBoard)
                .withOccupantRemoved(occupant56);

        GameState lastState = new GameState(colorList, deck, t88.tile(), board, GameState.Action.PLACE_TILE, messageBoard).withPlacedTile(t88).withOccupantRemoved(occupant36).withNewOccupant(null);
        GameState expectedLastState = new GameState(colorSwitch, deck.withTopTileDrawn(Tile.Kind.NORMAL), deck.topTile(Tile.Kind.NORMAL), lastBoard, GameState.Action.PLACE_TILE, messageBoard);

        GameState expectedState = new GameState(colorSwitch, deck.withTopTileDrawn(Tile.Kind.NORMAL), deck.topTile(Tile.Kind.NORMAL), secondBoardCleaned, GameState.Action.PLACE_TILE, messageBoard);

        assertNotEquals(deck.topTile(Tile.Kind.NORMAL), deck.withTopTileDrawn(Tile.Kind.NORMAL).topTile(Tile.Kind.NORMAL));

        assertEquals(expectedState, state);
        assertEquals(expectedLastState, lastState);
    }


    //good
    @Test
    void withNewOccupantWithMenhirClosed() {

        List<PlayerColor> colorList = List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.BLUE);

        List<PlayerColor> colorSwitch = new ArrayList<>(colorList);
        colorSwitch.addLast(colorSwitch.getFirst());
        colorSwitch.removeFirst();

        MessageBoard messageBoard = new MessageBoard(new BasicTextMaker(), List.of());
        TileDecks deck = deckGenerator().withTopTileDrawn(Tile.Kind.START);

        var t56 = new PlacedTile(tileList.get(56), PlayerColor.GREEN, Rotation.NONE, new Pos(0, 0));
        var t37 = new PlacedTile(tileList.get(37), PlayerColor.GREEN, Rotation.NONE, new Pos(0, 1));
        var t36 = new PlacedTile(tileList.get(36), PlayerColor.GREEN, Rotation.RIGHT, new Pos(1, 0));

        var occupant56 = new Occupant(Occupant.Kind.PAWN, 561);
        var occupant37 = new Occupant(Occupant.Kind.PAWN, 371);
        var occupant36 = new Occupant(Occupant.Kind.PAWN, 360);

        var boardOccupied= Board.EMPTY
                .withNewTile(t56)
                .withNewTile(t37)
                .withOccupant(occupant37)
                .withNewTile(t36)
                .withOccupant(occupant36);

        var boardEmpty = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t37)
                .withOccupant(occupant37);

        GameState state = new GameState(colorList, deck, t36.tile(), boardEmpty, GameState.Action.PLACE_TILE, messageBoard)
                .withPlacedTile(t36)
                .withNewOccupant(occupant36);

        Area<Zone.Forest> area = new Area<>(Set.of(new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(36_1, Zone.Forest.Kind.PLAIN), new Zone.Forest(37_0, Zone.Forest.Kind.PLAIN)), List.of(PlayerColor.GREEN, PlayerColor.GREEN, PlayerColor.GREEN), 0);

        GameState expectedState = new GameState(colorList, deck.withTopTileDrawn(Tile.Kind.MENHIR), deck.topTile(Tile.Kind.MENHIR), boardOccupied, GameState.Action.PLACE_TILE, messageBoard.withClosedForestWithMenhir(PlayerColor.GREEN, area));

        assertNotEquals(deck.topTile(Tile.Kind.MENHIR), deck.withTopTileDrawn(Tile.Kind.MENHIR).topTile(Tile.Kind.MENHIR));

        assertEquals(expectedState, state);
    }

    @Test
    void withNewOccupantWithEndOfTurn() {
        List<PlayerColor> colorList = List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.BLUE);

        List<PlayerColor> colorSwitch = new ArrayList<>(colorList);
        colorSwitch.addLast(colorSwitch.getFirst());
        colorSwitch.removeFirst();

        MessageBoard messageBoard = new MessageBoard(new BasicTextMaker(), List.of());
        TileDecks deck = deckGenerator().withTopTileDrawn(Tile.Kind.START);

        var t56 = new PlacedTile(tileList.get(56), PlayerColor.GREEN, Rotation.NONE, new Pos(0, 0));
        var t36 = new PlacedTile(tileList.get(36), PlayerColor.GREEN, Rotation.RIGHT, new Pos(1, 0));

        var occupant56 = new Occupant(Occupant.Kind.PAWN, 561);
        var occupant36 = new Occupant(Occupant.Kind.PAWN, 360);

        var boardOccupied= Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t36)
                .withOccupant(occupant36);

        var boardEmpty = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56);

        GameState state = new GameState(colorList, deck, t36.tile(), boardEmpty, GameState.Action.PLACE_TILE, messageBoard)
                .withPlacedTile(t36)
                .withNewOccupant(occupant36);

        GameState expectedState = new GameState(colorSwitch, deck.withTopTileDrawn(Tile.Kind.NORMAL), deck.topTile(Tile.Kind.NORMAL), boardOccupied, GameState.Action.PLACE_TILE, messageBoard);

        assertNotEquals(deck.topTile(Tile.Kind.NORMAL), deck.withTopTileDrawn(Tile.Kind.NORMAL).topTile(Tile.Kind.NORMAL));

        assertEquals(expectedState, state);
    }

    @Test
    void withOccupantRemovedCorrectly() {

        List<PlayerColor> colorList = List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.BLUE);
        MessageBoard messageBoard = new MessageBoard(new BasicTextMaker(), List.of());
        TileDecks deck = deckGenerator().withTopTileDrawn(Tile.Kind.START);

        var t56 = new PlacedTile(tileList.get(56), PlayerColor.GREEN, Rotation.NONE, new Pos(0, 0));
        var t17 = new PlacedTile(tileList.get(17), PlayerColor.GREEN, Rotation.NONE, new Pos(-1, 0));
        var t27 = new PlacedTile(tileList.get(27), PlayerColor.GREEN, Rotation.NONE, new Pos(-2, 0));


        var occupant56 = new Occupant(Occupant.Kind.PAWN, 560);
        var occupant17 = new Occupant(Occupant.Kind.PAWN, 171);
        var occupant27 = new Occupant(Occupant.Kind.PAWN, 273);


        var board = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t17)
                .withOccupant(occupant17)
                .withNewTile(t27)
                .withOccupant(occupant27);

        var boardEmpty = Board.EMPTY
                .withNewTile(t56)
                .withOccupant(occupant56)
                .withNewTile(t17)
                .withOccupant(occupant17)
                .withNewTile(t27);

        GameState removedState = new GameState(colorList, deck, null, board, GameState.Action.RETAKE_PAWN, messageBoard).withOccupantRemoved(occupant27);

        GameState expectedState = new GameState(colorList, deck, null, boardEmpty, GameState.Action.OCCUPY_TILE, messageBoard);

        assertEquals(expectedState, removedState);

    }

    @Test
    void lastTilePotentialOccupantsWithOccupantsLeft() {

        List<PlayerColor> colorList = List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.BLUE);
        MessageBoard messageBoard = new MessageBoard(new BasicTextMaker(), List.of());

        var t56 = new PlacedTile(tileList.get(56), null, Rotation.NONE, new Pos(0, 0));
        var t17 = new PlacedTile(tileList.get(17), PlayerColor.BLUE, Rotation.NONE, new Pos(-1, 0));
        var t27 = new PlacedTile(tileList.get(27), PlayerColor.GREEN, Rotation.NONE, new Pos(-2, 0));

        var occupant17 = new Occupant(Occupant.Kind.PAWN, 17_0);

        var board = Board.EMPTY
                .withNewTile(t56)
                .withNewTile(t17)
                .withOccupant(occupant17)
                .withNewTile(t27);

        Set<Occupant> occupantList = Set.of(new Occupant(Occupant.Kind.PAWN, 273), new Occupant(Occupant.Kind.PAWN, 271), new Occupant(Occupant.Kind.PAWN, 272), new Occupant(Occupant.Kind.HUT, 271));

        assertEquals(occupantList, new GameState(colorList, deckGenerator(), deckGenerator().topTile(Tile.Kind.NORMAL), board, GameState.Action.PLACE_TILE, messageBoard).lastTilePotentialOccupants());

    }

}
