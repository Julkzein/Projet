package ch.epfl.chacun;

import java.util.*;

import static ch.epfl.chacun.Zone.localId;
import static java.lang.reflect.Array.get;
import static java.util.List.copyOf;

public class Board {
    private final PlacedTile[] placedTiles;
    private final int[] index;
    private final ZonePartitions partition;
    private final Set<Animal> canceledAnimal;
    public static final int reach = 12;
    public static final Board EMPTY = new Board(new PlacedTile[625] , new int[0], ZonePartitions.EMPTY, Set.of());
    /////////
    public Board(PlacedTile[] placedTiles, int[] index, ZonePartitions partition, Set<Animal> canceledAnimal) {
        this.placedTiles = placedTiles;
        this.index = index;
        this.partition = partition;
        this.canceledAnimal = canceledAnimal;
    }

    public Board(int[] index, PlacedTile[] placedTiles, ZonePartitions partition) {
        this.placedTiles = placedTiles;
        this.index = index;
        this.partition = partition;
        this.canceledAnimal = Set.of();
    }

    public PlacedTile tileAt(Pos pos) {
        int index = (pos.x() + reach) * 25 + (reach + pos.y());
        return index < 0 || index >= placedTiles.length ? null : placedTiles[index];

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

    private boolean isAdjacent(Pos pos1, Pos pos2) {
        return Math.abs(pos1.x() - pos2.x()) + Math.abs(pos1.y() - pos2.y()) == 1;
    }
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
            Set<Zone.Meadow> meadowsInArea = new HashSet<>();
            Set<Zone.Meadow> adjacentMeadows = new HashSet<>();
            List<PlayerColor> occupants = new ArrayList<>();
            for (Area area : meadowAreas()) {
                if (area.zones().contains(meadowZone)) {
                    Set<Zone.Meadow> zones = area.zones();
                    occupants = area.occupants();
                    for (Zone.Meadow zone : zones) {
                        meadowsInArea.add(zone);
                    }
                }
            }
            for (PlacedTile placedTile : placedTiles) {
                if (isAdjacent(placedTile.pos(), pos)) {
                    for (Zone zone : placedTile.tile().zones()) {
                        if (zone instanceof Zone.Meadow meadow && meadowsInArea.contains(zone)) {
                            adjacentMeadows.add(meadow);
                        }
                    }
                }
            }
            return new Area<>(adjacentMeadows, occupants, 0);
    }

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
            if(placedTile != null) {
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
        if (index.length > 0) {
            return placedTiles[index[index.length - 1]];
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

    public boolean couldPlaceTile(Tile tile) {
        for (Pos position : insertionPositions()) {
            for (Rotation rot : Rotation.ALL) {
                if (canAddTile(new PlacedTile(tile, PlayerColor.RED, rot, position))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Board withNewTile(PlacedTile tile) {
        if (placedTiles.length != 0 && !canAddTile(tile)) {
            throw new IllegalArgumentException();
        }

        PlacedTile[] newPlacedTiles = new PlacedTile[placedTiles.length + 1];
        int[] newIndex = new int[index.length + 1];
        for (int i = 0; i < placedTiles.length; i++) {
            newPlacedTiles[i] = placedTiles[i];
            newIndex[i] = index[i];
        }
        newPlacedTiles[placedTiles.length] = tile;
        newIndex[index.length] = (tile.pos().x() + reach) * 25 + (reach + tile.pos().y());

        return new Board(newPlacedTiles, newIndex, partition, canceledAnimal);
    }

    public Board withOccupant(Occupant occupant) {
        for (PlacedTile placedTile : placedTiles) {
            if (occupant.zoneId() == placedTile.tile().id()) {
                if (placedTile.occupant() == null) {
                    throw new IllegalArgumentException();
                } else {
                    placedTile = placedTile.withOccupant(occupant);
                }
            }
        }
        return new Board(placedTiles, index, partition, canceledAnimal);
    }

    public Board withoutOccupant(Occupant occupant) {
        for (PlacedTile placedTile : placedTiles) {
            if (occupant.zoneId() == placedTile.tile().id()) {
                if (placedTile.occupant() == occupant) {
                    placedTile = placedTile.withNoOccupant();
                }
            }
        }
        return new Board(placedTiles, index, partition, canceledAnimal);
    }

    public Board withoutGatherersOrFishersIn(Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers) {
        ZonePartitions.Builder newBoardZonePartitionsBuilder = new ZonePartitions.Builder<>(partition);
        for (Area<Zone.Forest> forestArea : forests) {
            newBoardZonePartitionsBuilder.clearGatherers(forestArea);
        }
        for (Area<Zone.River> riverArea : rivers) {
            newBoardZonePartitionsBuilder.clearFishers(riverArea);
        }
        ZonePartitions newBoardZonePartitions = newBoardZonePartitionsBuilder.build();
        return new Board(placedTiles, index, newBoardZonePartitions, canceledAnimal);
    }

    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        //Set<Animal> newCancelledAnimals = new HashSet<>(canceledAnimal);
       // newCancelledAnimals.addAll(newlyCancelledAnimals);
        //return new Board(placedTiles, index, partition, Set.copyOf(newCancelledAnimals));
        Set<Animal> newCancelledAnimals = Set.copyOf(newlyCancelledAnimals);
        newCancelledAnimals.addAll(canceledAnimal);
        return new Board(placedTiles, index, partition, newCancelledAnimals);
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) return false;
        if (!(that instanceof Board board)) return false;
        return Arrays.equals(board.index, index) && Arrays.equals(board.placedTiles, placedTiles) &&  board.partition.equals(partition) && board.canceledAnimal.equals(canceledAnimal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(index), Arrays.hashCode(placedTiles), partition, canceledAnimal);
    }

}
