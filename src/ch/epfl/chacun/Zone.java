package ch.epfl.chacun;

import java.util.List;

/**
 * This module contains the Zone interface and its implementations.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public sealed interface Zone {

    /**
     * Represents the six possible special powers of a zone.
     */
    public enum SpecialPower {
        SHAMAN,
        LOGBOAT,
        HUNTING_TRAP,
        PIT_TRAP,
        WILD_FIRE,
        RAFT
    }

    /**
     * Returns the tile id of the given zone id.
     *
     * @param zoneId the zone id
     * @return the tile id of the given zone id.
     */
    static int tileId(int zoneId) {
        return zoneId / 10;
    }

    /**
     * Returns the local id of the given zone id.
     *
     * @param zoneId the zone id
     * @return the local id of the given zone id.
     */
    static int localId(int zoneId) {
        return zoneId % 10;
    }

    /**
     * Returns the id of the zone.
     * @return the id of the zone.
     */
    int id();

    /**
     * Returns the tile id of the zone.
     * @return the tile id of the zone.
     */
    default int tileId() {
        return tileId(id());
    }

    /**
     * Returns the local id of the zone.
     * @return the local id of the zone.
     */
    default int localId() {
        return localId(id());
    }

    /**
     * Returns the special power of the zone.
     * @return the special power of the zone.
     */
    default SpecialPower specialPower() {
        return null;
    }

    /**
     * Represents a forest in the game.
     */
    record Forest(int id, Kind kind) implements Zone {
        public enum Kind {
            PLAIN,
            WITH_MENHIR,
            WITH_MUSHROOMS
        }
    }

    /**
     * Represents a meadow in the game.
     */
    record Meadow(int id, List<Animal> animals, SpecialPower specialPower) implements Zone {
        public Meadow {
            animals = List.copyOf(animals);
        }
    }

    /**
     * Represents a water zone in the game.
     */
    sealed interface Water extends Zone {
      int fishCount();
    }

    /**
     * Represents a lake in the game.
     */
    record Lake(int id, int fishCount, Zone.SpecialPower specialPower) implements Zone, Water {
    }

    /**
     * Represents a river in the game.
     */
    record River(int id, int fishCount, Lake lake) implements Zone, Water {
        public boolean hasLake() {
            return this.lake != null;
        }
    }
}
