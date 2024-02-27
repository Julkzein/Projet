package ch.epfl.chacun;

import java.util.*;

/**
 * Represent the area that are composed of several zones 
 *
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record Area<Z extends Zone>(Set<Z> zones, List<PlayerColor> occupants, int openConnections) {
    public Area {
        if (openConnections < 0) {
            throw new IllegalArgumentException();
        }
        Collections.sort(occupants);
        occupants = List.copyOf(occupants);
    }

    public static boolean hasMenhir(Area<Zone.Forest> forest) {
        for (Zone.Forest zone : forest.zones) {
            if (zone.kind() == Zone.Forest.Kind.WITH_MENHIR) {
                return true;
            }
        }
        return false;
    }

    public static int mushroomGroupCount(Area<Zone.Forest> forest) {
        int i = 0;
        for (Zone.Forest zone : forest.zones) {
            if (zone.kind() == Zone.Forest.Kind.WITH_MUSHROOMS) {
                i += 1;
            }
        }
        return i;
    }

    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> animals = new HashSet<>();
        for (Zone.Meadow meadows : meadow.zones) {
            animals.addAll(meadows.animals());
        }
        animals.removeAll(cancelledAnimals);
        return animals;
    }

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
        int occupantCount [] = new int[index];
        for (PlayerColor occupant : occupants) {
            switch (occupant) {
                case RED -> occupantCount[0] += 1;
                case BLUE -> occupantCount[1] += 1;
                case GREEN -> occupantCount[2] += 1;
                case YELLOW -> occupantCount[3] += 1;
                case PURPLE -> occupantCount[4] += 1;
            }
        }

        int max = 0;
        for (int i = 0; i < index; i++) {
            if (occupantCount[i] > max) {
                max = occupantCount[i];
            }
        }

        Set<PlayerColor> majorityOccupantsSet = new HashSet<>();
        for (int i = 0; i < index; i++) {
            if (occupantCount[i] == max) {
                switch (i) {
                    case 0 -> majorityOccupantsSet.add(PlayerColor.RED);
                    case 1 -> majorityOccupantsSet.add(PlayerColor.BLUE);
                    case 2 -> majorityOccupantsSet.add(PlayerColor.GREEN);
                    case 3 -> majorityOccupantsSet.add(PlayerColor.YELLOW);
                    case 4 -> majorityOccupantsSet.add(PlayerColor.PURPLE);
                }
            }
        }
        return majorityOccupantsSet;
    }

    public Area<Z> connectTo(Area<Z> that) {
        Set<Z> combinedZones = new HashSet<>(this.zones());
        combinedZones.addAll(that.zones());

        List<Occupant> combinedOccupants = new ArrayList<>(this.occupants());
        combinedOccupants.addAll(that.occupants());

        int combinedOpenConnections = this.openConnections() + that.openConnections();

        return new Area<>(combinedZones, combinedOccupants, combinedOpenConnections);
    }
}
