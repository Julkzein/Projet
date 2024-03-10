package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

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

    public ZonePartitions.Builder builderZonePartitions() {
        ZonePartitions partitions = new ZonePartitions(buildZonePartitionForest(), buildZonePartitionRiver(), buildZonePartitionMeadow(), buildZonePartitionWater());
        return new ZonePartitions.Builder(partitions);
    }

    @Test
    void testBuild() {
        ZonePartitions partitions = new ZonePartitions(buildZonePartitionForest(), buildZonePartitionRiver(), buildZonePartitionMeadow(), buildZonePartitionWater());
        ZonePartitions.Builder part = new ZonePartitions.Builder(partitions);
        assertEquals(partitions, part.build());
    }

    @Test
    void testAddTile() {
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow1 = new Zone.Meadow(2, new ArrayList<>(), null);
        Zone.Forest forest2 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow2 = new Zone.Meadow(4, new ArrayList<>(), null);
        Tile tile = new Tile(78, Tile.Kind.NORMAL,
                new TileSide.Forest(forest1),
                new TileSide.Meadow(meadow1),
                new TileSide.Forest(forest2),
                new TileSide.Meadow(meadow2));

        ZonePartitions.Builder builder = builderZonePartitions();
        builder.addTile(tile);
        ZonePartitions testos = builder.build();
        Set<Zone> zonesF = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        Area areaF = new Area<>(zonesF, new ArrayList<>(), 0);
        ZonePartition.Builder builderF = new ZonePartition.Builder(new ZonePartition(Set.of(areaF)));
        ZonePartition f = builderF.build();
        Set<Zone> zonesM = Set.of(new Zone.Meadow(2, new ArrayList<>(), null), new Zone.Meadow(4, new ArrayList<>(), null));
        Area areaM = new Area<>(zonesM, new ArrayList<>(), 0);
        ZonePartition.Builder builderM = new ZonePartition.Builder(new ZonePartition(Set.of(areaM)));
        ZonePartition m = builderM.build();
        ZonePartition.Builder builderR = new ZonePartition.Builder(new ZonePartition(Set.of()));
        ZonePartition r = builderR.build();
        ZonePartition.Builder builderW = new ZonePartition.Builder(new ZonePartition<>(Set.of()));
        ZonePartition w = builderW.build();
        assertEquals(testos, w);
    }




}
