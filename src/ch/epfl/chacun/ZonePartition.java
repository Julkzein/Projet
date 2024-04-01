package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * This record represents a partition of zones of the given type.
 *
 * @param areas the set of areas composing the partition
 * @param <Z> The generic type used to allow the zone builder to create partitions of different types of zone
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record ZonePartition<Z extends Zone>(Set<Area<Z>> areas) {

    /**
     * This constructor creates a copy of the zone partition
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
    public static final class Builder<Z extends Zone>{
        /**
         * The set of areas of the zone partition
         */
        private Set<Area<Z>> areas;

        /**
         * This constructor creates a builder with the given zone partition
         *
         * @param zonePartition : the zone partition to build
         */
        public Builder(ZonePartition<Z> zonePartition) {
            areas = new HashSet<>(zonePartition.areas);
        }

        /**
         * This method adds to the partition a new area containing solely
         * a zone of the given zone and with the given number of open connections
         */
        public void addSingleton(Z zone, int openConnections) {
            areas.add(new Area<>(Set.of(zone), new ArrayList<>(), openConnections));
        }


        /**
         * This method adds an occupant of the given color in the given zone
         *
         * @param zone : the zone in which to add the occupant
         * @param color : the color of the occupant to add
         * @throws IllegalArgumentException if the zone is not in any area
         */
        public void addInitialOccupant(Z zone, PlayerColor color) {
            for (Area<Z> area : areas) {
                if (area.zones().contains(zone)) {
                    Preconditions.checkArgument(!area.isOccupied());
                    areas.add(area.withInitialOccupant(color));
                    areas.remove(area);
                    return;
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
        public void removeOccupant(Z zone, PlayerColor color) {
            for (Area<Z> area : areas) {
                if (area.zones().contains(zone) && area.isOccupied() && area.occupants().contains(color)) {
                    areas.add(area.withoutOccupant(color));
                    areas.remove(area);
                    return;
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
        public void removeAllOccupantsOf(Area<Z> area) {
            Preconditions.checkArgument(areas.contains(area));
            for (Area<Z> area1 : areas) {
                if (area1.equals(area)) {
                    areas.remove(area);
                    areas.add(area.withoutOccupants());
                    return;
                }
            }
            throw new IllegalArgumentException();

        }



        /**
         * This method connects the two given zones to create a larger area
         *
         * @param zone1 : the first zone to add to the area
         * @param zone2 : the second zone to add to the area
         */
        public void union(Z zone1, Z zone2) {
            Area<Z> area1 = null;
            Area<Z> area2 = null;
            for(Area<Z> area : areas) {
                if (area1 != null && area2 != null) {
                    break;
                }
                if (area.zones().contains(zone1) && area1 == null) {
                    area1 = area;
                }
                if (area.zones().contains(zone2) && area2 == null) {
                    area2 = area;
                }
            }
            Preconditions.checkArgument(area1 != null && area2 != null);
            areas.add(area1.connectTo(area2));
            areas.remove(area1);
            areas.remove(area2);
        }

        /**
         * This method builds the zone partition
         *
         * @return the zone partition built by the builder
         */
        public ZonePartition<Z> build() {
            return new ZonePartition<>(areas);
        }
    }
}
