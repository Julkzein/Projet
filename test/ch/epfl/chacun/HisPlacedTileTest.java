package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HisPlacedTileTest {
    private final Zone.Forest forest0 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
    private final Zone.Meadow meadow4 = new Zone.Meadow(4, new ArrayList<>(), null);
    private final Zone.Meadow meadow5 = new Zone.Meadow(5, new ArrayList<>(), null);
    private final Zone.Lake lake8 = new Zone.Lake(8, 987654321, Zone.SpecialPower.LOGBOAT);
    private final Zone.River river3 = new Zone.River(3, 123456789, lake8);
    private final TileSide[] sides = new TileSide[] {
            new TileSide.Forest(forest0), new TileSide.Forest(forest1), new TileSide.Forest(forest2),
            new TileSide.River(meadow4, river3, meadow5),
    };
    private final Tile tile = new Tile(21, Tile.Kind.NORMAL, sides[0], sides[1], sides[2], sides[3]);
    private final PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0), new Occupant(Occupant.Kind.PAWN, tile.id()));

    @Test
    void idIsCorrectlyDefined() {
        final int expected = 21;
        assertEquals(expected, placedTile.id());
    }

    @Test
    void kindIsCorrectlyDefined() {
        assertEquals(Tile.Kind.NORMAL, placedTile.kind());
    }

    @Test
    void sideIsCorrectlyDefined() {
        assertEquals(sides[0], placedTile.side(Direction.N));
        assertEquals(sides[1], placedTile.side(Direction.E));
        assertEquals(sides[2], placedTile.side(Direction.S));
        assertEquals(sides[3], placedTile.side(Direction.W));
    }

    @Test
    void zoneWithIdIsCorrectlyDefined() {
        assertEquals(0, placedTile.zoneWithId(0).id());
        assertEquals(1, placedTile.zoneWithId(1).id());
        assertEquals(2, placedTile.zoneWithId(2).id());
        assertEquals(3, placedTile.zoneWithId(3).id());
    }

    @Test
    void specialPowerZoneIsCorrectlyDefined() {
        assertEquals(lake8, placedTile.specialPowerZone());
    }

    @Test
    void forestZonesIsCorrectlyDefined() {
        Set<Zone.Forest> expected = new HashSet<>();
        expected.add(forest0);
        expected.add(forest1);
        expected.add(forest2);
        assertEquals(expected, placedTile.forestZones());
    }

    @Test
    void meadowZonesIsCorrectlyDefined() {
        Set<Zone.Meadow> expected = new HashSet<>();
        expected.add(meadow4);
        expected.add(meadow5);
        assertEquals(expected, placedTile.meadowZones());
    }

    @Test
    void riverZonesIsCorrectlyDefined() {
        Set<Zone.River> expected = new HashSet<>();
        expected.add(river3);
        assertEquals(expected, placedTile.riverZones());
    }

    @Test
    void potentialOccupantsIsCorrectlyDefined() {
        Set<Occupant> expected = new HashSet<>();
        expected.add(new Occupant(Occupant.Kind.PAWN, 0));
        expected.add(new Occupant(Occupant.Kind.PAWN, 1));
        expected.add(new Occupant(Occupant.Kind.PAWN, 2));
        expected.add(new Occupant(Occupant.Kind.PAWN, 3));
        expected.add(new Occupant(Occupant.Kind.PAWN, 4));
        expected.add(new Occupant(Occupant.Kind.PAWN, 5));
        expected.add(new Occupant(Occupant.Kind.HUT, 8));
        assertEquals(expected, placedTile.potentialOccupants());
    }

    @Test
    void withOccupantIsCorrectlyDefined() {
        Occupant occupant = new Occupant(Occupant.Kind.PAWN, tile.id());
        assertEquals(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0), null).withOccupant(occupant), placedTile);
    }

    @Test
    void mabite() {
        assertThrows(IllegalArgumentException.class, () -> placedTile.withOccupant(null));
    }

    @Test
    void withNoOccupantIsCorrectlyDefined() {
        assertEquals(new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0), null), placedTile.withNoOccupant());
    }

    @Test
    void idOfZoneOccupiedByIsCorrectlyDefined() {
        assertEquals(tile.id(), placedTile.idOfZoneOccupiedBy(Occupant.Kind.PAWN));
    }

    @Test
    void tileIsCorrectlyDefined() {
        assertEquals(tile, placedTile.tile());
    }

    @Test
    void placerIsCorrectlyDefined() {
        assertEquals(PlayerColor.RED, placedTile.placer());
    }

    @Test
    void rotationIsCorrectlyDefined() {
        assertEquals(Rotation.NONE, placedTile.rotation());
    }

    @Test
    void posIsCorrectlyDefined() {
        assertEquals(new Pos(0, 0), placedTile.pos());
    }

    @Test
    void occupantIsCorrectlyDefined() {
        assertEquals(new Occupant(Occupant.Kind.PAWN, tile.id()), placedTile.occupant());
    }
}