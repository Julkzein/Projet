package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyGameStateTest {

    // Help for constructing the test cases
    private final Zone.Meadow meadow1 = new Zone.Meadow(374, List.of(), null);
    private final Zone.Meadow meadow2 = new Zone.Meadow(231, List.of(), null);
    private final Zone.Meadow meadow3 = new Zone.Meadow(768, List.of(new Animal(7680, Animal.Kind.DEER), new Animal(7681, Animal.Kind.DEER)), null);
    private final Zone.Meadow meadow4 = new Zone.Meadow(180, List.of(new Animal(1800, Animal.Kind.TIGER)), null);
    private final Zone.Meadow meadow5 = new Zone.Meadow(542, List.of(new Animal(5420, Animal.Kind.TIGER), new Animal(5421, Animal.Kind.DEER)), null);
    private final Zone.Meadow meadow6 = new Zone.Meadow(952, List.of(new Animal(9520, Animal.Kind.AUROCHS), new Animal(9521, Animal.Kind.TIGER)), null);
    private final Zone.Meadow meadowWithHuntingTrap = new Zone.Meadow(123, List.of(), Zone.SpecialPower.HUNTING_TRAP);
    private final Zone.Meadow meadowWithPitTrap = new Zone.Meadow(850, List.of(), Zone.SpecialPower.PIT_TRAP);
    private final Zone.Meadow meadowWithShaman = new Zone.Meadow(228, List.of(), Zone.SpecialPower.SHAMAN);
    private final Zone.Meadow meadowWithWildFire = new Zone.Meadow(607, List.of(), Zone.SpecialPower.WILD_FIRE);
    private final Zone.Forest forest1 = new Zone.Forest(468, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest2 = new Zone.Forest(212, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest3 = new Zone.Forest(329, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest4 = new Zone.Forest(791, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest5 = new Zone.Forest(907, Zone.Forest.Kind.WITH_MENHIR);
    private final Zone.Forest forest6 = new Zone.Forest(568, Zone.Forest.Kind.WITH_MUSHROOMS);
    private final Zone.Lake lake1 = new Zone.Lake(427, 2, null);
    private final Zone.Lake lake2 = new Zone.Lake(437, 1, null);
    private final Zone.Lake lakeWithLogboat = new Zone.Lake(317, 0, Zone.SpecialPower.LOGBOAT);
    private final Zone.Lake lakeWithRaft = new Zone.Lake(878, 2, Zone.SpecialPower.RAFT);
    private final Zone.River river1 = new Zone.River(172, 0, null);
    private final Zone.River river2 = new Zone.River(174, 0, null);
    private final Zone.River river3 = new Zone.River(564, 0, null);
    private final Zone.River river4 = new Zone.River(426, 0, null);
    private final Zone.River riverWithLake1 = new Zone.River(272, 0, lake1);
    private final Zone.River riverWithLake2 = new Zone.River(706, 0, lake2);
    private final Zone.River riverWithLakeWithLogboat1 = new Zone.River(716, 0, lakeWithLogboat);
    private final Zone.River riverWithLakeWithLogboat2 = new Zone.River(717, 0, lakeWithLogboat);
    private final Zone.River riverWithLakeWithRaft = new Zone.River(726, 0, lakeWithRaft);
    private final TileSide[] sides1 = new TileSide[]{
            new TileSide.Meadow(meadow1), new TileSide.Meadow(meadow1),
            new TileSide.Forest(forest1), new TileSide.Forest(forest1)};

    private final TileSide[] sides2 = new TileSide[]{
            new TileSide.Meadow(meadow3), new TileSide.River(meadow3, river1, meadow4),
            new TileSide.River(meadow4, river1, meadow3), new TileSide.Meadow(meadow3)};

    private final TileSide[] sides3 = new TileSide[]{
            new TileSide.River(meadow1, riverWithLake1, meadow2), new TileSide.Meadow(meadow2),
            new TileSide.Forest(forest1), new TileSide.Forest(forest1)};


    private final TileSide[] sides4 = new TileSide[]{
            new TileSide.Forest(forest5), new TileSide.Forest(forest5),
            new TileSide.Forest(forest5), new TileSide.Forest(forest5)};

    private final TileSide[] sides5 = new TileSide[]{
            new TileSide.Meadow(meadowWithHuntingTrap), new TileSide.Forest(forest3),
            new TileSide.Meadow(meadowWithHuntingTrap), new TileSide.Meadow(meadowWithHuntingTrap)};

    private final TileSide[] sides6 = new TileSide[]{
            new TileSide.River(meadow5, riverWithLakeWithLogboat1, meadow6), new TileSide.Meadow(meadow6),
            new TileSide.Meadow(meadow6), new TileSide.River(meadow6, riverWithLakeWithLogboat2, meadow5)};


    @Test
    public void gameStateConstructorCorrectlyDefined() {
        //assertThrows(IllegalArgumentException.class, () -> new GameState(List.of(), ));
    }
}
