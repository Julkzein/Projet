package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    private BasicTextMaker BASIC_TEXT_MAKER = new BasicTextMaker();
    private final GameState INITIAL_GAME_STATE = GameState.initial(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks, BASIC_TEXT_MAKER);

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

    @Test
    public void withPlacedTileNoPower() {
        GameState gameState = INITIAL_GAME_STATE.withStartingTilePlaced();
        gameState = gameState.withPlacedTile(new PlacedTile(new Tile(342, Tile.Kind.NORMAL, sides1[0], sides1[1], sides1[2], sides1[3]), PlayerColor.RED, Rotation.RIGHT, new Pos(0,1)));
        PlacedTile[] placedTiles = {new PlacedTile(new Tile(341, Tile.Kind.START, sides3[0], sides3[1], sides3[2], sides3[3]), null, Rotation.NONE, new Pos(0,0)), new PlacedTile(new Tile(342, Tile.Kind.NORMAL, sides1[0], sides1[1], sides1[2], sides1[3]), PlayerColor.RED, Rotation.RIGHT, new Pos(0,1))};
        TileDecks tileDecks1 = tileDecks.withTopTileDrawn(Tile.Kind.NORMAL).withTopTileDrawn(Tile.Kind.START);
        assertEquals(gameState,
                new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), tileDecks1, null,
                        Board.EMPTY
                                .withNewTile(new PlacedTile(new Tile(342, Tile.Kind.NORMAL, sides1[0], sides1[1], sides1[2], sides1[3]), PlayerColor.RED, Rotation.RIGHT, new Pos(0,1)))
                                .withNewTile(new PlacedTile(new Tile(341, Tile.Kind.START, sides3[0], sides3[1], sides3[2], sides3[3]), null, Rotation.NONE, new Pos(0,0))),
                        GameState.Action.OCCUPY_TILE, new MessageBoard(BASIC_TEXT_MAKER, new ArrayList<>())));
    }


}
