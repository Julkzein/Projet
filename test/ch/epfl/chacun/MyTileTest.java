package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class MyTileTest {

    @Test
    void returnsFourPossibleSidesInOrder() {
        Tile tile = new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), new Zone.SpecialPower())),
                new TileSide.Meadow(new Zone.Meadow(3, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(new Zone.River(4, Zone.Forest.Kind.PLAIN))

    }


}
