package ch.epfl.chacun.etape3;

import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import ch.epfl.chacun.ZonePartition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HisZonePartitionTest {
    private final Zone.Forest forest0 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
    private final Zone.Meadow meadow4 = new Zone.Meadow(4, new ArrayList<>(), null);
    private final Zone.Meadow meadow5 = new Zone.Meadow(5, new ArrayList<>(), null);
    private final Zone.Lake lake8 = new Zone.Lake(8, 987654321, Zone.SpecialPower.LOGBOAT);
    private final Zone.River river3 = new Zone.River(3, 123456789, lake8);

    private final ZonePartition<Zone> zonePartition = new ZonePartition<>(Set.of(
            new Area<>(Set.of(forest0, forest1, forest2), List.of(PlayerColor.RED), 0),
            new Area<>(Set.of(meadow4, meadow5), List.of(PlayerColor.RED), 0)
    ));

    private final ZonePartition<Zone> zonePartitionFull = new ZonePartition<>(Set.of(
            new Area<>(Set.of(forest0, forest1, forest2), List.of(PlayerColor.RED), 0),
            new Area<>(Set.of(meadow4, meadow5), List.of(PlayerColor.RED), 0),
            new Area<>(Set.of(river3), new ArrayList<>(), 2)
    ));

    private final ZonePartition<Zone> zonePartitionFullWithOccupant = new ZonePartition<>(Set.of(
            new Area<>(Set.of(forest0, forest1, forest2), List.of(PlayerColor.RED), 0),
            new Area<>(Set.of(meadow4, meadow5), List.of(PlayerColor.RED), 0),
            new Area<>(Set.of(river3), List.of(PlayerColor.RED), 2)
    ));

    private final ZonePartition<Zone> zonePartitionUnion = new ZonePartition<>(Set.of(
            new Area<>(Set.of(forest0, forest1, forest2, river3), List.of(PlayerColor.RED, PlayerColor.RED), 0),
            new Area<>(Set.of(meadow4, meadow5), List.of(PlayerColor.RED), 0)
    ));

    @Test
    void areaContainingIsDefinedCorrectly() {
        assertEquals(new Area<>(Set.of(river3), new ArrayList<>(), 2), zonePartitionFull.areaContaining(river3));
    }

    @Test
    void areasIsDefinedCorrectly() {
        assertEquals(Set.of(
                new Area<>(Set.of(forest0, forest1, forest2), List.of(PlayerColor.RED), 0),
                new Area<>(Set.of(meadow4, meadow5), List.of(PlayerColor.RED), 0),
                new Area<>(Set.of(river3), new ArrayList<>(), 2)
        ), zonePartitionFull.areas());
    }

    @Test
    void addSingletonIsDefinedCorrectly() {
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(zonePartition);
        builder.addSingleton(river3, 2);
        ZonePartition<Zone> newZonePartition = builder.build();
        assertEquals(zonePartitionFull, newZonePartition);
    }

    @Test
    void addInitialOccupantIsDefinedCorrectly() {
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(zonePartitionFull);
        builder.addInitialOccupant(river3, PlayerColor.RED);
        ZonePartition<Zone> newZonePartition = builder.build();
        assertEquals(zonePartitionFullWithOccupant, newZonePartition);
    }

    @Test
    void removeOccupantIsDefinedCorrectly() {
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(zonePartitionFullWithOccupant);
        builder.removeOccupant(river3, PlayerColor.RED);
        ZonePartition<Zone> newZonePartition = builder.build();
        assertEquals(zonePartitionFull, newZonePartition);
    }

    @Test
    void removeAllOccupantsOfIsDefinedCorrectly() {
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(zonePartitionFullWithOccupant);
        builder.removeAllOccupantsOf(new Area<>(Set.of(river3), List.of(PlayerColor.RED), 2));
        ZonePartition<Zone> newZonePartition = builder.build();
        assertEquals(zonePartitionFull, newZonePartition);
    }

    @Test
    void unionIsDefinedCorrectly() {
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(zonePartitionFullWithOccupant);
        builder.union(forest0, river3);
        ZonePartition<Zone> newZonePartition = builder.build();
        assertEquals(zonePartitionUnion, newZonePartition);
    }
}