package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MyZonePartitionTest {

    /*** test marche mais erreur d'assertion ***/
    @Test
    void testCopyOfArea() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.River(2, 4, null));
        Set<Zone> zones2 = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        Area area1 = new Area(zones, new ArrayList<>(), 0);
        Area area2 = new Area(zones2, new ArrayList<>(), 0);
        Set<Area> areaSet = Set.of(area1, area2);
        ZonePartition zonePartition = new ZonePartition(areaSet);
        ZonePartition zonePartition2 = new ZonePartition(zonePartition.areas());

        assertNotEquals(zonePartition.areas(), zonePartition2.areas());
    }

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
        assertEquals(builder, builder2);
    }

    @Test
    void addInitialOccupant() {
        Set<Zone> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3))));

        builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED);

        List<PlayerColor> initialOccupants = new ArrayList<>();
        initialOccupants.add(PlayerColor.RED);
        ZonePartition.Builder<Zone> builder2 = new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, initialOccupants, 3))));

        assertEquals(builder, builder2);
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

        assertEquals(builder, new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3)))));
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
        Set<Zone> zones2 = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Meadow(2, new ArrayList<>(), null));
        Area area = new Area<>(zones, new ArrayList<>(), 3);
        Area area2 = new Area<>(zones2, List.of(PlayerColor.RED), 3);
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder(new ZonePartition(Set.of(area, area2)));
        builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.RED);
        builder.addInitialOccupant(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), PlayerColor.GREEN);

        builder.removeAllOccupantsOf(area);

        assertEquals(builder, new ZonePartition.Builder(new ZonePartition(Set.of(new Area<>(zones, new ArrayList<>(), 3), area2))));
    }
    


}