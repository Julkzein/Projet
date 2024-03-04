package ch.epfl.chacun;

import java.util.*;

/**
 * Represent the area that are composed of several zones 
 *
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record Area<Z extends Zone>(Set<Z> zones, List<PlayerColor> occupants, int openConnections) {
    /**
     * This constructor creates an area with the given zones, occupants and open connections
     *
     * @param zones : the set of zones of the area
     * @param occupants : the list of occupants of the area
     * @param openConnections : the number of open connections of the area
     * @throws IllegalArgumentException if the open connections is negative
     */
    public Area {
        if (openConnections < 0) {
            throw new IllegalArgumentException();
        }
        Collections.sort(occupants);
        occupants = List.copyOf(occupants);
    }

    /**
     * This method returns true is the given forest has a menhir
     *
     * @param forest the forest to search in
     * @return true if the given forest has a menhir
     */
    public static boolean hasMenhir(Area<Zone.Forest> forest) {
        for (Zone.Forest zone : forest.zones) {
            if (zone.kind() == Zone.Forest.Kind.WITH_MENHIR) {
                return true;
            }
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
            if (zone.kind() == Zone.Forest.Kind.WITH_MUSHROOMS) {
                i += 1;
            }
        }
        return i;
    }

    /**
     * This method returns the number of animals in the given meadow not
     * considering the animals in the given set of cancelled animals
     *
     * @param meadow the meadow to search in
     * @param cancelledAnimals the set of animals to cancel
     * @return the number of animals in the given meadow
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
     * This method returns the number fish in the given river and
     * possible lakes attached to the river
     * 
     * @param river the river to search in
     * @return the number of fish in the given river
     */
    public static int riverFishCount(Area<Zone.River> river) {
        int i = 0;
        Set<Zone.Lake> lakes = new HashSet<>();
        for (Zone.River rivers : river.zones) {
            i += rivers.fishCount();
            if (rivers.hasLake() && lakes.add(rivers.lake())) {
                i += rivers.lake().fishCount();
            }
        }
        return i;
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
            if (waterBody instanceof Zone.Lake) {
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
    public int lakeCount(Area<Zone.Water> riverSystem) {
        int i = 0;
        Set<Zone.Lake> lakes = new HashSet<>();
        for (Zone.Water waterBody : riverSystem.zones) {
            if (waterBody instanceof Zone.Lake) {
                i += 1;
            }
            if (waterBody instanceof Zone.River river) {
                if (river.hasLake() && lakes.add(river.lake())) {
                    i += 1;
                }
            }
        }
        return i;
    }


    public boolean isClosed() {
        return openConnections == 0;
    }

    public boolean isOccupied() {
        return occupants.size() > 0;
    }

    public Set<PlayerColor> majorityOccupants() {
        int index = PlayerColor.values().length;
        int[] occupantCount = new int[index];
        for (PlayerColor occupant : occupants) {
            occupantCount[occupant.ordinal()] += 1;
        }
        int max = 0;
        for (int i = 0; i < index; i++) {
            if (occupantCount[i] > max) {
                max = occupantCount[i];
            }
        }
        Set<PlayerColor> majorityOccupantsSet = new HashSet<>();
        for (PlayerColor occupant : PlayerColor.ALL) {
            if (occupantCount[occupant.ordinal()] == max) {
                majorityOccupantsSet.add(occupant);
            }
        }
        return majorityOccupantsSet;
    }

    public Area<Z> connectTo(Area<Z> that) {
        if (this == that) {
            return new Area<>(this.zones, this.occupants, this.openConnections);
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

    public Area<Z> withInitialOccupants(PlayerColor occupant) {
        if (this.isOccupied()) {
            throw new IllegalArgumentException();
        } else {
            List<PlayerColor> newOccupants = new ArrayList<>(this.occupants);
            newOccupants.add(occupant);
            return new Area<>(this.zones, newOccupants, this.openConnections);
        }
    }

    public Area<Z> withoutOccupant(PlayerColor occupant) {
        if (!this.occupants.contains(occupant)) {
            throw new IllegalArgumentException();
        } else {
            List<PlayerColor> newOccupants = new ArrayList<>(this.occupants);
            newOccupants.remove(occupant);
            return new Area<>(this.zones, newOccupants, this.openConnections);
        }
    }

    public Area<Z> withoutOccupants() {
        return new Area<>(this.zones, new ArrayList<>(), this.openConnections);
    }

    public Set<Integer> tileIds() {
        Set<Integer> tileIds = new HashSet<>();
        for (Zone zone : zones) {
            tileIds.add(zone.tileId());
        }
        return tileIds;
    }

    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower) {
        for (Zone zone : zones) {
            if (zone.specialPower() == specialPower) {
                return zone;
            }
        }
        return null;
    }
}
