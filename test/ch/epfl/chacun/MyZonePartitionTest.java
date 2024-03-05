package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder();

    }



}
