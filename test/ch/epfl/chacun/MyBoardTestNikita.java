/**
 * MyBoardTest
 * @author Nikita Karpachev (373884)
 * @author Adrian Mikhaiel (380437)
 */

package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public final class MyBoardTestNikita {

    Board emptyBoard = Board.EMPTY;

    Tile tile = new Tile(3, Tile.Kind.NORMAL, new TileSide.Forest(new Zone.Forest(32, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(31, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(30, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(34, Zone.Forest.Kind.WITH_MENHIR)));
    Tile tile2 = new Tile(2, Tile.Kind.NORMAL, new TileSide.Forest(new Zone.Forest(22, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(21, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(20, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(24, Zone.Forest.Kind.WITH_MENHIR)));
    PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,0));
    Board boardWithOneTile = Board.EMPTY.withNewTile(placedTile);
    PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.GREEN, Rotation.RIGHT, new Pos(0,1));
    Board boardWithTwoTiles = boardWithOneTile.withNewTile(placedTile2);


    @Test
    void equalsWorksOnTrivialCases() {
        Board empty = Board.EMPTY;
        Board empty2 = Board.EMPTY;
        Board b0 = empty.withNewTile(placedTile);
        Board b1 = b0.withNewTile(placedTile2);

        assertTrue(empty.equals(empty2));
        assertFalse(empty.equals(b0));
        assertFalse(b0.equals(b1));
    }

    @Test
    void hashCodeWorksOnTrivialCases() {
        Board empty = Board.EMPTY;
        Board empty2 = Board.EMPTY;
        Board b0 = empty.withNewTile(placedTile);
        Board b1 = b0.withNewTile(placedTile2);

        assertEquals(empty.hashCode(), empty2.hashCode());
        assertNotEquals(empty.hashCode(), b0.hashCode());
        assertNotEquals(b0.hashCode(), b1.hashCode());
    }


    @Test
    void tileAtWorksWithEmptyBoard(){
        Board emptyBoard = Board.EMPTY;
        assertNull(emptyBoard.tileAt(new Pos(0,0)));
    }
    @Test
    void tileAtWorksWithBoardWithTileOutsideLimits() {
        assertNull(boardWithOneTile.tileAt(new Pos(100,100)));
        assertNull(boardWithOneTile.tileAt(new Pos(-100, -99)));
        assertNull(boardWithOneTile.tileAt(new Pos(-13, 12)));
        assertNull(boardWithOneTile.tileAt(new Pos(12, -13)));
    }

    @Test
    void tileAtWorksWithTrivialCases() {
        assertEquals(placedTile, boardWithOneTile.tileAt(new Pos(0,0)));
        assertEquals(placedTile, boardWithTwoTiles.tileAt(new Pos(0,0)));
        assertEquals(placedTile2, boardWithTwoTiles.tileAt(new Pos(0,1)));
    }


    @Test
    void tileWithIdThrowsOnInexistentTile() {
        Board emptyBoard = Board.EMPTY;
        assertThrows(IllegalArgumentException.class, () -> emptyBoard.tileWithId(9));
    }

    @Test
    void tileWithIdWorksWithTrivialCases() {
        assertEquals(placedTile, boardWithOneTile.tileWithId(3));
        assertEquals(placedTile, boardWithTwoTiles.tileWithId(3));
        assertEquals(placedTile2, boardWithTwoTiles.tileWithId(2));
    }


    @Test
    void cancelledAnimalsIsImmutable() {
        Set<Animal> cancelledAnimals = emptyBoard.cancelledAnimals();
        assertThrows(UnsupportedOperationException.class, () -> cancelledAnimals.add(new Animal(0, Animal.Kind.DEER)));
    }

    @Test
    void occupantsWorksTrivialCases() {
        HashSet<Occupant> occupants = new HashSet<>();
        Occupant testOccupant = new Occupant(Occupant.Kind.PAWN, 0);
        Occupant testOccupant2 = new Occupant(Occupant.Kind.HUT, 1);
        PlacedTile internalPlacedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,0));
        PlacedTile internalPlacedTile2 = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1,1));

        PlacedTile newTile = internalPlacedTile.withOccupant(testOccupant);
        PlacedTile newTile2 = internalPlacedTile2.withOccupant(testOccupant2);

        occupants.add(testOccupant);

        Board b0 = Board.EMPTY.withNewTile(newTile);
        Board b1 = b0.withNewTile(newTile2);

        assertEquals(occupants, b0.occupants());

        occupants.add(testOccupant2);

        assertEquals(occupants, b1.occupants());

        assertEquals(Set.of(), emptyBoard.occupants());

        assertNotEquals(b0.occupants(), b1.occupants());
    }
    @Test
    void forestAreaThrowsOnZoneNotInBoard() {
        Zone.Forest forest = new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR);
        assertThrows(IllegalArgumentException.class, () -> emptyBoard.forestArea(forest));
    }

    @Test
    void forestAreaWorksWithTrivialCases() {

        Zone.Forest forest1 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest3 = new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest4 = new Zone.Forest(5, Zone.Forest.Kind.WITH_MENHIR);


        Tile t1 = new Tile(33, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1));
        Tile t2 = new Tile(34, Tile.Kind.NORMAL, new TileSide.Forest(forest2), new TileSide.Forest(forest2), new TileSide.Forest(forest2), new TileSide.Forest(forest2));
        Tile t3 = new Tile(35, Tile.Kind.NORMAL, new TileSide.Forest(forest3), new TileSide.Forest(forest3), new TileSide.Forest(forest3), new TileSide.Forest(forest3));
        Tile t4 = new Tile(36, Tile.Kind.NORMAL, new TileSide.Forest(forest4), new TileSide.Forest(forest4), new TileSide.Forest(forest4), new TileSide.Forest(forest4));

        PlacedTile pt1 = new PlacedTile(t1, PlayerColor.RED, Rotation.NONE, new Pos(0,0));
        PlacedTile pt2 = new PlacedTile(t2, PlayerColor.BLUE, Rotation.NONE, new Pos(0,1));
        PlacedTile pt3 = new PlacedTile(t3, PlayerColor.RED, Rotation.NONE, new Pos(1,0));
        PlacedTile pt4 = new PlacedTile(t4, PlayerColor.BLUE, Rotation.NONE, new Pos(1,1));

        Board b = Board.EMPTY.withNewTile(pt1).withNewTile(pt2).withNewTile(pt3).withNewTile(pt4);

        Area<Zone.Forest> area1 = new Area<>(Set.of(forest1), List.of(), 4);
        Area<Zone.Forest> area2 = new Area<>(Set.of(forest2), List.of(), 4);
        Area<Zone.Forest> area3 = new Area<>(Set.of(forest3, forest4), List.of(), 4);

        assertEquals(area1, b.forestArea(forest1));
        assertEquals(area2, b.forestArea(forest2));
        //TODO revoir
        //assertEquals(area3, b.forestArea(forest3));
        //assertEquals(area3, b.forestArea(forest4));
        assertNotEquals(area1, b.forestArea(forest2));

    }
    //methodes meadowArea, riverArea et riverSystemArea identiques donc inutiles de faire les tests sur les 3

    /*@Test
    void meadowAreasWorksWithTrivialCases() {
        Zone.Meadow meadow = new Zone.Meadow(2, List.of(), Zone.SpecialPower.LOGBOAT);
        Animal an0 = new Animal(3, Animal.Kind.TIGER);
        Zone.Meadow meadow1 = new Zone.Meadow(3, List.of(an0), Zone.SpecialPower.RAFT);
        Area<Zone.Meadow> a0 = new Area<>(Set.of(meadow), List.of(), 4);

        Board e0 = Board.EMPTY;
        Tile t0 = new Tile(22, Tile.Kind.NORMAL, new TileSide.Meadow(meadow), new TileSide.Meadow(meadow), new TileSide.Meadow(meadow), new TileSide.Meadow(meadow));
        PlacedTile pt0 = new PlacedTile(t0, PlayerColor.RED, Rotation.NONE, new Pos(0,0));

        Board e0Meadow = e0.withNewTile(pt0);

        Tile t1 = new Tile(23, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Meadow(meadow1), new TileSide.Meadow(meadow1), new TileSide.Meadow(meadow1));
        PlacedTile pt1 = new PlacedTile(t1, PlayerColor.BLUE, Rotation.NONE, new Pos(0,1));

        Board e1Meadow = e0Meadow.withNewTile(pt1);

        HashSet<Area<Zone.Meadow>> set = new HashSet<>();
        Area<Zone.Meadow> a1 = new Area<>(Set.of(meadow), List.of(), 4);
        Area<Zone.Meadow> a2 = new Area<>(Set.of(meadow1), List.of(), 4);

        set.add(a1);
        set.add(a2);

        for (int i = 0; i < e1Meadow.getIndexes().length; i++) {
            System.out.println(e1Meadow.getTileTab()[e1Meadow.getIndexes()[i]].tile());
        }

        assertEquals(set, e1Meadow.meadowAreas());

    }*/

    @Test
    void meadowAreasWorksOnTrivialCases(){
        Zone.Meadow meadow = new Zone.Meadow(2, List.of(), Zone.SpecialPower.LOGBOAT);
        Zone.Meadow meadow1 = new Zone.Meadow(3, List.of(), Zone.SpecialPower.RAFT);
        Area<Zone.Meadow> a0 = new Area<>(Set.of(meadow), List.of(), 4);
        Area<Zone.Meadow> a1 = new Area<>(Set.of(meadow1), List.of(), 4);

        ZonePartitions partitions = new ZonePartitions(new ZonePartition<>(), new ZonePartition<>(Set.of(a0, a1)), new ZonePartition<>(), new ZonePartition<>());
        //TODO revoir
        //Board board = new Board(new PlacedTile[0], new int[0], partitions, Set.of(), Set.of());


    }
    //TODO revoir
    @Test
    void adjacentMeadowWorksOnTrivialCases(){

    }

    @Test
    void occupantCountWorksWithTrivialCases() {
        Board empty = Board.EMPTY;
        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 32);
        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN, 21);

        Board occ0 = empty.withNewTile(placedTile).withOccupant(occupant);
        Board occ1 = occ0.withNewTile(placedTile2).withOccupant(occupant2);

        assertEquals(1, occ0.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(0, occ0.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(0, empty.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(1, occ1.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(1, occ1.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
    }

    @Test
    void insertionsPositionsWorksOnEmtpyBoard(){
        Board board = Board.EMPTY;
        assertEquals(0, board.insertionPositions().size());
    }

    @Test
    void insertionPositionsWorksOnBoardWithOneTile(){
        Board board = Board.EMPTY.withNewTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,0)));
        assertEquals(Set.of(new Pos(0, -1), new Pos(0, 1), new Pos(-1, 0), new Pos(1, 0)), board.insertionPositions());
    }

    @Test
    void insertinoPositionsWorksOnBoardWithMoreTiles() {
        Pos p1 = new Pos(0,0);
        Pos p2 = new Pos(1,0);

        Tile tile = new Tile(3, Tile.Kind.NORMAL, new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)));
        Tile secondTile = new Tile(2, Tile.Kind.NORMAL, new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)));
        Board board = Board.EMPTY.withNewTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, p1));
        Board twoTilesBoard = board.withNewTile(new PlacedTile(secondTile, PlayerColor.RED, Rotation.NONE, p2));

        assertEquals(Set.of(new Pos(-1, 0), new Pos(0, -1),
                        new Pos(1, -1), new Pos(2, 0),
                        new Pos(1,1), new Pos(0, 1)),
                        twoTilesBoard.insertionPositions());
    }

    @Test
    void insertionPositionsWorksOnBoardWithTilesOnEdges(){
        Pos p1 = new Pos(-12,-12);
        Pos p2 = new Pos(12,12);

        Tile tile = new Tile(3, Tile.Kind.NORMAL, new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)));
        Tile secondTile = new Tile(2, Tile.Kind.NORMAL, new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)));
        Board board = Board.EMPTY.withNewTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, p1));
        Board twoTilesBoard = board.withNewTile(new PlacedTile(secondTile, PlayerColor.RED, Rotation.NONE, p2));

        assertEquals(Set.of(new Pos(-12, -11), new Pos(-11, -12),
                        new Pos(11, 12), new Pos(12, 11)),
                        twoTilesBoard.insertionPositions());
    }



    @Test
    void lastPlacedTileWorks() {
        Board empty = Board.EMPTY;
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,0));
        PlacedTile pt = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1,1));

        Board b0 = empty.withNewTile(placedTile);
        Board b1 = b0.withNewTile(placedTile2);
        Board b2 = b1.withNewTile(pt);

        assertNull(empty.lastPlacedTile());
        assertEquals(placedTile2, b1.lastPlacedTile());
        assertEquals(pt, b2.lastPlacedTile());

    }

    @Test
    void lastPlacedTileWorksOnTrivialCases(){
        Board empty = Board.EMPTY;
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(0,1));

        Board b0 = empty.withNewTile(placedTile);
        Board b1 = b0.withNewTile(placedTile2);

        assertEquals(placedTile2, b1.lastPlacedTile());
        assertNotEquals(placedTile, b1.lastPlacedTile());
    }

    @Test
    void lastPlacedTileWorksOnEmptyBoard(){
        assertNull(emptyBoard.lastPlacedTile());
    }


    @Test
    void forestsClosedByLastTileWorksTrivialCases() {
        Board empty = Board.EMPTY;
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(0,1));

        Board b0 = empty.withNewTile(placedTile);
        Board b1 = b0.withNewTile(placedTile2);

        assertEquals(Set.of(), empty.forestsClosedByLastTile());
    }

    @Test
    void canAddTileWorksTrivialCases() {
    Board empty = Board.EMPTY;
    PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,0));
    PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(0,1));

    Board b0 = empty.withNewTile(placedTile);
    Board b1 = b0.withNewTile(placedTile2);

    assertTrue(b0.canAddTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,1))));
    assertTrue(b0.canAddTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(1,0))));
    assertTrue(b0.canAddTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(-1,0))));
    assertTrue(b0.canAddTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,-1))));

    assertFalse(b0.canAddTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,0))));
    assertFalse(b0.canAddTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(1,1))));
    assertFalse(b0.canAddTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(-1,-1))));
    assertFalse(b0.canAddTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(1,-1))));
    assertFalse(b0.canAddTile(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(-1,1))));

    assertTrue(b1.canAddTile(new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(0,2))));
    assertTrue(b1.canAddTile(new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1,1))));
    assertTrue(b1.canAddTile(new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(-1,1))));
    assertTrue(b1.canAddTile(new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1,0))));
    assertTrue(b1.canAddTile(new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(-1,0))));
    assertTrue(b1.canAddTile(new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(0, -1))));
    }

    //TODO finir
    @Test
    void couldPlaceTileWorksOnTrivialCases(){
        Board empty = Board.EMPTY;
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0,0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(0,1));

        Board b0 = empty.withNewTile(placedTile);
        Board b1 = b0.withNewTile(placedTile2);

        assertTrue(b0.couldPlaceTile(tile));
        assertTrue(b1.couldPlaceTile(tile2));

    }

    //TODO revoir
    @Test
    void withNewTileWorks() {
        Tile tile = new Tile(3, Tile.Kind.NORMAL, new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS)));
        PlacedTile placed = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(-1,0));

        Board board = Board.EMPTY.withNewTile(placed);

//        System.out.println(board.getIndexes().length);

        for (int i = 0; i < board.index.length; i++) {
//            System.out.println(board.getIndexes()[i]);
        }
        System.out.println(board.index[0]);


        for (int i = 0; i < board.index.length; i++) {
            System.out.println(board.index[i]);
        }

    }

    //TODO finir
    @Test
    void withOccupantWorksOnTrivialCases() {

    }

    //TODO finir
    @Test
    void withoutOccupantWorksOnTrivialCases() {

    }


    //TODO finir
    @Test
    void withoutGatherersOrFishersInWorksTrivialCases() {


    }

    @Test
    void withMoreCancelledAnimalsWorks() {
        Set<Animal> cancelledAnimals = new HashSet<>();
        Animal animal = new Animal(0, Animal.Kind.TIGER);
        Animal animal2 = new Animal(1, Animal.Kind.DEER);
        cancelledAnimals.add(animal);
        cancelledAnimals.add(animal2);

        Board empty = Board.EMPTY;
        Board board = Board.EMPTY.withMoreCancelledAnimals(cancelledAnimals);

        assertEquals(Set.of(), empty.cancelledAnimals());
        assertEquals(cancelledAnimals, board.cancelledAnimals());
    }
}
