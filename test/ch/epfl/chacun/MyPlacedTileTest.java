package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static ch.epfl.chacun.Occupant.Kind.HUT;
import static ch.epfl.chacun.Occupant.Kind.PAWN;
import static org.junit.jupiter.api.Assertions.*;

public class MyPlacedTileTest {

    @Test
    void placedTileWithNullTile(){
        assertThrows(IllegalArgumentException.class, ()-> new PlacedTile(null, PlayerColor.RED, Rotation.NONE, new Pos(2, 2), null));
    }

    @Test
    void placedTileWithNullRotation(){
        assertThrows(IllegalArgumentException.class, ()-> new PlacedTile(new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null))), PlayerColor.RED, null, new Pos(2, 2), null));
    }

    @Test
    void placedTileWithNullPos(){
        assertThrows(IllegalArgumentException.class, ()-> new PlacedTile(new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null))), PlayerColor.RED, Rotation.NONE, null, null));
    }

    public PlacedTile pt() {
        return new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                        new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null)))
                ,
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), null);
    }

    public PlacedTile ptWithOccupantPawn() {
        return new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                        new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null))),
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), new Occupant(PAWN, 1));
    }
    public PlacedTile ptWithOccupantHut() {
        return new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                        new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null))),
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), new Occupant(HUT, 1));
    }


    public PlacedTile pt(Rotation rotation) {
        return new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                        new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null))),
                PlayerColor.RED, rotation, new Pos(2, 2), null);
    }

    public PlacedTile ptNoPower() {
        return new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), null)),
                        new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null))),
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), null);
    }

    public PlacedTile startPt() {
        return new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                        new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null)))
                ,
                null, Rotation.NONE, new Pos(0, 0), null);
    }

    public PlacedTile ptWithLake() {
        return new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                        new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, new Zone.Lake(8, 0, null)), new Zone.Meadow(6, new ArrayList<>(), null)))
                ,
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), null);
    }

    @Test
    void testId() {
        assertEquals(78, pt().id());
    }

    @Test
    void tileKindTest() {
        assertEquals(Tile.Kind.NORMAL, pt().kind());
    }

    @Test
    void tileSideNoRotationN() {
        assertEquals(new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), pt().side(Direction.N));
    }

    @Test
    void tileSideRotationLeftN() {
        assertEquals(new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)), pt(Rotation.LEFT).side(Direction.N));
    }

    @Test
    void tileSideRotationRightN() {
        assertEquals(new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null)), pt(Rotation.RIGHT).side(Direction.N));
    }

    @Test
    void tileSideRotationHalfTurnN() {
        assertEquals(new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)), pt(Rotation.HALF_TURN).side(Direction.N));
    }

    @Test
    void tileSideRotationHalfTurnS() {
        assertEquals(new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), pt(Rotation.HALF_TURN).side(Direction.S));
    }


    @Test
    void returnsTileZoneWithGivenId() {
        assertEquals(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), pt().zoneWithId(1));
        assertEquals(new Zone.Meadow(2, new ArrayList<>(), null), pt().zoneWithId(2));
        assertEquals(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN), pt().zoneWithId(3));
        assertEquals(new Zone.Meadow(7, new ArrayList<>(), null), pt().zoneWithId(7));
        assertEquals(new Zone.River(4, 2, null), pt().zoneWithId(4));
        assertEquals(new Zone.Meadow(6, new ArrayList<>(), null), pt().zoneWithId(6));
    }

    @Test
    void throwsExceptionWhenNoSuchId() {
        assertThrows(IllegalArgumentException.class, ()-> pt().zoneWithId(5));
        assertThrows(IllegalArgumentException.class, ()-> pt().zoneWithId(8));
        assertThrows(IllegalArgumentException.class, ()-> pt().zoneWithId(-1));
    }

    @Test
    void specialPowerTestNoPower() {
        assertNull(ptNoPower().specialPowerZone());
    }

    @Test
    void specialPowerTest() {
        assertEquals(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN), pt().specialPowerZone());
    }

    @Test
    void forestZonesWithOneForest() {
        Set<Zone.Forest> zoneForest = new HashSet<>();
        zoneForest.add(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        assertEquals(zoneForest, pt().forestZones());
    }

    @Test
    void forestZonesWithTwoForest() {
        PlacedTile ptTwoForest =  new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), null)),
                        new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null))),
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), null);

        Set<Zone.Forest> zoneForest = new HashSet<>();
        zoneForest.add(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        zoneForest.add(new Zone.Forest(4, Zone.Forest.Kind.PLAIN));
        assertEquals(zoneForest, ptTwoForest.forestZones());
    }

    @Test
    void forestZonesWithNoForest() {
        Set<Zone.Forest> zoneForest = new HashSet<>();
        assertEquals(zoneForest, new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), null)),
                        new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null))),
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), null).forestZones());
    }

    @Test
    void meadowZonesWithTwoMeadow() {
        Set<Zone.Meadow> zoneMeadow = new HashSet<>();
        zoneMeadow.add(new Zone.Meadow(2, new ArrayList<>(), null));
        zoneMeadow.add(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN));
        assertEquals(zoneMeadow, new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN))),
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), null).meadowZones());
    }

    @Test
    void meadowZonesWithOneMeadow() {
        Set<Zone.Meadow> zoneMeadow = new HashSet<>();
        zoneMeadow.add(new Zone.Meadow(2, new ArrayList<>(), null));
        assertEquals(zoneMeadow, new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN))),
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), null).meadowZones());
    }

    @Test
    void meadowZonesWithNoMeadow() {
        Set<Zone.Meadow> zoneMeadow = new HashSet<>();
        assertEquals(zoneMeadow, new PlacedTile(
                new Tile(78, Tile.Kind.NORMAL,
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                        new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN))),
                PlayerColor.RED, Rotation.NONE, new Pos(2, 2), null).meadowZones());
    }

    @Test
    void returnsNullWhenStartTile() {
        assertNull(startPt().potentialOccupants());
    }

    @Test
    void returnsPotentialOccupant() {
        assertEquals(Set.of(new Occupant(PAWN, 1), new Occupant(PAWN, 2), new Occupant(PAWN, 3), new Occupant(PAWN, 4), new Occupant(PAWN, 6), new Occupant(PAWN, 7), new Occupant(HUT, 8)), ptWithLake().potentialOccupants());
        //assertEquals(new Occupant(HUT, 1), ptWithOccupantHut().occupant());
    }

    @Test
    void returnsMultipleOccupant() {
        PlacedTile tile = ptWithOccupantPawn();
        tile.withOccupant(new Occupant(HUT, 2));
        assertEquals(Set.of(new Occupant(PAWN, 1),new Occupant(HUT, 2)) , tile.occupant());
    }
}
