package ch.epfl.chacun;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyBoardTest {
    public Board buildBoardWithTile32() {
        var z0 = new Zone.Forest(32_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0 = new Animal(32_1_0, Animal.Kind.TIGER);
        var z1 = new Zone.Meadow(32_1, List.of(a1_0), null);
        var sN = new TileSide.Forest(z0);
        var sE = new TileSide.Meadow(z1);
        var sS = new TileSide.Meadow(z1);
        var sW = new TileSide.Forest(z0);
        var tile = new Tile(32, Tile.Kind.NORMAL, sN, sE, sS, sW);

        var emptyPartitions = new ZonePartitions(
                new ZonePartition<>(),
                new ZonePartition<>(),
                new ZonePartition<>(),
                new ZonePartition<>());

        var b = new ZonePartitions.Builder(emptyPartitions);

        b.addTile(tile);
        b.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, z0);
        ZonePartitions partitions = b.build();

        PlacedTile placedTile32 = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0), new Occupant(Occupant.Kind.PAWN, 0));
        PlacedTile[] placedTiles = {placedTile32};
        int[] index = {32};
        Set<Animal> canceledAnimal = Set.of();
        return new Board(placedTiles, index, partitions, canceledAnimal);
    }

    @Test
    void tileAtWorksCorrectly() {
        var z0 = new Zone.Forest(32_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0 = new Animal(32_1_0, Animal.Kind.TIGER);
        var z1 = new Zone.Meadow(32_1, List.of(a1_0), null);
        var sN = new TileSide.Forest(z0);
        var sE = new TileSide.Meadow(z1);
        var sS = new TileSide.Meadow(z1);
        var sW = new TileSide.Forest(z0);
        var tile = new Tile(32, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile32 = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0), new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(placedTile32, buildBoardWithTile32().tileAt(new Pos(0, 0)));
    }

    @Test
    void tileAtReturnsNullWhenNoSuchTile() {
        assertNull(buildBoardWithTile32().tileAt(new Pos(1, 1)));
    }

    @Test
    void occupantCountTest() {
        assertEquals(1, buildBoardWithTile32().occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
    }

    @Test
    void insertionPositionsTest() {
        Set<Pos> insert = Set.of(new Pos(0, 1), new Pos(0, -1), new Pos(1, 0), new Pos(-1, 0));
        assertEquals(insert, buildBoardWithTile32().insertionPositions());
    }

    @Test
    void lastPlacedTileTest() {
        var z0 = new Zone.Forest(32_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0 = new Animal(32_1_0, Animal.Kind.TIGER);
        var z1 = new Zone.Meadow(32_1, List.of(a1_0), null);
        var sN = new TileSide.Forest(z0);
        var sE = new TileSide.Meadow(z1);
        var sS = new TileSide.Meadow(z1);
        var sW = new TileSide.Forest(z0);
        var tile = new Tile(32, Tile.Kind.NORMAL, sN, sE, sS, sW);
        assertEquals(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0), new Occupant(Occupant.Kind.PAWN, 0)), buildBoardWithTile32().lastPlacedTile());
    }



}

