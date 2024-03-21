package ch.epfl.chacun;

import java.util.*;

public class Board {
    private final PlacedTile[] placedTiles;
    private final int[] index;
    private final ZonePartitions partition;
    private final Set<Animal> canceledAnimal;
    public static final int REACH = 12;
    private static final int TOTAL_TILE_COUNT = 625;
    public static final Board EMPTY = new Board(new PlacedTile[TOTAL_TILE_COUNT] , new int[0], ZonePartitions.EMPTY, Set.of());



    ///////// ATTENTION BIEN REMETTRE EN PRIVATE !!!



    public Board(PlacedTile[] placedTiles, int[] index, ZonePartitions partition, Set<Animal> canceledAnimal) {
        this.placedTiles = placedTiles;
        this.index = index;
        this.partition = partition;
        this.canceledAnimal = Collections.unmodifiableSet(canceledAnimal);
    }

    public Board(int[] index, PlacedTile[] placedTiles, ZonePartitions partition) {
        this.placedTiles = placedTiles;
        this.index = index;
        this.partition = partition;
        this.canceledAnimal = Set.of();
    }

    private int index(Pos pos) {
        return (pos.x() + REACH) * 25 + (REACH + pos.y());
    }

    public PlacedTile tileAt(Pos pos) {
        int index = index(pos);
        return index < 0 || index >= placedTiles.length ? null : placedTiles[index];

    }

    public PlacedTile tileWithId(int tileId) {
        for (int index : index) {
            if (placedTiles[index].id() == tileId) {
                return placedTiles[index];
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
                if ((tileAt(new Pos(x - 1, y)) == null) && Math.abs(x-1) <= 12 && Math.abs(y) <= 12) {
                    positions.add(new Pos(x - 1, y));
                }
                if (tileAt(new Pos(x + 1, y)) == null && Math.abs(x+1) <= 12 && Math.abs(y) <= 12) {
                    positions.add(new Pos(x + 1, y));
                }
                if (tileAt(new Pos(x, y - 1)) == null && Math.abs(x) <= 12 && Math.abs(y-1) <= 12) {
                    positions.add(new Pos(x, y - 1));
                }
                if (tileAt(new Pos(x, y + 1)) == null && Math.abs(x) <= 12 && Math.abs(y+1) <= 12) {
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
            PlacedTile tileEast = tileAt(placedTile.pos().neighbor(Direction.E));
            PlacedTile tileWest = tileAt(placedTile.pos().neighbor(Direction.W));
            PlacedTile tileNorth = tileAt(placedTile.pos().neighbor(Direction.N));
            PlacedTile tileSouth = tileAt(placedTile.pos().neighbor(Direction.S));

            if ((tileEast == null || placedTile.tile().e().isSameKindAs(tileEast.tile().w()))
                && (tileWest == null || placedTile.tile().w().isSameKindAs(tileWest.tile().e()))
                && (tileNorth == null || placedTile.tile().n().isSameKindAs(tileNorth.tile().s()))
                && (tileSouth == null || placedTile.tile().s().isSameKindAs(tileSouth.tile().n()))) {
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
        Preconditions.checkArgument(tileAt(tile.pos()) == null);
        PlacedTile[] newPlacedTiles = Arrays.copyOf(placedTiles, placedTiles.length);
        int[] newIndex = Arrays.copyOf(index, index.length + 1);
        int index = index(tile.pos());
        newPlacedTiles[index] = tile;
        newIndex[newIndex.length - 1] = index;

        return new Board(newPlacedTiles, newIndex, partition, canceledAnimal);
    }

    public Board withOccupant(Occupant occupant) {
        int id = Zone.tileId(occupant.zoneId());
        Preconditions.checkArgument(tileWithId(id).occupant() == null);
        PlacedTile addTile = tileWithId(id).withOccupant(occupant);
        PlacedTile[] placedTiles1 = Arrays.copyOf(placedTiles, placedTiles.length);

        placedTiles1[index(tileWithId(id).pos())] = addTile;

        return new Board(placedTiles1, index, partition, canceledAnimal);
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
        Set<Animal> newCancelledAnimals = new HashSet<>(canceledAnimal);
        newCancelledAnimals.addAll(newlyCancelledAnimals);
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

    private boolean isOnBoard(Pos pos) {
        return Math.abs(pos.x()) <= 12 && Math.abs(pos.y()) <= 12;
    }

}
