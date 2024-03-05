package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.*;

import static ch.epfl.chacun.Area.*;
import static org.junit.jupiter.api.Assertions.*;

public class MyAreaTest {

    @Test
    void areaCompactConstructorOpenConnections() {
        assertThrows(IllegalArgumentException.class, () -> new Area<>(new HashSet<Zone>(), new ArrayList<PlayerColor>(), -1));
    }

    @Test
    void areaCompactConstructorOk(){
        new Area<>(new HashSet<Zone>(), new ArrayList<PlayerColor>(), 0);
        new Area<>(new HashSet<Zone>(), new ArrayList<PlayerColor>(), 1);
    }

    @Test
    void sortOccupants() {
        Area area = new Area<>(Set.of(), List.of(PlayerColor.GREEN, PlayerColor.RED), 0);
        assertEquals(List.of(PlayerColor.RED, PlayerColor.GREEN), area.occupants());
    }

    @Test
    void areaHasMenhir() {
        Area area = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR)), new ArrayList<PlayerColor>(), 0);
        assertTrue(hasMenhir(area));
    }

    @Test
    void areaHasMultipleMenhirs() {
        Area area = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new ArrayList<PlayerColor>(), 0);
        assertTrue(hasMenhir(area));
    }

    @Test
    void areaWithNoMenhir() {
        Area area = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), new ArrayList<PlayerColor>(), 0);
        assertFalse(hasMenhir(area));
    }

    @Test
    void areaMushroomCount() {
        Area area = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS)), new ArrayList<PlayerColor>(), 0);
        Area area2 = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(3, Zone.Forest.Kind.WITH_MUSHROOMS)), new ArrayList<PlayerColor>(), 0);
        Area area3 = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(3, Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS)), new ArrayList<PlayerColor>(), 0);
        Area area4 = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), new ArrayList<PlayerColor>(), 0);

        assertEquals(1, mushroomGroupCount(area));
        assertEquals(2, mushroomGroupCount(area2));
        assertEquals(3, mushroomGroupCount(area3));
        assertEquals(0, mushroomGroupCount(area4));
    }

    @Test
    void areaAnimalCount() {
        Animal deer = new Animal(87, Animal.Kind.DEER);
        Area area = new Area<>(Set.of(new Zone.Meadow(1,  List.of(deer), null)), new ArrayList<PlayerColor>(), 0);
        Area area2 = new Area<>(Set.of(new Zone.Meadow(1,  List.of(deer, new Animal(88, Animal.Kind.TIGER)), null)), new ArrayList<PlayerColor>(), 0);
        Area area3 = new Area<>(Set.of(new Zone.Meadow(1,  List.of(deer, new Animal(88, Animal.Kind.TIGER), new Animal(89, Animal.Kind.AUROCHS)), null)), new ArrayList<PlayerColor>(), 0);

        assertEquals(Set.of(), animals(area, Set.of(new Animal(87, Animal.Kind.DEER))));
        assertEquals(Set.of(), animals(area, Set.of(deer)));
        assertEquals(Set.of(deer), animals(area2, Set.of(new Animal(88, Animal.Kind.TIGER))));
        assertEquals(Set.of(deer), animals(area2, Set.of(new Animal(88, Animal.Kind.TIGER), new Animal(89, Animal.Kind.AUROCHS))));
        assertEquals(Set.of(deer, new Animal(89, Animal.Kind.AUROCHS)), animals(area3, Set.of(new Animal(88, Animal.Kind.TIGER))));
        assertEquals(Set.of(deer, new Animal(88, Animal.Kind.TIGER), new Animal(89, Animal.Kind.AUROCHS)), animals(area3, Set.of()));
    }

    @Test
    void areaRiverFishCount() {
        Area area = new Area<>(Set.of(new Zone.River(1, 2, null)), new ArrayList<PlayerColor>(), 0);
        Area area2 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 2, null))), new ArrayList<PlayerColor>(), 0);
        Zone.Lake lake = new Zone.Lake(2, 3, null);
        Area area3 = new Area<>(Set.of(new Zone.River(1, 2, lake), new Zone.River(2, 1, lake)), new ArrayList<PlayerColor>(), 0);

        assertEquals(2, riverFishCount(area));
        assertEquals(4, riverFishCount(area2));
        assertEquals(6, riverFishCount(area3));
    }

    @Test
    void areaRiverSystemFishCount() {
        Zone.Lake lake = new Zone.Lake(2, 3, null);
        Area areaLoop = new Area<>(Set.of(new Zone.River(1, 2, lake), new Zone.River(2, 1, lake)), new ArrayList<PlayerColor>(), 0);
        assertEquals(6, riverSystemFishCount(areaLoop));

        Area areaNotLoop = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, lake)), new ArrayList<PlayerColor>(), 0);
        assertEquals(6, riverSystemFishCount(areaNotLoop));
    }

    @Test
    void areLakeCount() {
        Area areaLoop = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), new ArrayList<PlayerColor>(), 0);
        assertEquals(1, areaLoop.lakeCount(areaLoop));
        Area areaNotLoop = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(8, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), new ArrayList<PlayerColor>(), 0);
        assertEquals(2, areaNotLoop.lakeCount(areaNotLoop));
    }

    @Test
    void areaIsClosed() {
        Area area1 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), new ArrayList<PlayerColor>(), 0);
        assertTrue(area1.isClosed());
        Area area2 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), new ArrayList<PlayerColor>(), 1);
        assertFalse(area2.isClosed());
    }

    @Test
    void areaIsOccupied() {
        Area area1 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), new ArrayList<PlayerColor>(), 0);
        assertFalse(area1.isOccupied());
        Area area2 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), List.of(PlayerColor.RED), 0);
        assertTrue(area2.isOccupied());
        Area area3 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), List.of(PlayerColor.RED, PlayerColor.GREEN), 0);
        assertTrue(area3.isOccupied());
    }

    @Test
    void areaMajorityOccupants() {
        Area area1 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), new ArrayList<PlayerColor>(), 0);
        assertEquals(new HashSet<>(), area1.majorityOccupants());
        Area area2 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), List.of(PlayerColor.RED), 0);
        assertEquals(new HashSet<>(Collections.singleton(PlayerColor.RED)), area2.majorityOccupants());
        Area area3 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), List.of(PlayerColor.RED, PlayerColor.GREEN), 0);
        Set<PlayerColor> maj3 = new HashSet<>();
        maj3.add(PlayerColor.RED);
        maj3.add(PlayerColor.GREEN);
        assertEquals(maj3, area3.majorityOccupants());
        Area area4 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), List.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.RED), 0);
        assertEquals(new HashSet<>(Collections.singleton(PlayerColor.RED)), area4.majorityOccupants());
    }

    @Test
    void areaConnectTo() {
        Area area1 = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null))), new ArrayList<PlayerColor>(Collections.singleton(PlayerColor.GREEN)), 1);
        assertEquals(area1, area1.connectTo(area1));
        Area area2 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), new ArrayList<PlayerColor>(Collections.singleton(PlayerColor.RED)), 1);
        List<PlayerColor> expectedOccu = new ArrayList<>();
        expectedOccu.add(PlayerColor.GREEN);
        expectedOccu.add(PlayerColor.RED);
        Area expectedArea = new Area<>(Set.of(new Zone.River(1, 2, new Zone.Lake(2, 3, null)), new Zone.River(2, 1, new Zone.Lake(2, 3, null)),new Zone.River(1, 2, null), new Zone.River(2, 1, null)), expectedOccu, 0);
        assertEquals(expectedArea, area1.connectTo(area2));
    }

    @Test
    void areaWithInitialOccupant() {
        Area area1 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), new ArrayList<PlayerColor>(Collections.singleton(PlayerColor.RED)), 1);
        assertThrows(IllegalArgumentException.class, () -> area1.withInitialOccupant(PlayerColor.RED));
        Area area2 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), new ArrayList<PlayerColor>(), 1);
        assertEquals(area1, area2.withInitialOccupant(PlayerColor.RED));
    }

    @Test
    void areaWithoutOccupant() {
        Area area1 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), new ArrayList<PlayerColor>(Collections.singleton(PlayerColor.RED)), 1);
        Area area2 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), new ArrayList<PlayerColor>(), 1);
        List<PlayerColor> occu3 = new ArrayList<>();
        occu3.add(PlayerColor.RED);
        occu3.add(PlayerColor.RED);
        Area area3 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), occu3, 1);
        assertThrows(IllegalArgumentException.class, () -> area2.withoutOccupant(PlayerColor.RED));
        assertEquals(area2, area1.withoutOccupant(PlayerColor.RED));
    }

    @Test
    void areaWithoutAllOccupant() {
        List<PlayerColor> occu3 = new ArrayList<>();
        occu3.add(PlayerColor.RED);
        occu3.add(PlayerColor.RED);
        Area area3 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), occu3, 1);
        Area area2 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), new ArrayList<PlayerColor>(), 1);
        assertEquals(area2, area3.withoutOccupants());
    }

    @Test
    void areaTileIds() {
        Area area3 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), new ArrayList<>(), 1);
        Set<Integer> expected = new HashSet<>();
        expected.add(0);
        assertEquals(expected, area3.tileIds());
    }

    @Test
    void areaZoneWithSpecialPower() {
        Area area3 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null), new Zone.Lake(4, 2, Zone.SpecialPower.LOGBOAT)), new ArrayList<>(), 1);
        assertEquals(new Zone.Lake(4, 2, Zone.SpecialPower.LOGBOAT), area3.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT));
        Area area2 = new Area<>(Set.of(new Zone.River(1, 2, null), new Zone.River(2, 1, null)), new ArrayList<>(), 1);
        assertEquals(null, area2.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT));
    }

}
