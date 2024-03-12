package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Set;

import static java.lang.reflect.Array.get;

public class Board {
    private final PlacedTile[] placedTiles;
    private final int[] index;
    private final ZonePartitions partition;
    private final Set<Animal> canceledAnimal;
    public static int reach = 12;
    public static Board EMPTY;

    private Board(PlacedTile[] placedTiles, int[] index, ZonePartitions partition, Set<Animal> canceledAnimal) {
        this.placedTiles = placedTiles;
        this.index = index;
        this.partition = partition;
        this.canceledAnimal = canceledAnimal;
    }

    public PlacedTile tileAt(Pos pos) {
        int index = (pos.x() + reach) * 25 + reach + pos.y();
        for (int i = 0; i < this.index.length; i++) {
            if (this.index[i] == index) {
                return placedTiles[index];
            }
        }
        return null;
    }

    public PlacedTile tileWithId(int tileId) {
        for (PlacedTile placedTile : placedTiles) {
            if (placedTile.tile().id() == tileId) {
                return placedTile;
            }
        }
        return null;
    }

    public Set<Animal> canceledAnimal() {
        return canceledAnimal;
    }

    public Set<Occupant> occupants() {
        Set<Occupant> occupants = new HashSet<>();
        for (PlacedTile placedTile : placedTiles) {
            if (placedTile != null) {
                occupants.addAll(Set.of(placedTile.occupant()));
            }
        }
        return occupants;
    }

    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
         for(Area<Zone.Forest> forestArea : partition.forests().areas()) {
             if(forestArea.zones().contains(forest)) {
                 return forestArea;
             }
         }
         throw new IllegalArgumentException();
    }

    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        for(Area<Zone.Meadow> meadowArea : partition.meadows().areas()) {
            if(meadowArea.zones().contains(meadow)) {
                return meadowArea;
            }
        }
        throw new IllegalArgumentException();
    }

    public Area<Zone.River> riverArea(Zone.River riverZone) {
        for(Area<Zone.River> riverArea : partition.rivers().areas()) {
            if(riverArea.zones().contains(riverZone)) {
                return riverArea;
            }
        }
        throw new IllegalArgumentException();
    }

    public Area<Zone.Water> riverSystemArea(Zone.Water waterZone) {
        for(Area<Zone.Water> riverSystemArea : partition.riverSystems().areas()) {
            if(riverSystemArea.zones().contains(waterZone)) {
                return riverSystemArea;
            }
        }
        throw new IllegalArgumentException();
    }

    public Set<Area<Zone.Meadow>> meadowAreas() {
        return partition.meadows().areas();
    }
}
