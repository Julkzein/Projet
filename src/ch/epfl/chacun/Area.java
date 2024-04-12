package ch.epfl.chacun;

import java.util.*;

/**
 * Represent an area that are composed of several zones
 *
 * @param zones : the set of zones of the area
 * @param occupants : the list of occupants of the area
 * @param openConnections : the number of open connections of the area
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record Area<Z extends Zone>(Set<Z> zones, List<PlayerColor> occupants, int openConnections) {
    /**
     * This constructor creates an area with the given zones, occupants and open connections
     *
     * @throws IllegalArgumentException if the open connections is negative
     */
    public Area {
        Preconditions.checkArgument(openConnections >= 0);
        List<PlayerColor> sortedOccupants = new ArrayList<>(occupants);
        Collections.sort(sortedOccupants);
        occupants = List.copyOf(sortedOccupants);
        zones = Set.copyOf(zones);
    }

    /**
     * This method returns true is the given forest has a menhir
     *
     * @param forest the forest to search in
     * @return true if the given forest has a menhir
     */
    public static boolean hasMenhir(Area<Zone.Forest> forest) {
        for (Zone.Forest zone : forest.zones) {
            if (zone.kind() == Zone.Forest.Kind.WITH_MENHIR) return true;
        }
        return false;
    }

    /**
     * This method returns the number of mushroom groups in the given forest
     *
     * @param forest the forest to search in
     * @return the number of mushroom groups in the given forest
     */
    public static int mushroomGroupCount(Area<Zone.Forest> forest) {
        int i = 0;
        for (Zone.Forest zone : forest.zones) {
            if (zone.kind() == Zone.Forest.Kind.WITH_MUSHROOMS) i += 1;
        }
        return i;
    }

    /**
     * This method returns the set of animals in the given meadow not
     * considering the animals in the given set of cancelled animals
     *
     * @param meadow the meadow to search in
     * @param cancelledAnimals the set of animals to cancel
     * @return the set of animals in the given meadow
     */
    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> animals = new HashSet<>();
        for (Zone.Meadow meadows : meadow.zones) {
            animals.addAll(meadows.animals());
        }
        animals.removeAll(cancelledAnimals);
        return animals;
    }


    /**
     * This method returns the number of fish in the given river and
     * possible lakes attached to the river
     * 
     * @param river the river to search in
     * @return the number of fish in the given river
     */
    public static int riverFishCount(Area<Zone.River> river) {
        int fishCount = 0;
        Set<Zone.Lake> lakes = new HashSet<>();
        for (Zone.River rivers : river.zones) {
            fishCount += rivers.fishCount();
            if (rivers.hasLake() && lakes.add(rivers.lake())) fishCount += rivers.lake().fishCount();
        }
        return fishCount;
    }

    /**
     * This method returns the number fish in the given river system and
     * possible lakes attached to the river
     *
     * @param riverSystem the river system to search in
     * @return the number of fish in the given river system
     */
    public static int riverSystemFishCount(Area<Zone.Water> riverSystem) {
        int i = 0;
        Set<Zone.Lake> lakes = new HashSet<>();
        for (Zone.Water waterBody : riverSystem.zones) {
            if (waterBody instanceof Zone.Lake && lakes.add((Zone.Lake) waterBody)) {
                i += waterBody.fishCount();
            }
            if (waterBody instanceof Zone.River river) {
                i+= waterBody.fishCount();
                if (river.hasLake() && lakes.add(river.lake())) {
                    i += river.lake().fishCount();
                }
            }
        }
        return i;
    }

    /**
     * This method returns the number of lakes in the given hydrographic system
     *
     * @param riverSystem the river system to search in
     * @return the number of lakes in the given river system
     */
    public static int lakeCount(Area<Zone.Water> riverSystem) {
        Set<Zone.Lake> lakes = new HashSet<>();
        for (Zone.Water waterBody : riverSystem.zones) {
            if (waterBody instanceof Zone.River river && river.hasLake()) {
                lakes.add(river.lake());
            }
            if (waterBody instanceof Zone.Lake) {
                lakes.add((Zone.Lake) waterBody);
            }
        }
        return lakes.size();
    }


    /**
     * This method checks if the area is closed
     *
     * @return true if and only if th area is
     * closed (it doesn't have any open connections)
     */
    public boolean isClosed() {
        return openConnections == 0;
    }

    /**
     * This method checks if the area is occupied
     *
     * @return true if and only if the area has at least one occupied
     */
    public boolean isOccupied() {
        return !occupants.isEmpty();
    }

    /**
     * This method returns the set containing the occupants of the
     * color majority
     *
     * @return a set containing the occupants of the color majority
     */
    public Set<PlayerColor> majorityOccupants() {
        int index = PlayerColor.values().length;
        int[] occupantCount = new int[index];
        for (PlayerColor occupant : occupants) {
            occupantCount[occupant.ordinal()] += 1;
        }
        int max = 0;
        for (int i = 0; i < index; i++) {
            if (occupantCount[i] > max) max = occupantCount[i];
        }
        if (max == 0) {
            return new HashSet<>();
        }
        Set<PlayerColor> majorityOccupantsSet = new HashSet<>();
        for (PlayerColor occupant : PlayerColor.ALL) {
            if (occupantCount[occupant.ordinal()] == max) {
                for (int i = 0; i < max; i++) {
                    majorityOccupantsSet.add(occupant);
                }

            }
        }
        return majorityOccupantsSet;
    }

    /**
     * This method connects two areas together
     *
     * @param that the area we want to connect this area to
     * @return the new area resulting of the connection between the two areas
     */
    public Area<Z> connectTo(Area<Z> that) {
        if (this.equals(that)) {
            return new Area<>(this.zones, this.occupants, this.openConnections - 2);
        }
        else {
            Set<Z> combinedZones = new HashSet<>(this.zones);
            combinedZones.addAll(that.zones);
            List<PlayerColor> combinedOccupants = new ArrayList<>(this.occupants);
            combinedOccupants.addAll(that.occupants);
            int combinedOpenConnections = this.openConnections + that.openConnections;
            return new Area<>(combinedZones, combinedOccupants, combinedOpenConnections - 2);
        }
    }

    /**
     * This method adds an occupant to the current area
     *
     * @param occupant the occupant we desire to add to the current area
     * @return and identical area to the current one but with the given occupant
     */
    public Area<Z> withInitialOccupant(PlayerColor occupant) {
        Preconditions.checkArgument(!this.isOccupied());
        return new Area<>(zones, List.of(occupant), openConnections);
    }

    /**
     * This method removes an occupant of the given color in the current area
     * throws an IllegalArgumentException if the area does not contain an occupant
     * of the given color
     *
     * @param occupant the occupant we desire to remove
     * @return an identical area to the current one but with the given occupant removed
     */
    public Area<Z> withoutOccupant(PlayerColor occupant) {
        Preconditions.checkArgument(occupants.contains(occupant));
        List<PlayerColor> newOccupants = new ArrayList<>(occupants);
        newOccupants.remove(occupant);
        return new Area<>(zones, newOccupants, openConnections);
    }

    /**
     * This method removes all the occupants of the current zone 
     * 
     * @return an identical area to the current one but without any occupants
     */
    public Area<Z> withoutOccupants() {
        return new Area<>(zones, new ArrayList<>(), openConnections);
    }

    /**
     * This method returns a set containing the id of the zones composed of the area
     *
     * @return a set of all the ids of the different zones containing the area
     */
    public Set<Integer> tileIds() {
        Set<Integer> tileIds = new HashSet<>();
        for (Zone zone : zones) {
            tileIds.add(zone.tileId());
        }
        return tileIds;
    }

    /**
     * This method returns the zone of the area containing the given special
     * power or null if no zone contains this special power
     *
     * @param specialPower the special power of the zone we desire
     * @return the zone of the area that posses the special power
     */
    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower) {
        for (Zone zone : zones) {
            if (zone.specialPower() == specialPower) return zone;
        }
        return null;
    }
}
