package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    void areaHasMenhir() {
        Area area = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR)), new ArrayList<PlayerColor>(), 0);
        assertEquals(true, hasMenhir(area));
    }

    @Test
    void areaHasMultipleMenhirs() {
        Area area = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR)), new ArrayList<PlayerColor>(), 0);
        assertEquals(true, hasMenhir(area));
    }

    @Test
    void areaHasntMenhir() {
        Area area = new Area<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)), new ArrayList<PlayerColor>(), 0);
        assertEquals(false, hasMenhir(area));
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

    
}
