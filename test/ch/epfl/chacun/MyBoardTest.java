package ch.epfl.chacun;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashSet;
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
        return null;
        //return new Board(placedTiles, index, partitions, canceledAnimal);
    }

    public Tile buildTile83() {
        var l083 = new Zone.Lake(83_8, 2, null);
        var l183 = new Zone.Lake(83_9, 2, null);
        var a0_083 = new Animal(83_0_0, Animal.Kind.DEER);
        var a0_183 = new Animal(83_0_1, Animal.Kind.DEER);
        var z083 = new Zone.Meadow(83_0, List.of(a0_083, a0_183), null);
        var z183 = new Zone.River(83_1, 0, l083);
        var z283 = new Zone.Meadow(83_2, List.of(), null);
        var z383 = new Zone.River(83_3, 0, l083);
        var z483 = new Zone.River(83_4, 0, l183);
        var z583 = new Zone.Meadow(83_5, List.of(), null);
        var z683 = new Zone.River(83_6, 0, l183);
        var sN83 = new TileSide.River(z083, z183, z283);
        var sE83 = new TileSide.River(z283, z383, z083);
        var sS83 = new TileSide.River(z083, z483, z583);
        var sW83 = new TileSide.River(z583, z683, z083);
        return new Tile(83, Tile.Kind.MENHIR, sN83, sE83, sS83, sW83);
    }

    public Board buildBoardWithTile3283() {
        var z0 = new Zone.Forest(32_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0 = new Animal(32_1_0, Animal.Kind.TIGER);
        var z1 = new Zone.Meadow(32_1, List.of(a1_0), null);
        var sN = new TileSide.Forest(z0);
        var sE = new TileSide.Meadow(z1);
        var sS = new TileSide.Meadow(z1);
        var sW = new TileSide.Forest(z0);
        var tile = new Tile(32, Tile.Kind.NORMAL, sN, sE, sS, sW);


        var l083 = new Zone.Lake(83_8, 2, null);
        var l183 = new Zone.Lake(83_9, 2, null);
        var a0_083 = new Animal(83_0_0, Animal.Kind.DEER);
        var a0_183 = new Animal(83_0_1, Animal.Kind.DEER);
        var z083 = new Zone.Meadow(83_0, List.of(a0_083, a0_183), null);
        var z183 = new Zone.River(83_1, 0, l083);
        var z283 = new Zone.Meadow(83_2, List.of(), null);
        var z383 = new Zone.River(83_3, 0, l083);
        var z483 = new Zone.River(83_4, 0, l183);
        var z583 = new Zone.Meadow(83_5, List.of(), null);
        var z683 = new Zone.River(83_6, 0, l183);
        var sN83 = new TileSide.River(z083, z183, z283);
        var sE83 = new TileSide.River(z283, z383, z083);
        var sS83 = new TileSide.River(z083, z483, z583);
        var sW83 = new TileSide.River(z583, z683, z083);
        var tile83 =  new Tile(83, Tile.Kind.MENHIR, sN83, sE83, sS83, sW83);

        var emptyPartitions = new ZonePartitions(
                new ZonePartition<>(),
                new ZonePartition<>(),
                new ZonePartition<>(),
                new ZonePartition<>());

        var b = new ZonePartitions.Builder(emptyPartitions);

        b.addTile(tile);
        b.addTile(buildTile83());
        b.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, z0);
        b.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, z083);
        ZonePartitions partitions = b.build();

        PlacedTile placedTile32 = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0), new Occupant(Occupant.Kind.PAWN, 0));
        PlacedTile placedTile83 = new PlacedTile(buildTile83(), PlayerColor.RED, Rotation.NONE, new Pos(0, 1), new Occupant(Occupant.Kind.PAWN, 0));
        PlacedTile[] placedTiles = {placedTile32, placedTile83};
        int[] index = {32, 83};
        Set<Animal> canceledAnimal = Set.of();
        return null;
        //return new Board(placedTiles, index, partitions, canceledAnimal);
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
        assertEquals(2, buildBoardWithTile3283().occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
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


    @Test
    void insertionALaZeub() {
        Set<Pos> positionOnTheFirstPos = new HashSet<>();

        positionOnTheFirstPos.add(new Pos(0,1));
        positionOnTheFirstPos.add(new Pos(0,-1));
        positionOnTheFirstPos.add(new Pos(1,0));
        positionOnTheFirstPos.add(new Pos(-1,0));

        assertEquals(positionOnTheFirstPos, buildBoardWithTile32().insertionPositions());
    }


}

