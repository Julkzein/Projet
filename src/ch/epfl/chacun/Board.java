package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Set;

import static java.lang.reflect.Array.get;

public class Board {
    private final PlacedTile[] placedTiles;
    private final int[] index;
    private final ZonePartitions partition;
    private final Set<Animal> canceledAnimal;
    public static final int reach = 12;
    public static final Board EMPTY = new Board(new PlacedTile[625] , new int[95], ZonePartitions.EMPTY, Set.of());

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
        throw new IllegalArgumentException();
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

    public Set<Area<Zone.Water>> riverSystemAreas() {
        return partition.riverSystems().areas();
    }

    /**
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {

    }
     */

    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        int count = 0;
        for (PlacedTile tile : placedTiles) {
            if (tile != null && tile.occupant().kind() == occupantKind && tile.placer() == player) {
                count++;
            }
        }
        return count;
    }

    public Set<Pos> insertionPositions() {
        Set<Pos> positions = new HashSet<>();
        for(PlacedTile placedTile : placedTiles) {
            if(!(placedTile == null)) {
                int x = placedTile.pos().x();
                int y = placedTile.pos().y();
                if (tileAt(new Pos(x - 1, y)) == null) {
                    positions.add(new Pos(x - 1, y));
                }
                if (tileAt(new Pos(x + 1, y)) == null) {
                    positions.add(new Pos(x + 1, y));
                }
                if (tileAt(new Pos(x, y - 1)) == null) {
                    positions.add(new Pos(x, y - 1));
                }
                if (tileAt(new Pos(x, y + 1)) == null) {
                    positions.add(new Pos(x, y + 1));
                }
            }
        }
        return positions;
    }

    public PlacedTile lastPlacedTile() {
        for (int i = index.length - 1; i >= 0; i--) {
            if(index[i] != 0) {
                return placedTiles[index[i]];
            }
        }
        return null;
    }

    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        Set<Area<Zone.Forest>> closedForests = new HashSet<>();
        PlacedTile lastTile = lastPlacedTile();
        for (Area<Zone.Forest> forestArea : partition.forests().areas()) {
            if (forestArea.openConnections() == 0) {
                for (Zone.Forest forest : forestArea.zones()) {
                    if (lastTile.tile().zones().contains(forest)) {
                        closedForests.add(forestArea);
                    }
                }
            }
        }
        return closedForests;
    }

    public Set<Area<Zone.River>> riversClosedByLastTile() {
        Set<Area<Zone.River>> closedRivers = new HashSet<>();
        PlacedTile lastTile = lastPlacedTile();
        for (Area<Zone.River> riverArea : partition.rivers().areas()) {
            if (riverArea.openConnections() == 0) {
                for (Zone.River river : riverArea.zones()) {
                    if (lastTile.tile().zones().contains(river)) {
                        closedRivers.add(riverArea);
                    }
                }
            }
        }
        return closedRivers;
    }

    public boolean canAddTile(PlacedTile placedTile) {
        if (insertionPositions().contains(placedTile.pos())) {
            PlacedTile tileEast = tileAt(new Pos(placedTile.pos().x() + 1, placedTile.pos().y()));
            PlacedTile tileWest = tileAt(new Pos(placedTile.pos().x() - 1, placedTile.pos().y()));
            PlacedTile tileNorth = tileAt(new Pos(placedTile.pos().x(), placedTile.pos().y() - 1));
            PlacedTile tileSouth = tileAt(new Pos(placedTile.pos().x(), placedTile.pos().y() + 1));

            if (placedTile.tile().e().isSameKindAs(tileEast.tile().w())
                && placedTile.tile().w().isSameKindAs(tileWest.tile().e())
                && placedTile.tile().n().isSameKindAs(tileNorth.tile().s())
                && placedTile.tile().s().isSameKindAs(tileSouth.tile().n())) {
                return true;
            }
        }
        return false;
    }
}
