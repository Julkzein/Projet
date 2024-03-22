package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import javax.print.DocFlavor;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TheirBoardTest {

    private Tile firstTile(){
        TileSide.Meadow meadow0 = new TileSide.Meadow(new Zone.Meadow(560, Collections.emptyList(), null));
        TileSide.Forest forest1 =  new TileSide.Forest(new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.Forest forest2 =  new TileSide.Forest(new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.River river3 = new TileSide.River(new Zone.Meadow(562, Collections.emptyList(), null), new Zone.River(563, 0, new Zone.Lake(568,1,null)), new Zone.Meadow(560,Collections.emptyList(), null));

        return new Tile(56, Tile.Kind.NORMAL, meadow0, forest1, forest2, river3);

    }
    private Tile firstTileWithoutLake(){
        TileSide.Meadow meadow0 = new TileSide.Meadow(new Zone.Meadow(560, Collections.emptyList(), null));
        TileSide.Forest forest1 =  new TileSide.Forest(new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.Forest forest2 =  new TileSide.Forest(new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.River river3 = new TileSide.River(new Zone.Meadow(562, Collections.emptyList(), null), new Zone.River(563, 0, new Zone.Lake(568,1,null)), new Zone.Meadow(560,Collections.emptyList(), null));

        return new Tile(56, Tile.Kind.NORMAL, meadow0, forest1, forest2, river3);

    }
    private Tile otherTile(){
        TileSide.River river0Second = new TileSide.River(new Zone.Meadow(170, Collections.emptyList(), null), new Zone.River(171, 0, null), new Zone.Meadow(172,Collections.emptyList(), null));
        TileSide.River river1Second = new TileSide.River(new Zone.Meadow(172, Collections.emptyList(), null), new Zone.River(171, 0, null), new Zone.Meadow(170,Collections.emptyList(), null));
        TileSide.River river2Second = new TileSide.River(new Zone.Meadow(170, Collections.emptyList(), null), new Zone.River(173, 0, null), new Zone.Meadow(174,Collections.emptyList(), null));
        TileSide.River river3Second = new TileSide.River(new Zone.Meadow(174, Collections.emptyList(), null), new Zone.River(173, 0, null), new Zone.Meadow(170,Collections.emptyList(), null));

        return new  Tile(17, Tile.Kind.NORMAL, river0Second, river1Second, river2Second, river3Second);

    }

    private Tile withOneRiverOnNorth(){

        TileSide.River riverN = new TileSide.River(new Zone.Meadow(400, Collections.emptyList(), null), new Zone.River(401, 0, null), new Zone.Meadow(402,Collections.emptyList(), null));
        TileSide.Forest forestALl =  new TileSide.Forest(new Zone.Forest(403, Zone.Forest.Kind.WITH_MENHIR));

        return new Tile(40, Tile.Kind.NORMAL, riverN, forestALl, forestALl, forestALl);

    }
    private Tile withOneRiverOnSouth(){

        TileSide.River riverS = new TileSide.River(new Zone.Meadow(410, Collections.emptyList(), null), new Zone.River(411, 0, null), new Zone.Meadow(412,Collections.emptyList(), null));
        TileSide.Forest forestALl =  new TileSide.Forest(new Zone.Forest(413, Zone.Forest.Kind.WITH_MENHIR));

        return new Tile(41, Tile.Kind.NORMAL, forestALl, forestALl, riverS, forestALl);

    }

    private Tile withTranseversaleRiverNS(){

        TileSide.River river = new TileSide.River(new Zone.Meadow(220, Collections.emptyList(), null), new Zone.River(221, 0, null), new Zone.Meadow(222,Collections.emptyList(), null));
        TileSide.Forest forestALl =  new TileSide.Forest(new Zone.Forest(223, Zone.Forest.Kind.WITH_MENHIR));

        return new Tile(22, Tile.Kind.NORMAL, river, forestALl, river, forestALl);

    }

    private Tile withRiverOnNAndS(){
        TileSide.River riverN = new TileSide.River(new Zone.Meadow(120, Collections.emptyList(), null), new Zone.River(121, 0, null), new Zone.Meadow(120,Collections.emptyList(), null));
        TileSide.River riverS = new TileSide.River(new Zone.Meadow(120, Collections.emptyList(), null), new Zone.River(122, 0, null), new Zone.Meadow(120,Collections.emptyList(), null));
        TileSide.Meadow meadow = new TileSide.Meadow(new Zone.Meadow(120,Collections.emptyList(), null));

        return new Tile(12, Tile.Kind.NORMAL, riverN, meadow, riverS, meadow);
    }

    private Tile fullForestTile(){
        TileSide.Forest forest1 =  new TileSide.Forest(new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR));
        return new  Tile(17, Tile.Kind.NORMAL, forest1, forest1, forest1, forest1);
    }

    private Tile forestOnEAndW(){
        TileSide.Forest forestE =  new TileSide.Forest(new Zone.Forest(781, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.Forest forestW =  new TileSide.Forest(new Zone.Forest(782, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.Meadow meadowSandN = new TileSide.Meadow(new Zone.Meadow(780, Collections.emptyList(), null));

        return new  Tile(78, Tile.Kind.NORMAL, meadowSandN, forestE, meadowSandN, forestW);
    }

    private Tile tileWithAllSides(){
        TileSide.Meadow meadow0 = new TileSide.Meadow(new Zone.Meadow(140, Collections.emptyList(), null));
        TileSide.Forest forest1 =  new TileSide.Forest(new Zone.Forest(141, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.River river2Second = new TileSide.River(new Zone.Meadow(142, Collections.emptyList(), null), new Zone.River(143, 0, null), new Zone.Meadow(144,Collections.emptyList(), null));
        TileSide.River river3Second = new TileSide.River(new Zone.Meadow(144, Collections.emptyList(), null), new Zone.River(143, 0, null), new Zone.Meadow(142,Collections.emptyList(), null));

        return new Tile(17, Tile.Kind.NORMAL, meadow0, forest1, river2Second, river3Second);
    }
    private Tile firstTileWithAnimals(){
        List<Animal> animalSet = new ArrayList<>(List.of(new Animal(5601, Animal.Kind.TIGER), new Animal(5602, Animal.Kind.DEER)));
        List<Animal> animalSet1 = new ArrayList<>(List.of(new Animal(5621, Animal.Kind.TIGER)));
        TileSide.Meadow meadow0 = new TileSide.Meadow(new Zone.Meadow(560, animalSet, null));
        TileSide.Forest forest1 =  new TileSide.Forest(new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.Forest forest2 =  new TileSide.Forest(new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.River river3 = new TileSide.River(new Zone.Meadow(562, animalSet1, null), new Zone.River(563, 0, new Zone.Lake(568,1,null)), new Zone.Meadow(560,Collections.emptyList(), null));

        return new Tile(56, Tile.Kind.NORMAL, meadow0, forest1, forest2, river3);

    }

    private Tile pitTrapTileNW(){

        TileSide.Meadow meadowN = new TileSide.Meadow(new Zone.Meadow(230, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP));
        TileSide.Meadow meadowW = new TileSide.Meadow(new Zone.Meadow(230, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP));
        TileSide.Forest forestES =  new TileSide.Forest(new Zone.Forest(231, Zone.Forest.Kind.PLAIN));

        return new Tile(23, Tile.Kind.NORMAL, meadowN, forestES, forestES, meadowW);
    }

    private Tile meadowFromEtoWAndS(){

        TileSide.Forest forestN =  new TileSide.Forest(new Zone.Forest(100, Zone.Forest.Kind.PLAIN));
        TileSide.Meadow meadowE = new TileSide.Meadow(new Zone.Meadow(101, Collections.emptyList(), null));
        TileSide.Meadow meadowW = new TileSide.Meadow(new Zone.Meadow(101, Collections.emptyList(), null));
        TileSide.Meadow meadowS = new TileSide.Meadow(new Zone.Meadow(101, Collections.emptyList(), null));

        return new Tile(10, Tile.Kind.NORMAL, forestN, meadowE, meadowS, meadowW);
    }

    private Tile tileWithAllSidesWithAnimals(){
        List<Animal> animalSet = new ArrayList<>(List.of(new Animal(1701, Animal.Kind.AUROCHS), new Animal(1702, Animal.Kind.MAMMOTH)));
        TileSide.Meadow meadow0 = new TileSide.Meadow(new Zone.Meadow(170, animalSet, null));
        TileSide.Forest forest1 =  new TileSide.Forest(new Zone.Forest(171, Zone.Forest.Kind.WITH_MENHIR));
        TileSide.River river2Second = new TileSide.River(new Zone.Meadow(172, Collections.emptyList(), null), new Zone.River(173, 0, null), new Zone.Meadow(174,Collections.emptyList(), null));
        TileSide.River river3Second = new TileSide.River(new Zone.Meadow(174, Collections.emptyList(), null), new Zone.River(173, 0, null), new Zone.Meadow(172,Collections.emptyList(), null));

        return new  Tile(17, Tile.Kind.NORMAL, meadow0, forest1, river2Second, river3Second);


    }
    @Test
    void tileAtWithValidPos(){



        PlacedTile firstTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));
        PlacedTile otherTile = new PlacedTile(withOneRiverOnSouth(), PlayerColor.RED, Rotation.NONE, new Pos(-11,-12), new Occupant(Occupant.Kind.PAWN, 170));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());
        partitions.addTile(otherTile());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[3];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;
        ints[2]=1;
        placedTiles[1]=otherTile;
        Board boardGame = new Board(ints, placedTiles, partitions.build());


        assertNull(boardGame.tileAt(new Pos(12,12)));
        assertNull(boardGame.tileAt(new Pos(0,1)));
        assertNull(boardGame.tileAt(new Pos(1,0)));
        assertNull(boardGame.tileAt(new Pos(0,-1)));

        assertEquals(firstTile,boardGame.tileAt(new Pos(0,0)));
        assertEquals(secondTile,boardGame.tileAt(new Pos(-1,0)));
        assertEquals(otherTile,boardGame.tileAt(new Pos(-11,-12)));

    }

    @Test
    void tileAtWithInvalidPos(){

        Board boardGame = Board.EMPTY;

        for(int i=-100; i<100; ++i){
            for (int j=-100; j<100; ++j){
                assertNull(boardGame.tileAt(new Pos(i,j)));
            }
        }

    }

    @Test
    void withNewTileInvalidAndValid(){

        //TODO ou lève IllegalArgumentException si le plateau n'est pas vide

        Board emptyBoard = Board.EMPTY;
        PlacedTile firstTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        partitions.addTile(firstTile());

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312] = firstTile;
        Board constructedBoard = new Board(ints, placedTiles, partitions.build());

        assertTrue(constructedBoard.equals(emptyBoard.withNewTile(firstTile)));

        assertThrows(IllegalArgumentException.class, ()-> constructedBoard.withNewTile(firstTile));


    }

    @Test
    void withNewTileMorePrecisely(){

        PlacedTile firstTile = new PlacedTile(pitTrapTileNW(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        PlacedTile secondTile = new PlacedTile(meadowFromEtoWAndS(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1,0), null);
        PlacedTile thirdTile = new PlacedTile(tileWithAllSides(), PlayerColor.RED, Rotation.RIGHT, new Pos(-2,0), null);
        PlacedTile forthTile = new PlacedTile(firstTile(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1,1), null);

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(pitTrapTileNW());
        partitions.addTile(firstTile());
        partitions.addTile(meadowFromEtoWAndS());
        partitions.addTile(tileWithAllSides());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));
        partitions.connectSides(secondTile.side(Direction.W), thirdTile.side(Direction.E));
        partitions.connectSides(secondTile.side(Direction.S), forthTile.side(Direction.N));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[4];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;
        ints[2]=310;
        placedTiles[310]=thirdTile;
        ints[3]=336;
        placedTiles[336]=forthTile;
        Board boardGame = new Board(ints, placedTiles, partitions.build());

        Board constructedBoard = Board.EMPTY;

        assertTrue(boardGame.equals(constructedBoard.withNewTile(firstTile).withNewTile(secondTile).withNewTile(thirdTile).withNewTile(forthTile)));

    }

    @Test
    void tileWithIdThrows(){

        Board boardGame = Board.EMPTY;
        assertThrows(IllegalArgumentException.class, ()-> boardGame.tileWithId(56));
        assertThrows(IllegalArgumentException.class, ()-> boardGame.tileWithId(12));
        assertThrows(IllegalArgumentException.class, ()-> boardGame.tileWithId(0));
        assertThrows(IllegalArgumentException.class, ()-> boardGame.tileWithId(100));

    }

    @Test
    void tileWithId(){

        PlacedTile firstTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());
        partitions.addTile(otherTile());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertEquals(firstTile, boardGame.tileWithId(56));
        assertEquals(secondTile, boardGame.tileWithId(17));
    }

    @Test
    void occupants(){

        PlacedTile firstTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());
        partitions.addTile(otherTile());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());


        Set<Occupant> occupantSet = new HashSet<>();
        occupantSet.add(new Occupant(Occupant.Kind.PAWN, 560));
        occupantSet.add(new Occupant(Occupant.Kind.PAWN, 170));

        assertEquals(occupantSet, boardGame.occupants());

    }

    @Test
    void insertionPosition(){

        PlacedTile firstTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());
        partitions.addTile(otherTile());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());


        Set<Pos> positions = new HashSet<>();
        positions.add(new Pos(-1,1));
        positions.add(new Pos(-1,-1));
        positions.add(new Pos(-2,0));
        positions.add(new Pos(1,0));
        positions.add(new Pos(0,1));
        positions.add(new Pos(0,-1));


        assertEquals(positions, boardGame.insertionPositions());

    }

    @Test
    void lastPlacedTile(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());

        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertEquals(startTile, boardGame.lastPlacedTile());

        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        partitions.addTile(otherTile());
        partitions.connectSides(startTile.side(Direction.W), secondTile.side(Direction.E));

        int[] ints1 = new int[2];
        ints1[0]=312;
        ints1[1]=311;
        placedTiles[311]=secondTile;
        Board secondBoard = new Board(ints1, placedTiles, partitions.build());

        assertEquals(secondTile, secondBoard.lastPlacedTile());

    }

    @Test
    void allAreaThrows(){


        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());

        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertThrows(IllegalArgumentException.class, () -> boardGame.forestArea(new Zone.Forest(167, Zone.Forest.Kind.PLAIN)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.forestArea(new Zone.Forest(187, Zone.Forest.Kind.PLAIN)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.forestArea(new Zone.Forest(561, Zone.Forest.Kind.PLAIN)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.meadowArea(new Zone.Meadow(167, List.of(), null)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.meadowArea(new Zone.Meadow(560, List.of(), Zone.SpecialPower.PIT_TRAP)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.meadowArea(new Zone.Meadow(981, List.of(), null)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.riverArea(new Zone.River(563, 0, new Zone.Lake(451,1,null))));
        assertThrows(IllegalArgumentException.class, () -> boardGame.riverArea(new Zone.River(733, 0, new Zone.Lake(734,1,null))));
        assertThrows(IllegalArgumentException.class, () -> boardGame.riverSystemArea(new Zone.River(733, 0, new Zone.Lake(734,1,null))));
        assertThrows(IllegalArgumentException.class, () -> boardGame.riverSystemArea(new Zone.River(122, 6, new Zone.Lake(734,1, Zone.SpecialPower.LOGBOAT))));
        assertThrows(IllegalArgumentException.class, () -> boardGame.riverSystemArea(new Zone.Lake(569,0,null)));

    }


    @Test
    void allAreaOk(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());

        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertEquals(partitions.build().forests().areaContaining(new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR)),
                boardGame.forestArea(new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR)));

        assertEquals(partitions.build().meadows().areaContaining(new Zone.Meadow(560, Collections.emptyList(), null)),
                boardGame.meadowArea(new Zone.Meadow(560, Collections.emptyList(), null)));

        assertEquals(partitions.build().rivers().areaContaining(new Zone.River(563, 0, new Zone.Lake(568,1,null))),
                boardGame.riverArea(new Zone.River(563, 0, new Zone.Lake(568,1,null))));

        assertEquals(partitions.build().riverSystems().areaContaining(new Zone.Lake(568,1,null)),
                boardGame.riverSystemArea(new Zone.Lake(568,1,null)));
        assertEquals(partitions.build().riverSystems().areaContaining(new Zone.River(563, 0, new Zone.Lake(568,1,null))),
                boardGame.riverSystemArea(new Zone.River(563, 0, new Zone.Lake(568,1,null))));
    }

    @Test
    void occupantCountOk(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());

        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());


        assertEquals(1, boardGame.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(0, boardGame.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(0, boardGame.occupantCount(PlayerColor.RED, Occupant.Kind.HUT));

        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));
        partitions.addTile(otherTile());
        partitions.connectSides(startTile.side(Direction.W), secondTile.side(Direction.E));
        int[] ints1 = new int[2];
        ints1[0]=312;
        ints1[1]=311;
        placedTiles[311]=secondTile;
        Board secondBoard = new Board(ints1, placedTiles, partitions.build());

        assertEquals(2, secondBoard.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));

        PlacedTile thirdTile = new PlacedTile(otherTile(), PlayerColor.GREEN, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));
        placedTiles[311]=thirdTile;
        Board thirdBoard = new Board(ints1, placedTiles, partitions.build());

        assertEquals(1, thirdBoard.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(0, thirdBoard.occupantCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(0, thirdBoard.occupantCount(PlayerColor.GREEN, Occupant.Kind.HUT));
        assertEquals(1, secondBoard.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));

    }

    @Test
    void insertionPositionWithValidPos(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());

        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        Set<Pos> positionOnTheFirstPos = new HashSet<>();

        positionOnTheFirstPos.add(new Pos(0,1));
        positionOnTheFirstPos.add(new Pos(0,-1));
        positionOnTheFirstPos.add(new Pos(1,0));
        positionOnTheFirstPos.add(new Pos(-1,0));

        assertEquals(positionOnTheFirstPos, boardGame.insertionPositions());

        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        partitions.addTile(otherTile());
        partitions.connectSides(startTile.side(Direction.W), secondTile.side(Direction.E));

        int[] ints1 = new int[2];
        ints1[0]=312;
        ints1[1]=311;
        placedTiles[311]=secondTile;
        Board secondBoard = new Board(ints1, placedTiles, partitions.build());

        Set<Pos> positionOnTheSecondPos = new HashSet<>();


        positionOnTheSecondPos.add(new Pos(0,1));
        positionOnTheSecondPos.add(new Pos(1,0));
        positionOnTheSecondPos.add(new Pos(0,-1));
        positionOnTheSecondPos.add(new Pos(-1,-1));
        positionOnTheSecondPos.add(new Pos(-1,1));
        positionOnTheSecondPos.add(new Pos(-2,0));


        assertEquals(positionOnTheSecondPos, secondBoard.insertionPositions());
    }

    @Test
    void lastPlacedTileWithEmptyBoard(){
        assertNull(Board.EMPTY.lastPlacedTile());
    }

    @Test
    void lastPLacedTileWithBoard(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());

        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertEquals(startTile, boardGame.lastPlacedTile());

        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));
        partitions.addTile(otherTile());
        partitions.connectSides(startTile.side(Direction.W), secondTile.side(Direction.E));
        int[] ints1 = new int[2];
        ints1[0]=312;
        ints1[1]=311;
        placedTiles[311]=secondTile;
        Board secondBoard = new Board(ints1, placedTiles, partitions.build());

        assertEquals(secondTile, secondBoard.lastPlacedTile());

    }

    @Test
    void setAreaOfMeadow(){

        PlacedTile firstTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());
        partitions.addTile(otherTile());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));
        partitions.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Meadow(170, Collections.emptyList(), null));
        partitions.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Meadow(560, Collections.emptyList(), null));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        Set<Area<Zone.Meadow>> areaSet= new HashSet<>();

        areaSet.add(new Area<>(new HashSet<>(List.of(new Zone.Meadow(560, Collections.emptyList(), null), new Zone.Meadow(172, Collections.emptyList(), null))), List.of(PlayerColor.RED), 2));
        areaSet.add(new Area<>(new HashSet<>(List.of(new Zone.Meadow(562, Collections.emptyList(), null), new Zone.Meadow(170, Collections.emptyList(), null))), List.of(PlayerColor.RED), 3));
        areaSet.add(new Area<>(new HashSet<>(List.of(new Zone.Meadow(174, Collections.emptyList(), null))), List.of(), 2));

        assertEquals(areaSet, boardGame.meadowAreas());


    }

    @Test
    void setAreaRiverSystem(){

        PlacedTile firstTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());
        partitions.addTile(otherTile());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));
        partitions.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Meadow(560, Collections.emptyList(), null));
        partitions.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Meadow(170, Collections.emptyList(), null));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        Set<Area<Zone.Water>> waterSet = new HashSet<>();

        waterSet.add(new Area<>(new HashSet<>(List.of(new Zone.River(563, 0, new Zone.Lake(568,1,null)), new Zone.Lake(568,1,null),new Zone.River(171, 0, null))), List.of(), 1));
        waterSet.add(new Area<>(new HashSet<>(List.of(new Zone.River(173, 0, null))), List.of(), 2));

        assertEquals(waterSet, boardGame.riverSystemAreas());


    }

    @Test
    void canAddTileIsOk(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));

        PlacedTile invalidPos = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(2,0), new Occupant(Occupant.Kind.PAWN, 170));
        PlacedTile occupiedPos = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 170));
        PlacedTile canNotConnect = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(1,0), new Occupant(Occupant.Kind.PAWN, 170));
        PlacedTile cantConnect = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,1), new Occupant(Occupant.Kind.PAWN, 170));
        PlacedTile validPlacement = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));
        PlacedTile outOfBoundOneSide = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-11,-12), new Occupant(Occupant.Kind.PAWN, 170));
        PlacedTile outOfBoundTwoSides = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-12,-12), new Occupant(Occupant.Kind.PAWN, 170));

        PlacedTile allSidesTile = new PlacedTile(tileWithAllSides(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));
        PlacedTile allSidesTileForestLeft = new PlacedTile(tileWithAllSides(), PlayerColor.RED, Rotation.HALF_TURN, new Pos(1,0), new Occupant(Occupant.Kind.PAWN, 170));
        PlacedTile allSidesTileRiverRight = new PlacedTile(tileWithAllSides(), PlayerColor.RED, Rotation.LEFT, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        partitions.addTile(firstTile());

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertFalse(boardGame.canAddTile(invalidPos));
        assertFalse(boardGame.canAddTile(occupiedPos));
        assertFalse(boardGame.canAddTile(canNotConnect));
        assertFalse(boardGame.canAddTile(cantConnect));
        assertFalse(boardGame.canAddTile(allSidesTile));
        assertFalse(boardGame.canAddTile(outOfBoundOneSide));
        assertFalse(boardGame.canAddTile(outOfBoundTwoSides));

        assertTrue(boardGame.canAddTile(validPlacement));
        assertTrue(boardGame.canAddTile(allSidesTileForestLeft));
        assertTrue(boardGame.canAddTile(allSidesTileRiverRight));
    }

    @Test
    void canAddTIleWithMultipleSides(){

        PlacedTile firstTile = new PlacedTile(pitTrapTileNW(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        PlacedTile secondTile = new PlacedTile(meadowFromEtoWAndS(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1,0), null);
        PlacedTile thirdTile = new PlacedTile(tileWithAllSides(), PlayerColor.RED, Rotation.RIGHT, new Pos(-2,0), null);
        PlacedTile forthTile = new PlacedTile(firstTile(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1,-1), null);

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(pitTrapTileNW());
        partitions.addTile(meadowFromEtoWAndS());
        partitions.addTile(tileWithAllSides());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));
        partitions.connectSides(secondTile.side(Direction.W), thirdTile.side(Direction.E));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[3];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;
        ints[2]=310;
        placedTiles[310]=thirdTile;
        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertTrue(boardGame.canAddTile(forthTile));
    }

    @Test
    void couldPlaceTileTrueWithAllPossibilities(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.LEFT, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        partitions.addTile(firstTile());

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertTrue(boardGame.couldPlaceTile(otherTile()));
        assertTrue(boardGame.couldPlaceTile(tileWithAllSides()));

    }

    @Test
    void couldPlaceTileFalse(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        PlacedTile startTile = new PlacedTile(fullForestTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        partitions.addTile(firstTile());

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());
        assertFalse(boardGame.couldPlaceTile(otherTile()));
    }


    @Test
    void withOccupantThrows(){
        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        partitions.addTile(firstTile());

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());
        assertThrows(IllegalArgumentException.class, () -> boardGame.withOccupant(new Occupant(Occupant.Kind.PAWN, 560)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.withOccupant(new Occupant(Occupant.Kind.PAWN, 561)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.withOccupant(new Occupant(Occupant.Kind.PAWN, 562)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.withOccupant(new Occupant(Occupant.Kind.PAWN, 563)));
        assertThrows(IllegalArgumentException.class, () -> boardGame.withOccupant(new Occupant(Occupant.Kind.PAWN, 564)));
    }

    @Test
    void withOccupantIsOk(){
        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        partitions.addTile(firstTile());

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGameEmpty = new Board(ints, placedTiles, partitions.build());

        ZonePartitions.Builder partitions1 = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        PlacedTile startTile1 = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        partitions1.addTile(firstTile());
        partitions1.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Meadow(560, Collections.emptyList(), null));

        PlacedTile[] placedTiles1 = new PlacedTile[625];
        int[] ints1 = new int[1];
        ints1[0]=312;
        placedTiles1[312]=startTile1;

        Board boardGame = new Board(ints1, placedTiles1, partitions1.build());

        assertTrue(boardGame.equals(boardGameEmpty.withOccupant(new Occupant(Occupant.Kind.PAWN, 560))));
    }

    @Test
    void withoutOccupantOk(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        PlacedTile startTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        partitions.addTile(firstTile());

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[1];
        ints[0]=312;
        placedTiles[312]=startTile;

        Board boardGameEmpty = new Board(ints, placedTiles, partitions.build());

        ZonePartitions.Builder partitions1 = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        PlacedTile startTile1 = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        partitions1.addTile(firstTile());
        partitions1.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Meadow(560, Collections.emptyList(), null));

        PlacedTile[] placedTiles1 = new PlacedTile[625];
        int[] ints1 = new int[1];
        ints1[0]=312;
        placedTiles1[312]=startTile1;

        Board boardGame = new Board(ints1, placedTiles1, partitions1.build());

        assertTrue(boardGameEmpty.equals(boardGame.withoutOccupant(new Occupant(Occupant.Kind.PAWN, 560))));
        //assertEquals(boardGameEmpty, boardGame.withoutOccupant(new Occupant(Occupant.Kind.PAWN, 560))); //TODO les contenus sont identiques mais les tests ne marchent pas

    }


    @Test
    void withoutForestAndRiver(){

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));
        PlacedTile startTile = new PlacedTile(withOneRiverOnSouth(), PlayerColor.RED, Rotation.NONE, new Pos(0,1), null);
        PlacedTile secondTile = new PlacedTile(withOneRiverOnNorth(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);

        partitions.addTile(withOneRiverOnSouth());
        partitions.addTile(withOneRiverOnNorth());
        partitions.connectSides(startTile.side(Direction.S), secondTile.side(Direction.N));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        ints[1]=287;
        placedTiles[312]=secondTile;
        placedTiles[287]=startTile;

        Board boardGameEmpty = new Board(ints, placedTiles, partitions.build());

        ZonePartitions.Builder occupiedPartition = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        PlacedTile occupiedStartTile = new PlacedTile(withOneRiverOnSouth(), PlayerColor.RED, Rotation.NONE, new Pos(0,1), new Occupant(Occupant.Kind.PAWN, 411));
        PlacedTile occupiedSecondTile = new PlacedTile(withOneRiverOnNorth(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 403));

        occupiedPartition.addTile(withOneRiverOnSouth());
        occupiedPartition.addTile(withOneRiverOnNorth());
        occupiedPartition.connectSides(occupiedStartTile.side(Direction.S), occupiedSecondTile.side(Direction.N));
        occupiedPartition.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.River(411, 0, null));
        occupiedPartition.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Forest(403, Zone.Forest.Kind.WITH_MENHIR));

        PlacedTile[] placedTiles1 = new PlacedTile[625];
        int[] ints1 = new int[2];
        ints1[0]=312;
        ints1[1]=287;
        placedTiles1[312]=occupiedSecondTile;
        placedTiles1[287]=occupiedStartTile;

        Board boardGame = new Board(ints1, placedTiles1, occupiedPartition.build());

        Set<Area<Zone.Forest>> forestArea= new HashSet<>();
        forestArea.add(new Area<>(Set.of(new Zone.Forest(403, Zone.Forest.Kind.WITH_MENHIR)), List.of(PlayerColor.RED), 3));

        Set<Area<Zone.River>> riverArea = new HashSet<>();
        riverArea.add(new Area<>(Set.of(new Zone.River(411, 0, null), new Zone.River(401, 0, null)), List.of(PlayerColor.RED), 0));

        assertTrue(boardGameEmpty.equals(boardGame.withoutGatherersOrFishersIn(forestArea, riverArea)));



    }


    @Test
    void withoutForestAndRiverWithFalseInfo(){
        PlacedTile firstTile = new PlacedTile(firstTileWithoutLake(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(withOneRiverOnNorth(), PlayerColor.RED, Rotation.NONE, new Pos(1,0), new Occupant(Occupant.Kind.PAWN, 403));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTileWithoutLake());
        partitions.addTile(withOneRiverOnNorth());
        partitions.connectSides(firstTile.side(Direction.E), secondTile.side(Direction.W));

        partitions.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Meadow(560, Collections.emptyList(), null));
        partitions.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Forest(403, Zone.Forest.Kind.WITH_MENHIR));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=313;
        placedTiles[313]=secondTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        PlacedTile firstTile1 = new PlacedTile(firstTileWithoutLake(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile1 = new PlacedTile(withOneRiverOnNorth(), PlayerColor.RED, Rotation.NONE, new Pos(1,0), null);

        ZonePartitions.Builder partitions1 = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions1.addTile(firstTileWithoutLake());
        partitions1.addTile(withOneRiverOnNorth());
        partitions1.connectSides(firstTile1.side(Direction.E), secondTile1.side(Direction.W));

        partitions1.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Meadow(560, Collections.emptyList(), null));

        PlacedTile[] placedTiles1 = new PlacedTile[625];
        int[] ints1 = new int[2];
        ints1[0]=312;
        placedTiles1[312]=firstTile1;
        ints1[1]=313;
        placedTiles1[313]=secondTile1;

        Board finalBoard = new Board(ints1, placedTiles1, partitions1.build());


        Set<Area<Zone.Forest>> forestSet = new HashSet<>();
        forestSet.add(new Area<>(Set.of(new Zone.Forest(403, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR)), List.of(PlayerColor.RED), 3));

        assertTrue(finalBoard.equals(boardGame.withoutGatherersOrFishersIn(forestSet, Set.of())));

    }

    @Test
    void withDeadAnimals(){

        PlacedTile firstTile = new PlacedTile(firstTileWithAnimals(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(tileWithAllSidesWithAnimals(), PlayerColor.RED, Rotation.HALF_TURN, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTileWithAnimals());
        partitions.addTile(tileWithAllSidesWithAnimals());
        partitions.connectSides(firstTile.side(Direction.N), secondTile.side(Direction.S));

        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;

        Board boardGame = new Board(ints, placedTiles, partitions.build());

        Set<Animal> cancelledAnimals = new HashSet<>();
        cancelledAnimals.add(new Animal(5602, Animal.Kind.DEER));
        cancelledAnimals.add(new Animal(5621, Animal.Kind.TIGER));
        cancelledAnimals.add(new Animal(1401, Animal.Kind.AUROCHS));

        assertTrue(new Board(cancelledAnimals, ints, placedTiles, partitions.build()).equals(boardGame.withMoreCancelledAnimals(cancelledAnimals)));


    }
    @Test
    void emptySetForEmptyBoard(){
        assertEquals(Set.of(), Board.EMPTY.riversClosedByLastTile());
        assertEquals(Set.of(), Board.EMPTY.forestsClosedByLastTile());
    }

    @Test
    void forestClosedByLastTileWithNoClosing(){

        PlacedTile firstTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(tileWithAllSides(), PlayerColor.RED, Rotation.HALF_TURN, new Pos(1,0), new Occupant(Occupant.Kind.PAWN, 170));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());
        partitions.addTile(tileWithAllSides());
        partitions.connectSides(firstTile.side(Direction.E), secondTile.side(Direction.W));


        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=313;
        placedTiles[313]=secondTile;
        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertEquals(new HashSet<>(Set.of()), boardGame.forestsClosedByLastTile());


    }

    @Test
    void forestClosedWithAClosedOne(){

        PlacedTile tileSideEst = new PlacedTile(tileWithAllSides(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        PlacedTile tileSideWest = new PlacedTile(tileWithAllSidesWithAnimals(), PlayerColor.RED, Rotation.HALF_TURN, new Pos(1,0), null);

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(tileWithAllSidesWithAnimals());
        partitions.addTile(tileWithAllSides());
        partitions.connectSides(tileSideEst.side(Direction.E), tileSideWest.side(Direction.W));


        PlacedTile[] placedTilesClosed = new PlacedTile[625];
        int[] intsClosed = new int[2];
        intsClosed[0]=312;
        placedTilesClosed[312]=tileSideEst;
        intsClosed[1]=313;
        placedTilesClosed[313]=tileSideWest;
        Board boardGameClosed = new Board(intsClosed, placedTilesClosed, partitions.build());

        Set<Area<Zone.Forest>> forestArea = new HashSet<>( Set.of(new Area<>(new HashSet<>(Set.of(new Zone.Forest(141, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(171, Zone.Forest.Kind.WITH_MENHIR))), List.of(), 0)));
        assertEquals(forestArea, boardGameClosed.forestsClosedByLastTile());
    }

    @Test
    void forestClosedWithMoreThanOneForest(){

        PlacedTile tileSideEst = new PlacedTile(tileWithAllSides(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        PlacedTile tileSideWest = new PlacedTile(tileWithAllSidesWithAnimals(), PlayerColor.RED, Rotation.HALF_TURN, new Pos(2,0), null);
        PlacedTile middleTIle = new PlacedTile(forestOnEAndW(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1,0), null);

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(tileWithAllSidesWithAnimals());
        partitions.addTile(tileWithAllSides());
        partitions.addTile(forestOnEAndW());
        partitions.connectSides(tileSideEst.side(Direction.E), middleTIle.side(Direction.W));
        partitions.connectSides(tileSideWest.side(Direction.W), middleTIle.side(Direction.E));


        PlacedTile[] placedTilesClosed = new PlacedTile[625];
        int[] intsClosed = new int[3];
        intsClosed[0]=312;
        placedTilesClosed[312]=tileSideEst;
        intsClosed[2]=313;
        placedTilesClosed[313]=middleTIle;
        intsClosed[1]=314;
        placedTilesClosed[314]=tileSideWest;
        Board boardGameClosed = new Board(intsClosed, placedTilesClosed, partitions.build());

        Set<Area<Zone.Forest>> forestClosed = new HashSet<>();
        forestClosed.add(new Area<>(Set.of(new Zone.Forest(781, Zone.Forest.Kind.WITH_MENHIR),new Zone.Forest(171, Zone.Forest.Kind.WITH_MENHIR)), List.of(), 0));
        forestClosed.add(new Area<>(Set.of(new Zone.Forest(782, Zone.Forest.Kind.WITH_MENHIR),new Zone.Forest(141, Zone.Forest.Kind.WITH_MENHIR)), List.of(), 0));


        assertEquals(forestClosed, boardGameClosed.forestsClosedByLastTile());

    }

    @Test
    void riverClosedWithTransversaleRivers(){

        PlacedTile firstTile = new PlacedTile(withOneRiverOnSouth(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        PlacedTile secondTIle = new PlacedTile(withOneRiverOnNorth(), PlayerColor.RED, Rotation.NONE, new Pos(0,2), null);
        PlacedTile thirdTIle = new PlacedTile(withTranseversaleRiverNS(), PlayerColor.RED, Rotation.NONE, new Pos(0,1), null);

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(withTranseversaleRiverNS());
        partitions.addTile(withOneRiverOnNorth());
        partitions.addTile(withOneRiverOnSouth());
        partitions.connectSides(firstTile.side(Direction.S), thirdTIle.side(Direction.N));
        partitions.connectSides(secondTIle.side(Direction.N), thirdTIle.side(Direction.S));

        PlacedTile[] placedTilesClosed = new PlacedTile[625];
        int[] intsClosed = new int[3];
        intsClosed[0]=312;
        placedTilesClosed[312]=firstTile;
        intsClosed[2]=362;
        placedTilesClosed[362]=secondTIle;
        intsClosed[1]=337;
        placedTilesClosed[337]=thirdTIle;
        Board boardGameClosed = new Board(intsClosed, placedTilesClosed, partitions.build());

        Set<Area<Zone.River>> riverSet = new HashSet<>();
        riverSet.add(new Area<>(Set.of(new Zone.River(411, 0, null),new Zone.River(401, 0, null), new Zone.River(221, 0, null)), List.of(), 0));

        assertEquals(riverSet, boardGameClosed.riversClosedByLastTile());


    }


    @Test
    void riverClosedWithTwoRivers(){

        PlacedTile firstTile = new PlacedTile(withOneRiverOnSouth(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        PlacedTile secondTIle = new PlacedTile(withOneRiverOnNorth(), PlayerColor.RED, Rotation.NONE, new Pos(0,2), null);
        PlacedTile thirdTIle = new PlacedTile(withRiverOnNAndS(), PlayerColor.RED, Rotation.NONE, new Pos(0,1), null);

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(withRiverOnNAndS());
        partitions.addTile(withOneRiverOnNorth());
        partitions.addTile(withOneRiverOnSouth());
        partitions.connectSides(firstTile.side(Direction.S), thirdTIle.side(Direction.N));
        partitions.connectSides(secondTIle.side(Direction.N), thirdTIle.side(Direction.S));

        PlacedTile[] placedTilesClosed = new PlacedTile[625];
        int[] intsClosed = new int[3];
        intsClosed[0]=312;
        placedTilesClosed[312]=firstTile;
        intsClosed[1]=362;
        placedTilesClosed[362]=secondTIle;
        intsClosed[2]=337;
        placedTilesClosed[337]=thirdTIle;
        Board boardGameClosed = new Board(intsClosed, placedTilesClosed, partitions.build());

        Set<Area<Zone.River>> riverSet = new HashSet<>();
        riverSet.add(new Area<>(Set.of(new Zone.River(411, 0, null), new Zone.River(121, 0, null)), List.of(), 0));
        riverSet.add(new Area<>(Set.of(new Zone.River(401, 0, null), new Zone.River(122, 0, null)), List.of(), 0));

        assertEquals(riverSet, boardGameClosed.riversClosedByLastTile());


    }
    @Test
    void riverClosedWithNoRiverClosed(){

        PlacedTile firstTile = new PlacedTile(firstTile(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), new Occupant(Occupant.Kind.PAWN, 560));
        PlacedTile secondTile = new PlacedTile(otherTile(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0), new Occupant(Occupant.Kind.PAWN, 170));

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(firstTile());
        partitions.addTile(otherTile());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));


        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=313;
        placedTiles[313]=secondTile;
        Board boardGame = new Board(ints, placedTiles, partitions.build());

        assertEquals(new HashSet<>(Set.of()), boardGame.riversClosedByLastTile());

    }

    @Test
    void riverClosedByLastTileWithClosedRiver(){

        PlacedTile firstTile = new PlacedTile(withOneRiverOnNorth(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        PlacedTile secondTile = new PlacedTile(withOneRiverOnSouth(), PlayerColor.RED, Rotation.NONE, new Pos(0,1),null);

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(withOneRiverOnNorth());
        partitions.addTile(withOneRiverOnSouth());
        partitions.connectSides(firstTile.side(Direction.N), secondTile.side(Direction.S));


        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[2];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=287;
        placedTiles[287]=secondTile;
        Board boardGame = new Board(ints, placedTiles, partitions.build());
        Set<Area<Zone.River>> areas = Set.of( new Area<> (new HashSet<>(Set.of(new Zone.River(401, 0, null), new Zone.River(411, 0, null))), List.of(), 0));

        assertEquals(areas, boardGame.riversClosedByLastTile());

    }

    @Test
    void adjacentMeadow(){ //TODO si j'ai du temps complexifier les test de cette methode

        PlacedTile firstTile = new PlacedTile(pitTrapTileNW(), PlayerColor.RED, Rotation.NONE, new Pos(0,0), null);
        PlacedTile secondTile = new PlacedTile(meadowFromEtoWAndS(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1,0), null);
        PlacedTile thirdTile = new PlacedTile(tileWithAllSides(), PlayerColor.RED, Rotation.RIGHT, new Pos(-2,0), null);
        PlacedTile forthTile = new PlacedTile(firstTile(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1,1), null);

        ZonePartitions.Builder partitions = new ZonePartitions.Builder(new ZonePartitions( new ZonePartition<>() , new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>()));

        partitions.addTile(pitTrapTileNW());
        partitions.addTile(firstTile());
        partitions.addTile(meadowFromEtoWAndS());
        partitions.addTile(tileWithAllSides());
        partitions.connectSides(firstTile.side(Direction.W), secondTile.side(Direction.E));
        partitions.connectSides(secondTile.side(Direction.W), thirdTile.side(Direction.E));
        partitions.connectSides(secondTile.side(Direction.S), forthTile.side(Direction.N));



        PlacedTile[] placedTiles = new PlacedTile[625];
        int[] ints = new int[4];
        ints[0]=312;
        placedTiles[312]=firstTile;
        ints[1]=311;
        placedTiles[311]=secondTile;
        ints[2]=310;
        placedTiles[310]=thirdTile;
        ints[3]=336;
        placedTiles[336]=forthTile;
        Board boardGame = new Board(ints, placedTiles, partitions.build());

        Area<Zone.Meadow> meadowArea = new Area<>(Set.of(new Zone.Meadow(230, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP), new Zone.Meadow(101, Collections.emptyList(), null), new Zone.Meadow(140, Collections.emptyList(), null), new Zone.Meadow(560, Collections.emptyList(), null)),
                List.of(), 1);

        assertEquals(meadowArea, boardGame.adjacentMeadow(new Pos(0,0),new Zone.Meadow(230, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP)));
    }

}