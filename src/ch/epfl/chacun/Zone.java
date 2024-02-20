package ch.epfl.chacun;

import java.util.List;

public interface Zone {
    public enum SpecialPower {
        SHAMAN,
        LOGBOAT,
        HUNTING_TRAP,
        PIT_TRAP,
        WILD_FIRE,
        RAFT;
    }

    public static int tileId(int zoneId) {
        return zoneId / 10;
    }

    public static int localId(int zoneId) {
        return zoneId % 10;
    }
    
    public abstract int id();

    public record Forest(int id, Kind kind) implements Zone {
        public enum Kind {
            PLAIN,
            WITH_MENHIR,
            WITH_MUSHROOMS;
        }
    }

    public record Meadow(int id, List<Animal> animals, SpecialPower specialPower) implements Zone {
        public Meadow {
            animals = List.copyOf(animals);
        }
    }

    public record Lake(int id, int fishCount, Zone.SpecialPower specialPower) implements Zone {

    }
}
