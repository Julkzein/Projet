package ch.epfl.chacun;
import org.junit.jupiter.api.Test;

import ch.epfl.chacun.Zone;

import static org.junit.jupiter.api.Assertions.*;

public class MyTileSideTest {
    @Test
    void isSameKindAsForestTest() {
        TileSide forest1 = new TileSide.Forest(new Zone.Forest(10, Zone.Forest.Kind.PLAIN));
        TileSide forest2 = new TileSide.Forest(new Zone.Forest(20, Zone.Forest.Kind.WITH_MENHIR));
        assertTrue(forest1.isSameKindAs(forest2));
    }

    @Test
    void isSameKindAsRiverTest() {
        TileSide river1 = new TileSide.River(new Zone.Meadow(10, null, null), new Zone.River(20, 2, new Zone.Lake(20, 1, null)), new Zone.Meadow(30, null, null));
        TileSide river2 = new TileSide.River(new Zone.Meadow(30, null, null), new Zone.River(60, 2, new Zone.Lake(20, 1, null)), new Zone.Meadow(30, null, null));
        assertTrue(river1.isSameKindAs(river2));
    }

    @Test
    void isSameKindAsMeadowTest() {
        TileSide meadow1 = new TileSide.Meadow(new Zone.Meadow(10, null, null));
        TileSide meadow2 = new TileSide.Meadow(new Zone.Meadow(20, null, null));
        assertTrue(meadow1.isSameKindAs(meadow2));
    }

    @Test
    void isSameKindAsDifferentMeadowRiverTest() {
        TileSide meadow = new TileSide.Meadow(new Zone.Meadow(10, null, null));
        TileSide river = new TileSide.River(new Zone.Meadow(10, null, null), new Zone.River(20, 2, new Zone.Lake(20, 1, null)), new Zone.Meadow(30, null, null));
        assertFalse(meadow.isSameKindAs(river));
    }

    @Test
    void isSameKindAsDifferentForestMeadowTest() {
        TileSide meadow = new TileSide.Meadow(new Zone.Meadow(10, null, null));
        TileSide forest = new TileSide.Forest(new Zone.Forest(10, Zone.Forest.Kind.PLAIN));
        assertFalse(meadow.isSameKindAs(forest));
    }

    @Test
    void isSameKindAsDifferentForestRiverTest() {
        TileSide river = new TileSide.River(new Zone.Meadow(10, null, null), new Zone.River(20, 2, new Zone.Lake(20, 1, null)), new Zone.Meadow(30, null, null));
        TileSide forest = new TileSide.Forest(new Zone.Forest(10, Zone.Forest.Kind.PLAIN));
        assertFalse(river.isSameKindAs(forest));
    }





}
