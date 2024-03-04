package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * This record represents a partition of zones of the given type.
 * It has for sole attribute a set of areas, each of which is a set of zones
 * of a given type
 *
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record ZonePartition<Z extends Zone>(Set<Area<Z>> areas) {

    /**
     * This cosntructor creates a copy of the zone partition
     * with the given areas in order to make it immutable
     */
    public ZonePartition {
        areas = Set.copyOf(areas);
    }


    /**
     * This constructor creates a zone partition with
     * an empty list for partition
     */
    public ZonePartition() {
        this(new HashSet<>());
    }


    /**
     * This method returns the area containing the given zone
     *
     * @param zone : the zone to find
     * @return the area containing the given zone
     * @throws IllegalArgumentException if the zone is not in any area
     */
    public Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas) {
            if (area.zones().contains(zone)) {
                return area;
            }
        }
        throw new IllegalArgumentException();
    }


    /**
     * This static subclass represents a builder for zone partitions
     * @param <Z>
     */
    static class Builder<Z extends Zone>{
        /**
         * The set of areas of the zone partition
         */
        private Set<Area<Z>> areas = new HashSet<>();

        /**
         * This constructor creates a builder with the given zone partition
         *
         * @param zonePartition : the zone partition to build
         */
        public Builder(ZonePartition<Z> zonePartition) {
            areas = zonePartition.areas;
        }

        /**
         * This method adds to the partition a new area containing
         * solely of the given zone and with the given number of open connections
         */
        void addSingleton(Z zone, int openConnections) {
            areas.add(new Area<>(Set.of(zone), new ArrayList<>(), openConnections));
        }


        /**
         * This method adds an occupant of the given color in the given zone
         *
         * @param zone : the zone in wich to add the occupant
         * @param color : the color of the occupat to add
         * @throws IllegalArgumentException if the zone is not in any area
         */
        void addInitialOccupant(Z zone, PlayerColor color) {
            for (Area<Z> area : areas) {
                if (area.zones().contains(zone)) {
                    if (area.occupants().isEmpty()) {
                        areas.add(area.withInitialOccupants(color));
                        areas.remove(area);
                        return;
                    }
                    throw new IllegalArgumentException();
                    //ajouterl le player à la zone => area ou zone?
                }
            }
            throw new IllegalArgumentException();
        }

        /**
         * This method removes the occupant of the given color from the given zone
         *
         * @param zone : the zone from which to remove the occupant
         * @param color : the color of the occupant to remove
         * @throws IllegalArgumentException if the zone is not in any area or if the zone
         * does not contain any occupant of the given color
         */
        void removeOccupant(Z zone, PlayerColor color) {
            for (Area<Z> area : areas) {
                if (area.zones().contains(zone)) {
                    if (area.occupants().contains(color)) {
                        areas.add(area.withoutOccupant(color));
                        areas.remove(area);
                        return;
                    }
                    throw new IllegalArgumentException();
                }
            }
            throw new IllegalArgumentException();
        }

        /**
         * This method removes all the occupants from the given zone
         *
         * @param area: the area from which to remove all the occupants
         * @throws IllegalArgumentException if the zone is not in any area
         */
        void removeAllOccupants(Area<Z> area) {
            for (Area<Z> area1 : areas) {
                if (area1.equals(area)) {
                    areas.add(area.withoutOccupants());
                    areas.remove(area);
                    return;
                }
            }
            throw new IllegalArgumentException();
        }


        /**
         * This method conncects the two given zones to create one larger area
         *
         * @param zone1 : the first zone to add to the area
         * @param zone2 : the second zone to add to the area
         */
        void union(Z zone1, Z zone2) {
            for(Area<Z> area : areas) {
                if(area.zones().contains(zone1)) {
                    for(Area<Z> area2 : areas) {
                        if(area2.zones().contains(zone2)) {
                            areas.add(area.connectTo(area2));
                            areas.removeAll(Set.of(area, area2));
                            return;
                        }
                    }
                }
            }
            throw new IllegalArgumentException();
        }

        /**
         * This method builds the zone partition
         *
         * @return the zone partition built by the builder
         */
        ZonePartition<Z> build() {
            return new ZonePartition<>(areas);
        }
    }
}
