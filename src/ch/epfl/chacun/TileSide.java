package ch.epfl.chacun;

import java.util.List;


/**
 * This interface represents a side of a tile.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public sealed interface TileSide {
    /**
     * abstract method that will return a list of the zones of the type of TileSide
     */
    List<Zone> zones();

    /**
     * Abstract method that will verify if the given TileSide is of the same type
     * as this TileSide
     *
     * @param that the TileSide we desire to check if it is of the same type
     * as this TileSide
     */
    boolean isSameKindAs(TileSide that);

    /**
     * This record represents the sides of the tiles that will be of the
     * type forest
     *
     * @param forest the zone of the type forest that will be present in the
     * TileSide of the type Forest
     */
    record Forest(Zone.Forest forest) implements TileSide {
        /**
         * This method returns the list of zones of the type forest
         *
         * @return the list of zones of the type forest
         */
        public List<Zone> zones() {
            return List.of(forest);
        }

        /**
         * This method checks if the given TileSide is of the type Forest
         * 
         * @param that The TileSide we want to verify if it is of the type forest
         * @return true if that is of the kind forest, false otherwise
         */
        public boolean isSameKindAs(TileSide that){
            return that instanceof Forest;
        }
    }

    /**
     * This record represents the sides of the tiles that will be of the
     * type meadow
     *
     * @param meadow the zone of the type meadow that will be present in the
     * TileSide of the type Meadow
     */
    record Meadow(Zone.Meadow meadow) implements TileSide {
        /**
         * This method returns the list of zones of the type meadow
         *
         * @return the list of zones of the type meadow
         */
        public List<Zone> zones() {
            return List.of(meadow);
        }

        /**
         * This method checks if the given TileSide is of the type Meadow
         *
         * @param that The TileSide we want to verify if it is of the type meadow
         * @return true if that is of the kind meadow, false otherwise
         */
        public boolean isSameKindAs(TileSide that) {
            return that instanceof Meadow;
        }
    }

    /**
     * This record represents the sides of the tiles that will be of the
     * type river
     *
     * @param river the zone of the type river that will be present in the
     * TileSide of the type River
     */
    record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide {
        /**
         * This method returns the list of zones of the type river
         *
         * @return the list of zones of the type river
         */
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        /**
         * This method checks if the given TileSide is of the type River
         *
         * @param that The TileSide we want to verify if it is of the type river
         * @return true if that is of the kind river, false otherwise
         */
        public boolean isSameKindAs(TileSide that) {
            return that instanceof River;
        }
    }
}
