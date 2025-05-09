package ch.epfl.chacun;

/**
 * This record contains the four zone partitions of the game.
 *
 * @param forests the partition of forests
 * @param meadows the partition of meadows
 * @param rivers the partition of rivers
 * @param riverSystems the partition of aquatic zones
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests,ZonePartition<Zone.Meadow> meadows, ZonePartition<Zone.River> rivers,  ZonePartition<Zone.Water> riverSystems) {

    /**
     * This constructor creates a set of empty zone partitions
     */
    public static final ZonePartitions EMPTY =
            new ZonePartitions(
                new ZonePartition<>(),
                new ZonePartition<>(),
                new ZonePartition<>(),
                new ZonePartition<>());

    /**
     * This class is a builder for zone partitions
     */
    public static final class Builder {
        private final ZonePartition.Builder<Zone.Forest> forests;
        private final ZonePartition.Builder<Zone.River> rivers;
        private final ZonePartition.Builder<Zone.Meadow> meadows;
        private final ZonePartition.Builder<Zone.Water> riverSystems;

        /**
         * This constructor creates a builder with given zone partitions
         * @param initial the zone partitions to start with
         */
        public Builder(ZonePartitions initial) {
            forests = new ZonePartition.Builder<>(initial.forests);
            rivers = new ZonePartition.Builder<>(initial.rivers);
            meadows = new ZonePartition.Builder<>(initial.meadows);
            riverSystems = new ZonePartition.Builder<>(initial.riverSystems);
        }

        /**
         * Adds to the four zone partitions the areas of the given tile
         * It first computes the number of open connections of each zone
         * Then it adds the zones to the different partitions
         * Finally, it connects the rivers and lakes connected
         *
         * @param tile the tile from which to add the areas
         */
        public void addTile(Tile tile) {
            int[] connections = new int[10]; //defines the maximum number of zones
            for (TileSide side : tile.sides()) {
                for (Zone zone : side.zones()) {
                    connections[zone.localId()] += 1;
                    if (zone instanceof Zone.River river && river.hasLake()) {
                        connections[river.lake().localId()] += 1;
                        connections[river.localId()] += 1;
                    }
                }
            }
            for (Zone zone : tile.zones()) {
                switch (zone) {
                    case Zone.Forest forest -> forests.addSingleton(forest, connections[zone.localId()]);
                    case Zone.Meadow meadow -> meadows.addSingleton(meadow, connections[zone.localId()]);
                    case Zone.River river -> {
                        riverSystems.addSingleton(river, connections[zone.localId()]);
                        int riverConnections = river.hasLake() ? connections[zone.localId()] - 1 : connections[zone.localId()];
                        rivers.addSingleton(river, riverConnections);
                    }
                    case Zone.Lake lake -> riverSystems.addSingleton(lake, connections[zone.localId()]);
                }
            }

            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.River river && river.hasLake()) {
                    riverSystems.union(river, river.lake());
                }
            }
        }

        /**
         * Connects the two given sides of the tile
         * @param s1 the first side
         * @param s2 the second side
         * @throws IllegalArgumentException if the sides are not of the same type
         */
        public void connectSides(TileSide s1, TileSide s2) {
            switch(s1) {
                case TileSide.Forest(Zone.Forest f1) -> {
                    if (s2 instanceof TileSide.Forest(Zone.Forest f2)) {
                        forests.union(f1, f2);
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                case TileSide.Meadow(Zone.Meadow m1) -> {
                    if (s2 instanceof TileSide.Meadow(Zone.Meadow m2)) {
                        meadows.union(m1, m2);
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                case TileSide.River(Zone.Meadow rm1, Zone.River r1, Zone.Meadow rm2) -> {
                    if (s2 instanceof TileSide.River(Zone.Meadow rm3, Zone.River r2, Zone.Meadow rm4)) {
                        meadows.union(rm1, rm4);
                        rivers.union(r1, r2);
                        meadows.union(rm2, rm3);
                        riverSystems.union(r1, r2);
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }

        /**
         * Adds the given occupant to the given zone
         * @param player the player to add
         * @param occupantKind the kind of occupant to add
         * @param occupiedZone the zone to add the occupant to
         * @throws IllegalArgumentException if the occupant kind cannot occupy the zone
         */
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone) {
            switch(occupiedZone) {
                case Zone.Forest forest when occupantKind == Occupant.Kind.PAWN-> forests.addInitialOccupant(forest, player);
                case Zone.Meadow meadow when occupantKind == Occupant.Kind.PAWN-> meadows.addInitialOccupant(meadow, player);
                case Zone.River river when occupantKind == Occupant.Kind.PAWN -> rivers.addInitialOccupant(river, player);
                case Zone.Water water when occupantKind == Occupant.Kind.HUT -> riverSystems.addInitialOccupant(water, player);
                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * Removes the given pawn from the given zone
         * @param player the pawn to remove
         * @param occupiedZone the zone to remove the pawn from
         * @throws IllegalArgumentException if the zone is a lake
         */
        public void removePawn(PlayerColor player, Zone occupiedZone) {
            switch(occupiedZone) {
                case Zone.Forest forest -> forests.removeOccupant(forest, player);
                case Zone.Meadow meadow -> meadows.removeOccupant(meadow, player);
                case Zone.River river -> rivers.removeOccupant(river, player);
                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * Removes all the gatherers from the given forest
         * @param forest the forest to remove the gatherers from
         */
        public void clearGatherers(Area<Zone.Forest> forest){
            forests.removeAllOccupantsOf(forest);
        }

        /**
         * Removes all the fishers from the given river
         * @param river the river to remove the fishers from
         */
        public void clearFishers(Area<Zone.River> river){
            rivers.removeAllOccupantsOf(river);
        }

        /**
         * Builds the zone partitions
         * @return the set of the four zone partitions
         */
        public ZonePartitions build() { return new ZonePartitions(forests.build(), meadows.build(), rivers.build(), riverSystems.build()); }

    }
}
