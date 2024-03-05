package ch.epfl.chacun.etape3;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyAreaTest {
    List<Animal> animals = List.of(
            new Animal(0, Animal.Kind.DEER),
            new Animal(1, Animal.Kind.AUROCHS),
            new Animal(2, Animal.Kind.MAMMOTH),
            new Animal(3, Animal.Kind.TIGER)
    );
    Set<Zone.Meadow> meadow = Set.of(
            new Zone.Meadow(560, animals, Zone.SpecialPower.SHAMAN),
            new Zone.Meadow(640, animals, null),
            new Zone.Meadow(240, animals, null),
            new Zone.Meadow(120, animals, null)
    );
    Set<Zone.Forest> forest = new HashSet<>(Set.of(
            new Zone.Forest(300, Zone.Forest.Kind.PLAIN),
            new Zone.Forest(720, Zone.Forest.Kind.WITH_MENHIR),
            new Zone.Forest(320, Zone.Forest.Kind.WITH_MUSHROOMS)
    ));
    Set<Zone.River> river = new HashSet<>(Set.of(
            new Zone.River(220, 2,  new Zone.Lake(0, 23, Zone.SpecialPower.SHAMAN)),
            new Zone.River(331, 43, new Zone.Lake(1, 43, Zone.SpecialPower.LOGBOAT)),
            new Zone.River(412, 99, new Zone.Lake(2, 67, Zone.SpecialPower.PIT_TRAP))
    ));
    List<PlayerColor> occupants = List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW);
    List<PlayerColor> occupants2 = List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.PURPLE);
    Zone.Lake lake = new Zone.Lake(6, 23, Zone.SpecialPower.SHAMAN);
    Set<Zone.Water> waterSet = Set.of(
            new Zone.River(110, 2, lake),
            new Zone.River(131, 4, lake),
            new Zone.River(232, 13, new Zone.Lake(2, 10, Zone.SpecialPower.PIT_TRAP))
    );
    Set<Zone.Water> waterSet2 = Set.of(
            new Zone.River(320, 2, lake),
            new Zone.River(421, 4, new Zone.Lake(4, 10, Zone.SpecialPower.PIT_TRAP)),
            new Zone.River(532, 13, new Zone.Lake(5, 11, Zone.SpecialPower.LOGBOAT)),
            new Zone.River(793, 2, new Zone.Lake(6, 12, Zone.SpecialPower.RAFT))
    );
    @Test
    void hasMenhirIsDefinedCorrectly() {
        Area<Zone.Forest> area = new Area<>(forest, occupants, 0);
        assertTrue(Area.hasMenhir(area));

        forest.removeIf(zone -> zone.kind() == Zone.Forest.Kind.WITH_MENHIR);
        assertTrue(Area.hasMenhir(area));

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR)), List.of(), 0);
        assertTrue(Area.hasMenhir(area));

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)), List.of(), 0);
        assertFalse(Area.hasMenhir(area));
    }

    @Test
    void mushroomGroupCountIsDefinedCorrectly() {
        Area<Zone.Forest> area = new Area<>(forest, occupants, 0);
        assertEquals(1, Area.mushroomGroupCount(area));

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS)), List.of(), 0);
        assertEquals(1, Area.mushroomGroupCount(area));

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)), List.of(), 0);
        assertEquals(0, Area.mushroomGroupCount(area));

        Set<Zone.Forest> forest2 = new HashSet<>(Set.of(
                new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS),
                new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS),
                new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS)
        ));

        area = new Area<>(forest2, List.of(), 2);
        assertEquals(3, Area.mushroomGroupCount(area));
    }

    @Test
    void animalsIsDefinedCorrectly() {
        Area<Zone.Meadow> area = new Area<>(meadow, occupants, 1);
        assertEquals(new HashSet<>(animals), Area.animals(area, Set.of()));

        Animal deer = new Animal(0, Animal.Kind.DEER);
        area = new Area<>(Set.of(new Zone.Meadow(0, List.of(deer), null)), occupants, 0);
        assertEquals(Set.of(deer), Area.animals(area, Set.of()));
        assertEquals(Set.of(), Area.animals(area, Set.of(deer)));

        area = new Area<>(Set.of(new Zone.Meadow(0, List.of(), null)), List.of(), 0);
        assertEquals(Set.of(), Area.animals(area, Set.of()));

        area = new Area<>(Set.of(new Zone.Meadow(0, List.of(), null)), occupants, 0);
        assertEquals(Set.of(), Area.animals(area, Set.of(new Animal(0, Animal.Kind.DEER))));

        area = new Area<>(Set.of(new Zone.Meadow(0, List.of(new Animal(0, Animal.Kind.DEER)), null)), List.of(), 0);
        assertEquals(Set.of(), Area.animals(area, Set.of(new Animal(0, Animal.Kind.DEER))));

        area = new Area<>(Set.of(new Zone.Meadow(0, List.of(new Animal(0, Animal.Kind.DEER)), null)), occupants, 0);
        assertEquals(Set.of(new Animal(0, Animal.Kind.DEER)), Area.animals(area, Set.of()));
    }

    @Test
    void riverFishCountIsDefinedCorrectly() {
        Area<Zone.River> area = new Area<>(river, occupants, 0);
        assertEquals(277, Area.riverFishCount(area));

        area = new Area<>(Set.of(new Zone.River(0, 2,  null)), List.of(), 1);
        assertEquals(2, Area.riverFishCount(area));

        area = new Area<>(Set.of(new Zone.River(0, 2,  lake)), List.of(), 1);
        assertEquals(25, Area.riverFishCount(area));

        area = new Area<>(Set.of(new Zone.River(0, 3,  lake)), List.of(), 1);
        assertEquals(26, Area.riverFishCount(area));

        area = new Area<>(Set.of(new Zone.River(0, 3,  lake), new Zone.River(1, 6,  lake)), List.of(), 1);
        assertEquals(32, Area.riverFishCount(area));
    }

    @Test
    void riverSystemFishCountIsDefinedCorrectly() {
        Area<Zone.Water> area = new Area<>(waterSet, occupants, 1);
        assertEquals(52, Area.riverSystemFishCount(area));

        area = new Area<>(Set.of(new Zone.River(0, 2,  lake)), List.of(), 1);
        assertEquals(25, Area.riverSystemFishCount(area));

        area = new Area<>(Set.of(new Zone.River(0, 2,  lake), new Zone.River(1, 4,
                new Zone.Lake(1, 40, Zone.SpecialPower.LOGBOAT))), List.of(), 1);
        assertEquals(69, Area.riverSystemFishCount(area));
    }

    @Test
    void lakeCountIsDefinedCorrectly() {
        Area<Zone.Water> area = new Area<>(waterSet, occupants, 1);
        assertEquals(2, Area.lakeCount(area));

        area = new Area<>(Set.of(new Zone.River(0, 2,  lake)), List.of(), 1);
        assertEquals(1, Area.lakeCount(area));

        area = new Area<>(Set.of(new Zone.River(0, 3,  lake), new Zone.River(1, 4,
                new Zone.Lake(1, 40, Zone.SpecialPower.LOGBOAT))), List.of(), 1);
        assertEquals(2, Area.lakeCount(area));

        area = new Area<>(waterSet2, List.of(PlayerColor.RED, PlayerColor.BLUE), 2);
        assertEquals(4, Area.lakeCount(area));
    }

    @Test
    void isClosedIsDefinedCorrectly() {
        Area<Zone.Forest> area = new Area<>(forest, occupants, 0);
        assertTrue(area.isClosed());

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)), List.of(), 2);
        assertFalse(area.isClosed());

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.PLAIN), new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), List.of(), 999);
        assertFalse(area.isClosed());
    }

    @Test
    void isOccupiedIsDefinedCorrectly() {
        Area<Zone.Forest> area = new Area<>(forest, occupants, 0);
        assertTrue(area.isOccupied());

        area = new Area<>(forest, List.of(PlayerColor.PURPLE), 0);
        assertTrue(area.isOccupied());
        area = area.withoutOccupant(PlayerColor.PURPLE);
        assertFalse(area.isOccupied());

        area = new Area<>(forest, List.of(PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW), 0);
        assertTrue(area.isOccupied());
        assertEquals(occupants2.size(), area.occupants().size());

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)), List.of(), 2);
        assertFalse(area.isOccupied());

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.PLAIN),
                new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), List.of(), 999);
        assertFalse(area.isOccupied());
    }

    @Test
    void majorityOccupantsIsDefinedCorrectly() {
        Area<Zone.Forest> area = new Area<>(forest, occupants, 0);
        assertEquals(new HashSet<>(occupants), area.majorityOccupants());

        area = new Area<>(forest, List.of(PlayerColor.PURPLE), 0);
        assertEquals(Set.of(PlayerColor.PURPLE), area.majorityOccupants());

        area = new Area<>(forest, List.of(PlayerColor.PURPLE, PlayerColor.PURPLE, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW), 0);
        assertEquals(Set.of(PlayerColor.PURPLE), area.majorityOccupants());

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)), List.of(), 2);
        assertEquals(Set.of(), area.majorityOccupants());

        area = new Area<>(Set.of(new Zone.Forest(0, Zone.Forest.Kind.PLAIN),
                new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), List.of(), 999);
        assertEquals(Set.of(), area.majorityOccupants());
    }

    @Test
    void connectToIsDefinedCorrectly() {
        Area<Zone.Forest> area = new Area<>(forest, occupants, 2);
        assertNotEquals(area, area.connectTo(area));

        Area<Zone.Forest> area2 = new Area<>(forest, occupants, 3);
        assertEquals(area.connectTo(area2), area2.connectTo(area));

        area = new Area <>(forest, List.of(PlayerColor.PURPLE), 2);
        assertTrue(area.connectTo(area).isClosed());

        area = new Area<>(forest, List.of(PlayerColor.PURPLE), 2);
        area2 = new Area<>(forest, List.of(PlayerColor.PURPLE, PlayerColor.RED), 3);

        Area<Zone.Forest> area3 = new Area<>(forest, List.of(PlayerColor.PURPLE, PlayerColor.RED), 3);
        assertNotEquals(area3, area.connectTo(area2));
    }

    @Test
    void withInitialOccupantIsDefinedCorrectly() {
        Area<Zone.Forest> area = new Area<>(forest, List.of(), 2);
        assertEquals(new Area<>(forest, List.of(PlayerColor.RED), 2), area.withInitialOccupant(PlayerColor.RED));

        Area<Zone.Meadow> area2 = new Area<>(meadow, List.of(PlayerColor.RED), 2);
        assertThrows(IllegalArgumentException.class, () -> area2.withInitialOccupant(PlayerColor.RED));
    }

    @Test
    void withoutOccupantIsDefinedCorrectly() {
        Area<Zone.Forest> area = new Area<>(forest, List.of(PlayerColor.RED), 2);
        assertEquals(new Area<>(forest, List.of(), 2), area.withoutOccupant(PlayerColor.RED));
        assertThrows(IllegalArgumentException.class, () -> area.withoutOccupant(PlayerColor.BLUE));

        Area<Zone.Meadow> area2 = new Area<>(meadow, List.of(), 2);
        assertThrows(IllegalArgumentException.class, () -> area2.withoutOccupant(PlayerColor.RED));

        Area<Zone.Meadow> area3 = new Area<>(meadow, List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW), 2);
        assertEquals(new Area<>(meadow, List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN), 2), area3.withoutOccupant(PlayerColor.YELLOW));
        assertEquals(new Area<>(meadow, List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.YELLOW), 2), area3.withoutOccupant(PlayerColor.GREEN));
        assertEquals(new Area<>(meadow, List.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.YELLOW), 2), area3.withoutOccupant(PlayerColor.BLUE));
        assertEquals(new Area<>(meadow, List.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW), 2), area3.withoutOccupant(PlayerColor.RED));

        assertThrows(IllegalArgumentException.class, () -> area3.withoutOccupant(PlayerColor.PURPLE));

    }

    @Test
    void withoutOccupantsIsDefinedCorrectly() {
        Area<Zone.Forest> area = new Area<>(forest, List.of(PlayerColor.RED), 2);
        assertEquals(new Area<>(forest, List.of(), 2), area.withoutOccupants());

        Area<Zone.Meadow> area3 = new Area<>(meadow, List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW), 2);
        assertEquals(new Area<>(meadow, List.of(), 2), area3.withoutOccupants());
        assertEquals(new Area<>(meadow, List.of(), 2), area3.withoutOccupants());
        assertEquals(new Area<>(meadow, List.of(), 2), area3.withoutOccupants());
        assertEquals(new Area<>(meadow, List.of(), 2), area3.withoutOccupants());
    }

    @Test
    void tileIdsIsDefinedCorrectly() {
        Area<Zone.Meadow> area = new Area<>(meadow, List.of(), 2);
        Area<Zone.Forest> area2 = new Area<>(forest, List.of(), 2);

        assertEquals(Set.of(64, 56, 24, 12), area.tileIds());
        assertEquals(Set.of(72, 32, 30), area2.tileIds());

        Set<Zone.Forest> forest2 = new HashSet<>(Set.of(
                new Zone.Forest(200, Zone.Forest.Kind.WITH_MUSHROOMS),
                new Zone.Forest(100, Zone.Forest.Kind.WITH_MUSHROOMS),
                new Zone.Forest(320, Zone.Forest.Kind.WITH_MUSHROOMS)
        ));
        area2 = new Area<>(forest2, List.of(), 2);
        assertEquals(Set.of(32, 20, 10), area2.tileIds());

        Set<Zone.Meadow> meadow2 = Set.of(
                new Zone.Meadow(210, animals, null),
                new Zone.Meadow(301, animals, null),
                new Zone.Meadow(592, animals, null)
        );
        area = new Area<>(meadow2, List.of(), 2);
        Area<Zone.Meadow> area3 = new Area<>(meadow2, List.of(PlayerColor.RED), 2);
        assertEquals(area.tileIds(), area3.tileIds());
    }

    @Test
    void zoneWithSpecialPowerIsDefinedCorrectly() {
        Area<Zone.Meadow> area = new Area<>(meadow, List.of(), 2);
        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT));
        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP));
        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.RAFT));
        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE));
        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP));
        assertEquals(new Zone.Meadow(560, animals, Zone.SpecialPower.SHAMAN), area.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN));

        Set<Zone.Lake> lakes = Set.of(
                new Zone.Lake(230, 23, Zone.SpecialPower.PIT_TRAP),
                new Zone.Lake(401, 43, Zone.SpecialPower.LOGBOAT),
                new Zone.Lake(342, 67, Zone.SpecialPower.PIT_TRAP)
        );

        Area<Zone.Lake> area2 = new Area<>(lakes, List.of(), 2);
        assertNull(area2.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE));
        assertNull(area2.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP));
        assertNull(area2.zoneWithSpecialPower(Zone.SpecialPower.RAFT));
        assertNotNull(area2.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP));
        assertEquals(
                new Zone.Lake(401, 43, Zone.SpecialPower.LOGBOAT),
                area2.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT)
        );

        Set<Zone.River> rivers = Set.of(
                new Zone.River(220, 2,  new Zone.Lake(0, 23, Zone.SpecialPower.SHAMAN)),
                new Zone.River(331, 43, new Zone.Lake(1, 43, Zone.SpecialPower.LOGBOAT)),
                new Zone.River(412, 99, new Zone.Lake(2, 67, Zone.SpecialPower.PIT_TRAP))
        );
        Area<Zone.River> area3 = new Area<>(rivers, List.of(), 2);
        assertNull(area3.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE));
        assertNull(area3.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP));
        assertNull(area3.zoneWithSpecialPower(Zone.SpecialPower.RAFT));

        Set<Zone.Lake> singleLake = Set.of(new Zone.Lake(1, 43, Zone.SpecialPower.LOGBOAT));
        area2 = new Area<>(singleLake, List.of(), 2);

        assertEquals(
                new Zone.River(331, 43, new Zone.Lake(1, 43, Zone.SpecialPower.LOGBOAT)).lake(),
                area2.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT)
        );

    }
}