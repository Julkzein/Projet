package ch.epfl.chacun.etape2;

import ch.epfl.chacun.Tile;
import ch.epfl.chacun.TileSide;
import ch.epfl.chacun.Zone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyTileTest {
    private final Zone.Forest forest0 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
    private final Zone.Meadow meadow4 = new Zone.Meadow(4, new ArrayList<>(), null);
    private final Zone.Meadow meadow5 = new Zone.Meadow(5, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP);
    private final Zone.Lake lake8 = new Zone.Lake(8, 987654321, Zone.SpecialPower.LOGBOAT);
    private final Zone.River river3 = new Zone.River(3, 123456789, lake8);
    private final TileSide[] sides = new TileSide[] {
            new TileSide.Forest(forest0), new TileSide.Forest(forest1), new TileSide.Forest(forest2),
            new TileSide.River(meadow4, river3, meadow5),
    };
    private final Tile tile = new Tile(21, Tile.Kind.NORMAL, sides[0], sides[1], sides[2], sides[3]);

    @Test
    void sidesIsCorrectlyDefined() {
        assertEquals(sides[0], tile.sides().get(0));
        assertEquals(sides[1], tile.sides().get(1));
        assertEquals(sides[2], tile.sides().get(2));
        assertEquals(sides[3], tile.sides().get(3));
    }

    @Test
    void sideZonesIsCorrectlyDefined() {
        assertEquals(new HashSet<Zone>(List.of(forest0, forest1, forest2, river3, meadow4, meadow5)), tile.sideZones());
    }

    @Test
    void zonesIsCorrectlyDefined() {
        assertEquals(new HashSet<Zone>(List.of(forest0, forest1, forest2, river3, meadow4, meadow5, lake8)), tile.zones());
    }

    @Test
    void idIsCorrectlyDefined() {
        assertEquals(21, tile.id());
    }

    @Test
    void kindIsCorrectlyDefined() {
        assertEquals(Tile.Kind.NORMAL, tile.kind());
    }

    @Test
    void nIsCorrectlyDefined() {
        assertEquals(sides[0], tile.n());
    }

    @Test
    void eIsCorrectlyDefined() {
        assertEquals(sides[1], tile.e());
    }

    @Test
    void sIsCorrectlyDefined() {
        assertEquals(sides[2], tile.s());
    }

    @Test
    void wIsCorrectlyDefined() {
        assertEquals(sides[3], tile.w());
    }
}