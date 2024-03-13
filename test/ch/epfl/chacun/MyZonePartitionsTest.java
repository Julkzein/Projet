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
        Area area = new Area<>(zones, List.of(), 3);
        Area area2 = new Area<>(zones2, List.of(), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        return builder.build();
    }

    public ZonePartition buildZonePartitionMeadow() {
        Set<Zone> zones = Set.of(new Zone.Meadow(1, new ArrayList<>(), null), new Zone.Meadow(2, new ArrayList<>(), null));
        Set<Zone> zones2 = Set.of(new Zone.Meadow(3, new ArrayList<>(), null), new Zone.Meadow(4, new ArrayList<>(), null));
        Area area = new Area<>(zones, List.of(), 3);
        Area area2 = new Area<>(zones2, List.of(), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        return builder.build();
    }

    public ZonePartition buildZonePartitionRiver() {
        Set<Zone> zones = Set.of(new Zone.River(1, 1, null), new Zone.River(2, 1, null));
        Set<Zone> zones2 = Set.of(new Zone.River(3, 1, null), new Zone.River(4, 1, null));
        Area area = new Area<>(zones, List.of(), 3);
        Area area2 = new Area<>(zones2, List.of(), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        return builder.build();
    }

    public ZonePartition buildZonePartitionWater() {
        Set<Zone> zones = Set.of(new Zone.River(1, 1, null), new Zone.Lake(3, 1, null));
        Set<Zone> zones2 = Set.of(new Zone.River(2, 1, null), new Zone.Lake(4, 1, null));
        Area area = new Area<>(zones, List.of(), 3);
        Area area2 = new Area<>(zones2, List.of(), 3);
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
    void testThrowConnectSides() {
        Zone.Forest forest1 = new Zone.Forest(9, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow1 = new Zone.Meadow(3, new ArrayList<>(), null);
        TileSide tsf1 = new TileSide.Forest(forest1);
        ZonePartitions.Builder builder = builderZonePartitions();
        TileSide tsm1 = new TileSide.Meadow(meadow1);
        assertThrows(IllegalArgumentException.class, () ->builder.connectSides(tsf1, tsm1));
    }

    @Test
    void testConnectSides() {
        Zone.Forest forest1 = new Zone.Forest(9, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(13, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide tsf1 = new TileSide.Forest(forest1);
        TileSide tsf2 = new TileSide.Forest(forest2);
        Set<Zone> zones = Set.of(new Zone.Forest(9, Zone.Forest.Kind.PLAIN));
        Set<Zone> zones2 = Set.of(new Zone.Forest(13, Zone.Forest.Kind.WITH_MUSHROOMS));
        Set<Zone> zones3 = Set.of(new Zone.Forest(9, Zone.Forest.Kind.PLAIN), new Zone.Forest(13, Zone.Forest.Kind.WITH_MUSHROOMS));
        Area area = new Area<>(zones, List.of(), 3);
        Area area2 = new Area<>(zones2, List.of(), 3);
        Area area3 = new Area<>(zones3, List.of(), 4);
        ZonePartition.Builder<Zone> builderSmall1 = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        ZonePartition.Builder<Zone> builderSmall2 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area3)));
        ZonePartition parti1 = builderSmall1.build();
        ZonePartition parti2 = builderSmall2.build();
        ZonePartitions partitionsTest = new ZonePartitions(parti1, buildZonePartitionRiver(), buildZonePartitionMeadow(), buildZonePartitionWater());
        ZonePartitions partitonsExp = new ZonePartitions(parti2, buildZonePartitionRiver(), buildZonePartitionMeadow(), buildZonePartitionWater());
        ZonePartitions.Builder builder = new ZonePartitions.Builder(partitionsTest);
        builder.connectSides(tsf1, tsf2);
        assertEquals(partitonsExp, builder.build());
    }


    @Test
    void addInitialOccupantTypeTest() {
        ZonePartitions.Builder builder = builderZonePartitions();
        assertThrows(IllegalArgumentException.class, () ->builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT, new Zone.Forest(1, Zone.Forest.Kind.PLAIN)));
        assertThrows(IllegalArgumentException.class, () ->builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT, new Zone.Meadow(1, new ArrayList<>(), null)));
    }

    @Test
    void addInitialOccupantTest() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), new Zone.Forest(4, Zone.Forest.Kind.PLAIN));
        Area area = new Area<>(zones, List.of(PlayerColor.RED), 3);
        Area area2 = new Area<>(zones2, List.of(), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        ZonePartition partiF = builder.build();
        ZonePartitions.Builder builderPart = builderZonePartitions();
        builderPart.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        ZonePartitions zonePartitionsExp = new ZonePartitions(partiF, buildZonePartitionRiver(), buildZonePartitionMeadow(), buildZonePartitionWater());
        ZonePartitions zonePartitionsAct = builderPart.build();
        assertEquals(zonePartitionsExp, zonePartitionsAct);
    }

   @Test
   void removePawn() {
       Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
       Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), new Zone.Forest(4, Zone.Forest.Kind.PLAIN));
       Area area = new Area<>(zones, List.of(PlayerColor.RED), 3);
       Area area2 = new Area<>(zones2, List.of(), 3);
       ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
       ZonePartition partiF = builder.build();
       ZonePartitions zonePartitionsT = new ZonePartitions(partiF, buildZonePartitionRiver(), buildZonePartitionMeadow(), buildZonePartitionWater());
       ZonePartitions.Builder tester1 = new ZonePartitions.Builder(zonePartitionsT);
       tester1.removePawn(PlayerColor.RED, new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
       ZonePartitions.Builder tester2 = new ZonePartitions.Builder(zonePartitionsT);
       tester2.removePawn(PlayerColor.RED, new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
       ZonePartitions partTest1 = tester1.build();
       ZonePartitions partTest2 = tester2.build();

       ZonePartitions.Builder builderPart = builderZonePartitions();
       ZonePartitions zonePartitionsAct = builderPart.build();

       assertEquals(zonePartitionsAct, partTest1);
       assertEquals(zonePartitionsAct, partTest2);
   }

   @Test
   void clearGatherersTest() {
       Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
       Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), new Zone.Forest(4, Zone.Forest.Kind.PLAIN));
       Area area = new Area<>(zones, List.of(PlayerColor.RED, PlayerColor.BLUE), 3);
       Area area2 = new Area<>(zones2, List.of(), 3);
       ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
       ZonePartition partiF = builder.build();
       ZonePartitions zonePartitionsT = new ZonePartitions(partiF, buildZonePartitionRiver(), buildZonePartitionMeadow(), buildZonePartitionWater());
       ZonePartitions.Builder tester = new ZonePartitions.Builder(zonePartitionsT);
       tester.clearGatherers(area);
       ZonePartitions partTest = tester.build();

       ZonePartitions.Builder builderPart = builderZonePartitions();
       ZonePartitions zonePartitionsAct = builderPart.build();

       assertEquals(zonePartitionsAct, partTest);
   }
}