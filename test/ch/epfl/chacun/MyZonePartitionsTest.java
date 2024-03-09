package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyZonePartitionsTest {
    public ZonePartition buildZonePartitionForest() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), new Zone.Forest(4, Zone.Forest.Kind.PLAIN));
        Area area = new Area<>(zones, List.of(PlayerColor.RED), 3);
        Area area2 = new Area<>(zones2, List.of(PlayerColor.RED, PlayerColor.GREEN), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        return builder.build();
    }

    public ZonePartition buildZonePartitionMeadow() {
        Set<Zone> zones = Set.of(new Zone.Meadow(1, new ArrayList<>(), null), new Zone.Meadow(2, new ArrayList<>(), null));
        Set<Zone> zones2 = Set.of(new Zone.Meadow(3, new ArrayList<>(), null), new Zone.Meadow(4, new ArrayList<>(), null));
        Area area = new Area<>(zones, List.of(PlayerColor.RED), 3);
        Area area2 = new Area<>(zones2, List.of(PlayerColor.RED, PlayerColor.GREEN), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        return builder.build();
    }

    public ZonePartition buildZonePartitionRiver() {
        Set<Zone> zones = Set.of(new Zone.River(1, 1, null), new Zone.River(2, 1, null));
        Set<Zone> zones2 = Set.of(new Zone.River(3, 1, null), new Zone.River(4, 1, null));
        Area area = new Area<>(zones, List.of(PlayerColor.RED), 3);
        Area area2 = new Area<>(zones2, List.of(PlayerColor.RED, PlayerColor.GREEN), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        return builder.build();
    }

    public ZonePartition buildZonePartitionWater() {
        Set<Zone> zones = Set.of(new Zone.River(1, 1, null), new Zone.Lake(2, 1, null));
        Set<Zone> zones2 = Set.of(new Zone.River(1, 1, null), new Zone.Lake(2, 1, null));
        Area area = new Area<>(zones, List.of(PlayerColor.RED), 3);
        Area area2 = new Area<>(zones2, List.of(PlayerColor.RED, PlayerColor.GREEN), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        return builder.build();
    }

    public ZonePartitions buildZonePartitions() {
        return new ZonePartitions(buildZonePartitionForest(), buildZonePartitionRiver(), buildZonePartitionMeadow(), buildZonePartitionWater());
    }

}
