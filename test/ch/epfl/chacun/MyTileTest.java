package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyTileTest {

    @Test
    void returnsFourPossibleSidesInOrder() {
        Tile tile = new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(7, new ArrayList<>(), null), new Zone.River(4, 2, null), new Zone.Meadow(6, new ArrayList<>(), null)));
        assertEquals(List.of(tile.n(), tile.e(), tile.s(), tile.w()), tile.sides());
    }

    @Test
    void returnsSideZones() {
        Zone.Lake lake = new Zone.Lake(4, 2, null);
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow2 = new Zone.Meadow(2, new ArrayList<>(), null);
        Zone.River river3 = new Zone.River(3, 2, lake);
        Zone.Meadow meadow4 = new Zone.Meadow(4, new ArrayList<>(), Zone.SpecialPower.SHAMAN);
        Zone.River river5 = new Zone.River(5, 2, lake);
        Zone.Meadow meadow6 = new Zone.Meadow(6, new ArrayList<>(), null);

        Tile tile = new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(forest1),
                new TileSide.Meadow(meadow2),
                new TileSide.River(meadow2, river3, meadow4),
                new TileSide.River(meadow4, river5, meadow6));

        assertEquals(tile.sideZones(), Set.of(forest1, meadow2, river3, meadow4, river5, meadow6));
    }

    @Test
    void returnsZones() {
        Zone.Lake lake = new Zone.Lake(8, 2, null);
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow2 = new Zone.Meadow(2, new ArrayList<>(), null);
        Zone.River river3 = new Zone.River(3, 2, lake);
        Zone.Meadow meadow4 = new Zone.Meadow(4, new ArrayList<>(), Zone.SpecialPower.SHAMAN);
        Zone.River river5 = new Zone.River(5, 2, lake);
        Zone.Meadow meadow6 = new Zone.Meadow(6, new ArrayList<>(), null);

        Tile tile = new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(forest1),
                new TileSide.Meadow(meadow2),
                new TileSide.River(meadow2, river3, meadow4),
                new TileSide.River(meadow4, river5, meadow6));

        assertEquals(tile.zones(), Set.of(meadow2, forest1, river3, lake, meadow4, river5, meadow6));
    }

    @Test
    void returnsZonesWithoutLake() {
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow2 = new Zone.Meadow(2, new ArrayList<>(), null);
        Zone.River river3 = new Zone.River(3, 2, null);
        Zone.Meadow meadow4 = new Zone.Meadow(4, new ArrayList<>(), Zone.SpecialPower.SHAMAN);
        Zone.River river5 = new Zone.River(5, 2, null);
        Zone.Meadow meadow6 = new Zone.Meadow(6, new ArrayList<>(), null);

        Tile tile = new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(forest1),
                new TileSide.Meadow(meadow2),
                new TileSide.River(meadow2, river3, meadow4),
                new TileSide.River(meadow4, river5, meadow6));

        assertEquals(tile.zones(), Set.of(meadow2, forest1, river3, meadow4, river5, meadow6));
    }

    @Test
    void returnsUniqueLakeWhenTwoRivers() {
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow2 = new Zone.Meadow(2, new ArrayList<>(), null);
        Zone.Lake lake = new Zone.Lake(4, 2, null);
        Zone.River river3 = new Zone.River(3, 2, lake);
        Zone.Meadow meadow4 = new Zone.Meadow(4, new ArrayList<>(), Zone.SpecialPower.SHAMAN);
        Zone.River river5 = new Zone.River(5, 2, lake);
        Zone.Meadow meadow6 = new Zone.Meadow(6, new ArrayList<>(), null);

        Tile tile = new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(forest1),
                new TileSide.Meadow(meadow2),
                new TileSide.River(meadow2, river3, meadow4),
                new TileSide.River(meadow4, river5, meadow6));

        assertEquals(Set.of(meadow2, forest1, river3, lake, meadow4, river5, meadow6), tile.zones());
    }

    @Test
    void returnsTwoLakesWhenTwoRivers() {
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow2 = new Zone.Meadow(2, new ArrayList<>(), null);
        Zone.Lake lake = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 3, null);
        Zone.River river3 = new Zone.River(3, 2, lake);
        Zone.Meadow meadow4 = new Zone.Meadow(4, new ArrayList<>(), Zone.SpecialPower.SHAMAN);
        Zone.River river5 = new Zone.River(5, 2, lake2);
        Zone.Meadow meadow6 = new Zone.Meadow(6, new ArrayList<>(), null);

        Tile tile = new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(forest1),
                new TileSide.Meadow(meadow2),
                new TileSide.River(meadow2, river3, meadow4),
                new TileSide.River(meadow4, river5, meadow6));

        assertEquals(Set.of(meadow2, forest1, river3, lake, lake2, meadow4, river5, meadow6), tile.zones());
    }
}
