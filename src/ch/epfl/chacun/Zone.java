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

    public record Forest(int id, Kind kind) {
        public enum Kind {
            PLAIN,
            WITH_MENHIR,
            WITH_MUSHROOMS
        }
    }

    public record Meadow(int id, List<Animal> animals, SpecialPower specialPower) {
        public Meadow {
            animals = List.copyOf(animals);
        }

    }
}
