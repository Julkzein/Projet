package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HisZonePartitionsTest {
    private final Zone.Forest forest0 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest3 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest4 = new Zone.Forest(4, Zone.Forest.Kind.PLAIN);
    private final Zone.Meadow meadow4 = new Zone.Meadow(4, new ArrayList<>(), null);
    private final Zone.Meadow meadow5 = new Zone.Meadow(5, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP);
    private final Zone.Meadow meadow6 = new Zone.Meadow(6, new ArrayList<>(), null);
    private final Zone.Meadow meadow7 = new Zone.Meadow(7, new ArrayList<>(), null);
    private final Zone.Meadow meadow8 = new Zone.Meadow(8, new ArrayList<>(), null);
    private final Zone.Lake lake8 = new Zone.Lake(8, 987654321, Zone.SpecialPower.LOGBOAT);
    private final Zone.Lake lake9 = new Zone.Lake(9, 987654321, Zone.SpecialPower.LOGBOAT);
    private final Zone.River river3 = new Zone.River(3, 123456789, lake8);
    private final Zone.River river4 = new Zone.River(4, 123456789, lake9);
    private final Zone.River river5 = new Zone.River(5, 123453333, null);
    private final TileSide[] sides = new TileSide[] {
            new TileSide.Forest(forest0), new TileSide.Forest(forest1), new TileSide.Forest(forest2),
            new TileSide.River(meadow4, river3, meadow5),
    };
    private final TileSide[] sides2 = new TileSide[] {
            new TileSide.Forest(forest3), new TileSide.Forest(forest4), new TileSide.Meadow(meadow6),
            new TileSide.River(meadow7, river4, meadow8)
    };
    private final Tile tile = new Tile(21, Tile.Kind.NORMAL, sides[0], sides[1], sides[2], sides[3]);
    private final Tile tile2 = new Tile(21, Tile.Kind.NORMAL, sides2[0], sides2[1], sides2[2], sides2[3]);

    @Test
    void addTileIsCorrectlyDefined() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);
        builder.addTile(tile2);
        ZonePartitions zonePartitions = builder.build();

        assertEquals(5, zonePartitions.forests().areas().size());
        assertEquals(5, zonePartitions.meadows().areas().size());
        assertEquals(2, zonePartitions.rivers().areas().size());
        assertEquals(2, zonePartitions.riverSystems().areas().size());

        // todo add more tests
        assertEquals(zonePartitions.riverSystems().areas().size(), zonePartitions.rivers().areas().size());
    }

    @Test
    void connectSidesIsCorrectlyDefined() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);
        builder.connectSides(sides[0], sides[1]);
        ZonePartitions zonePartitions = builder.build();
        assertEquals(2, zonePartitions.forests().areas().size());
        assertThrows(IllegalArgumentException.class, () -> builder.connectSides(sides[0], sides[3]));
    }

    @Test
    void addInitialOccupantIsCorrectlyDefined() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);
        builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, forest0);
        ZonePartitions zonePartitions = builder.build();
        for (Area<Zone.Forest> area : zonePartitions.forests().areas()) {
            if (area.zones().contains(forest0)) {
                assertEquals(1, area.occupants().size());
            }
        }
        assertThrows(IllegalArgumentException.class, () -> builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, lake8));
    }

    @Test
    void removePawnIsCorrectlyDefined() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);
        builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, forest0);
        ZonePartitions zonePartitions = builder.build();
        for (Area<Zone.Forest> area : zonePartitions.forests().areas()) {
            if (area.zones().contains(forest0)) {
                assertEquals(1, area.occupants().size());
            }
        }
        builder.removePawn(PlayerColor.RED, forest0);
        zonePartitions = builder.build();
        for (Area<Zone.Forest> area : zonePartitions.forests().areas()) {
            if (area.zones().contains(forest0)) {
                assertEquals(0, area.occupants().size());
            }
        }
        assertThrows(IllegalArgumentException.class, () -> builder.removePawn(PlayerColor.RED, lake8));
    }

    @Test
    void clearGatherersIsCorrectlyDefined() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);
        builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, forest0);
        builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, forest1);
        builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, forest2);
        ZonePartitions zonePartitions = builder.build();
        for (Area<Zone.Forest> area : zonePartitions.forests().areas()) {
            builder.clearGatherers(area);
        }
        zonePartitions = builder.build();
        for (Area<Zone.Forest> area : zonePartitions.forests().areas()) {
            assertEquals(0, area.occupants().size());
        }
    }

    @Test
    void clearFishersIsCorrectlyDefined() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);
        builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, river3);
        ZonePartitions zonePartitions = builder.build();
        for (Area<Zone.River> area : zonePartitions.rivers().areas()) {
            builder.clearFishers(area);
        }
        zonePartitions = builder.build();
        for (Area<Zone.River> area : zonePartitions.rivers().areas()) {
            assertEquals(0, area.occupants().size());
        }
    }
}