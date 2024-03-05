package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MyZonePartitionTest {
    @Test
    void areaContaining() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.River(2, 4, null));
        Set<Zone> zones2 = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        Area area1 = new Area(zones, new ArrayList<>(), 0);
        Area area2 = new Area(zones2, new ArrayList<>(), 0);
        Set<Area> areaSet = Set.of(area1, area2);
        ZonePartition zonePartition = new ZonePartition(areaSet);
        assertEquals(zonePartition.areaContaining(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), area2);
        assertEquals(zonePartition.areaContaining(new Zone.River(2, 4, null)), area1);
        assertThrows(IllegalArgumentException.class, () -> zonePartition.areaContaining(new Zone.Forest(3, Zone.Forest.Kind.PLAIN)));
    }

    @Test
    void addSingletonArea() {
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition());
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        ZonePartition.Builder<Zone> builder2 = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3))));
        builder.addSingleton(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), 3);
        assertEquals(builder.build().areas(), builder2.build().areas());
    }

    @Test
    void addInitialOccupantTest() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3))));

        builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED);

        List<PlayerColor> initialOccupants = new ArrayList<>();
        initialOccupants.add(PlayerColor.RED);
        ZonePartition.Builder<Zone> builder2 = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, initialOccupants, 3))));

        assertEquals(builder.build().areaContaining(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), new Area<>(zones, initialOccupants, 3));
    }

    @Test
    void addInitialOccupantThrowsWhenNoSuchZone() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3))));

        assertThrows(IllegalArgumentException.class, () -> builder.addInitialOccupant(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), PlayerColor.RED));
    }

    @Test
    void addInitialOccuppantThrowsWhenOccupied() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3))));
        builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED);

        assertThrows(IllegalArgumentException.class, () -> builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED));
    }

    @Test
    void removeOccupantsCorrectly() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3))));
        builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED);
        builder.removeOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED);

        assertEquals(builder.build().areas(), new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3)))).build().areas());
    }

    @Test
    void removeOccupantsThrowsWhenNoSuchZone() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3))));
        builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED);

        assertThrows(IllegalArgumentException.class, () -> builder.removeOccupant(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), PlayerColor.RED));
    }

    @Test
    void removeOccupantsThrowsWhenNotOccupied() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3))));
        builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED);

        assertThrows(IllegalArgumentException.class, () -> builder.removeOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.GREEN));
    }

    @Test
    void removeAllOccupantsOf() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), new Zone.Meadow(4, new ArrayList<>(), null));
        Area<Zone> area = new Area<>(zones, List.of(PlayerColor.RED), 3);
        Area<Zone> area2 = new Area<>(zones2, List.of(PlayerColor.RED), 3);

        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));

        builder.removeAllOccupantsOf(area);

        assertEquals(builder.build().areas(), new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3), area2))).build().areas());
    }

    @Test
    void removeAllOccupantsOfThrowsIfNoSuchArea() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), new Zone.Meadow(4, new ArrayList<>(), null));
        Area area = new Area<>(zones, new ArrayList<>(), 3);
        Area area2 = new Area<>(zones2, List.of(PlayerColor.GREEN), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED);

        assertThrows(IllegalArgumentException.class, () -> builder.removeAllOccupantsOf(new Area<>(Set.of(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new ArrayList<>(), 3)));
        assertThrows(IllegalArgumentException.class, () -> builder.removeAllOccupantsOf(new Area<>(zones, List.of(PlayerColor.RED, PlayerColor.GREEN), 3)));
        assertThrows(IllegalArgumentException.class, () -> builder.removeAllOccupantsOf(new Area<>(zones, new ArrayList<>(), 2)));
    }

    @Test
    void unionWorksCorreclty() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), new Zone.Meadow(4, new ArrayList<>(), null));
        Area area = new Area<>(zones, List.of(PlayerColor.RED), 3);
        Area area2 = new Area<>(zones2, List.of(PlayerColor.RED, PlayerColor.GREEN), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));

        builder.union(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(4, new ArrayList<>(), null));

        //pb car areas est private
        //assertEquals(builder.areas(), List.of(new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(4, new ArrayList<>(), null)), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN), 4)));
    }

    @Test
    void unionThrowsIfNoSuchArea() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), new Zone.Meadow(4, new ArrayList<>(), null));
        Area area = new Area<>(zones, List.of(PlayerColor.RED), 3);
        Area area2 = new Area<>(zones2, List.of(PlayerColor.RED, PlayerColor.GREEN), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));

        assertThrows(IllegalArgumentException.class, () -> builder.union(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(5, new ArrayList<>(), null)));
    }

    @Test
    void buildsCorrectly() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN), new Zone.Meadow(4, new ArrayList<>(), null));
        Area area = new Area<>(zones, List.of(PlayerColor.RED), 3);
        Area area2 = new Area<>(zones2, List.of(PlayerColor.RED, PlayerColor.GREEN), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));

        ZonePartition zonePartition = builder.build();
        ZonePartition zonePartition2 = new ZonePartition(Set.of(area, area2));

        assertEquals(zonePartition, zonePartition2);
    }
}