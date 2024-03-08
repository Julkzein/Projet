package ch.epfl.chacun;

import javax.naming.TimeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

/**
 * This record contains the four zone partitions of the game.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests, ZonePartition<Zone.River> rivers, ZonePartition<Zone.Meadow> meadows, ZonePartition<Zone.Water> riverSystems) {

    /**
     * This constructor creates a set of empty zone partitions
     */
    public final static ZonePartitions EMPTY =
            new ZonePartitions(
                new ZonePartition<Zone.Forest>(),
                new ZonePartition<Zone.River>(),
                new ZonePartition<Zone.Meadow>(),
                new ZonePartition<Zone.Water>());

    /**
     * This class is a builder for zone partitions
     * @param <Z> the type of zone
     */
    public final class Builder<Z extends Zone> {
        private ZonePartition.Builder<Zone.Forest> forests;
        private ZonePartition.Builder<Zone.River> rivers;
        private ZonePartition.Builder<Zone.Meadow> meadows;
        private ZonePartition.Builder<Zone.Water> riverSystems;

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
            int[] connections = new int[10];
            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.River){
                    connections[zone.localId()] += 2;
                } else {
                    if (tile.sideZones().contains(zone)) {
                        for(TileSide t : tile.sides()) {
                            for (Zone otherZone : t.zones()) {
                                if (otherZone.equals(zone)) {
                                    connections[zone.localId()] += 1;
                                }
                            }
                        }
                    }
                }

            }
            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.River river) {
                    if (river.hasLake()) {
                        connections[zone.localId()] -= 1;
                        connections[river.lake().localId()] += 1;
                    }
                }
            }
            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.Forest forest) {
                    forests.addSingleton(forest, connections[zone.localId()]);
                } else if (zone instanceof Zone.River river) {
                    rivers.addSingleton(river, connections[zone.localId()]);
                } else if (zone instanceof Zone.Meadow meadow) {
                    meadows.addSingleton(meadow, connections[zone.localId()]);
                } else if (zone instanceof Zone.Water water) {
                    riverSystems.addSingleton(water, connections[zone.localId()]);
                }
            }
            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.River river) {
                    if (river.hasLake()) {
                        riverSystems.union(river.lake(), river);
                    }
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
                        rivers.union(r1, r2);
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
                case Zone.Forest forest when occupantKind == Occupant.Kind.PAWN-> {
                    for (Area area : forests().areas()) {
                        if (area.zones().contains(forest)) {
                            forests.addInitialOccupant(forest, player);
                            return;
                        }
                    }
                }
                case Zone.Meadow meadow when occupantKind == Occupant.Kind.PAWN-> {
                    for (Area area : meadows().areas()) {
                        if (area.zones().contains(meadow)) {
                            meadows.addInitialOccupant(meadow, player);
                            return;
                        }
                    }
                }
                case Zone.River river when (occupantKind == Occupant.Kind.PAWN || occupantKind == Occupant.Kind.HUT) -> {
                    for (Area area : rivers().areas()) {
                        if (area.zones().contains(river)) {
                            rivers.addInitialOccupant(river, player);
                            return;
                        }
                    }
                }
                case Zone.Lake lake when occupantKind == Occupant.Kind.HUT-> {
                    for (Area area : riverSystems().areas()) {
                        if (area.zones().contains(lake)) {
                            riverSystems.addInitialOccupant(lake, player);
                            return;
                        }
                    }
                }
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
                case Zone.Forest forest -> {
                    for (Area area : forests().areas()) {
                        if (area.zones().contains(forest)) {
                            forests.removeOccupant(forest, player);
                            return;
                        }
                    }
                }
                case Zone.Meadow meadow -> {
                    for (Area area : meadows().areas()) {
                        if (area.zones().contains(meadow)) {
                            meadows.removeOccupant(meadow, player);
                            return;
                        }
                    }
                }
                case Zone.River river -> {
                    for (Area area : rivers().areas()) {
                        if (area.zones().contains(river)) {
                            rivers.removeOccupant(river, player);
                            return;
                        }
                    }
                }
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
        public ZonePartitions build() {
            return new ZonePartitions(forests.build(), rivers.build(), meadows.build(), riverSystems.build());
        }

    }
}
