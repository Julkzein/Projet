package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.epfl.chacun.Area.hasMenhir;
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
    void 
}
