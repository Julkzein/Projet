package ch.epfl.chacun;

import java.util.List;
import java.util.Objects;

/**
 * This interface represents a zone in the game.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public sealed interface Zone {

    /**
     * Represents the six possible special powers of a zone.
     */
    enum SpecialPower {
        SHAMAN,
        LOGBOAT,
        HUNTING_TRAP,
        PIT_TRAP,
        WILD_FIRE,
        RAFT
    }

    /**
     * Returns the id of the tile containing the zone of the given zoneId
     *
     * @param zoneId the zone id
     * @return the id of the tile containing the zone of the given zoneId
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
     * Returns the tile id of the tile containing the zone.
     * @return the tile id of the tile containing the zone.
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
        /**
         * The different types of forest.
         */
        public enum Kind {
            PLAIN,
            WITH_MENHIR,
            WITH_MUSHROOMS
        }
    }

    /**
     * Represents a meadow in the game.
     * @param id the id of the meadow
     * @param animals the animals contained in the meadow
     * @param specialPower the potential special power of the meadow
     */
    record Meadow(int id, List<Animal> animals, SpecialPower specialPower) implements Zone {
        /**
         * Compact constructor that guarantees the immutability of the meadow.
         */
        public Meadow {
            animals = List.copyOf(animals);
        }
    }

    /**
     * Represents a water zone in the game.
     */
    sealed interface Water extends Zone {
        /**
         * the number of fish in the water zone
         */
      int fishCount();
    }

    /**
     * Represents a lake in the game.
     * @param id the id of the zone
     * @param fishCount the number of fish in the lake
     * @param specialPower the possible special power of the lake
     */
    record Lake(int id, int fishCount, Zone.SpecialPower specialPower) implements Zone, Water {
    }

    /**
     * Represents a river in the game.
     * @param id the id of the zone
     * @param fishCount the number of fish in the river
     * @param lake the possible lake the river is connected to
     */
    record River(int id, int fishCount, Lake lake) implements Zone, Water {
        /**
         * This method checks if the river has a lake
         *
         * @return true if the river has a lake
         */
        public boolean hasLake() {
            return Objects.nonNull(lake);
        }
    }
}
