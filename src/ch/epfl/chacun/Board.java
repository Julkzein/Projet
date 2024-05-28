package ch.epfl.chacun;

import java.util.*;


/**
 * This class represents the board of the game. It is composed of several private and
 * public attributes that describe the board and its tiles. These attributes are:
 * a PlacedTile array describing all the tiles that have been placed on the board, an
 * int array describing the indexes of the placed tiles in the order they have been placed,
 * a ZonePartitions describing a partition of all the zones on the board, a Set of Animals
 * describing the currently cancelled animals of the board, a constant REACH describing the
 * number of tiles separating the center of the board from its edges.
 *
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public final class Board {
    private final PlacedTile[] placedTiles; //an array of all the tiles placed on the board
    private final int[] index; //an array of the indexes of the tiles placed on the board in the order they were placed
    private final ZonePartitions partition; //all the partitions of the board
    private final Set<Animal> cancelledAnimals; //a set of all the canceled animals
    public static final int REACH = 12; //the distance in tiles separating the middle tile from the sides
    public static final int WIDTH = 2 * REACH + 1; //the width of the board
    private static final int TOTAL_TILE_COUNT = WIDTH * WIDTH; //the total amount of tiles of the board
    public static final Board EMPTY = new Board(new PlacedTile[TOTAL_TILE_COUNT] , new int[0], ZonePartitions.EMPTY, Set.of()); //a totally empty board


    /**
     * This constructor creates a new Board with the given parameters.
     *
     * @param placedTiles the array of placed tiles on the board
     * @param index the array of indexes of the placed tiles
     * @param partition the partition of the zones on the board
     * @param cancelledAnimals the set of cancelled animals on the board
     */
    private Board(PlacedTile[] placedTiles, int[] index, ZonePartitions partition, Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.index = index;
        this.partition = partition;
        this.cancelledAnimals = Collections.unmodifiableSet(cancelledAnimals);
    }


    /**
     * This method gives the index of a placed tile at a given position.
     *
     * @param pos the position of the tile
     * @return the index of the tile at the given position
     */
    private int index(Pos pos) {
        return (pos.x() + REACH) + (REACH + pos.y()) * WIDTH;
    }

    private boolean invalidPos(Pos pos) {
        int i = index(pos);
        return i < 0 || i >= placedTiles.length || Math.abs(pos.x()) > REACH || Math.abs(pos.y()) > REACH;
    }

    /**
     * This method returns the tile at the given position.
     *
     * @param pos the position of the tile at the given position.
     * @return the tile at the given position
     */
    public PlacedTile tileAt(Pos pos) {
        int i = index(pos);
        return invalidPos(pos) ? null : placedTiles[i];
    }

/**
     * This method returns the placed tile that has the given id or throws a new
     * IllegalArgumentException if no such tile exists on the board.
     *
     * @param tileId the id of the tile to be found
     * @throws IllegalArgumentException if no such tile exists on the board
     * @return the placed tile with given id
     */
    public PlacedTile tileWithId(int tileId) {
        for (int i : index) {
            if (placedTiles[i].id() == tileId) return placedTiles[i];
        }
        throw new IllegalArgumentException();
    }

    /**
     * This method returns the set of cancelled animals on the board.
     *
     * @return the set of cancelled animals on the board.
     */
    public Set<Animal> cancelledAnimals() {
        return cancelledAnimals;
    }

    /**
     * This method returns the set of occupants on the board.
     *
     * @return the set of occupants on the board.
     */
    public Set<Occupant> occupants() {
        Set<Occupant> occupants = new HashSet<>();
        for (int i : index) {
            if (placedTiles[i] != null && placedTiles[i].occupant() != null) {
                occupants.add(placedTiles[i].occupant());
            }
        }
        return occupants;
    }

    /**
     * This method returns the area containing the given forest zone
     * or throws a new IllegalArgumentException if no such zone belongs
     * to the board.
     *
     * @param forest the forest zone to be found
     * @throws IllegalArgumentException if no such zone belongs to the board
     * @return the area containing the given forest zone
     */
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
         for (Area<Zone.Forest> forestArea : partition.forests().areas()) {
             if (forestArea.zones().contains(forest)) return forestArea;
         }
         throw new IllegalArgumentException();
    }

    /**
     * This method returns the area containing the given meadow zone
     * or throws a new IllegalArgumentException if no such zone belongs
     * to the board.
     *
     * @param meadow the meadow zone to be found
     * @throws IllegalArgumentException if no such zone belongs to the board
     * @return the area containing the given meadow zone
     */
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        for (Area<Zone.Meadow> meadowArea : partition.meadows().areas()) {
            if (meadowArea.zones().contains(meadow)) return meadowArea;
        }
        throw new IllegalArgumentException();
    }


    /**
     * This method returns the area containing the given river zone
     * or throws a new IllegalArgumentException if no such zone belongs
     * to the board.
     *
     * @param riverZone the river zone to be found
     * @throws IllegalArgumentException if no such zone belongs to the board
     * @return the area containing the given river zone
     */
    public Area<Zone.River> riverArea(Zone.River riverZone) {
        for (Area<Zone.River> riverArea : partition.rivers().areas()) {
            if (riverArea.zones().contains(riverZone)) return riverArea;
        }
        throw new IllegalArgumentException();
    }

    /**
     * This method returns the area containing the given water zone
     * or throws a new IllegalArgumentException if no such zone belongs
     * to the board.
     *
     * @param waterZone the water zone to be found
     * @throws IllegalArgumentException if no such zone belongs to the board
     * @return the area containing the given water zone
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water waterZone) {
        for (Area<Zone.Water> riverSystemArea : partition.riverSystems().areas()) {
            if (riverSystemArea.zones().contains(waterZone)) return riverSystemArea;
        }
        throw new IllegalArgumentException();
    }

    /**
     * This method returns a set of all the meadow areas on the board.
     *
     * @return the set of all the meadow areas on the board
     */
    public Set<Area<Zone.Meadow>> meadowAreas() {
        return partition.meadows().areas();
    }

    /**
     * This method returns a set of all the water zones areas on the board.
     *
     * @return the set of all the water zone areas on the board
     */
    public Set<Area<Zone.Water>> riverSystemAreas() {
        return partition.riverSystems().areas();
    }

    /**
     * This method returns true if the two given positions are adjacent
     *
     * @return true if the two given positions are adjacent
     */
    private boolean isAdjacent(Pos pos1, Pos pos2) {
        return (Math.abs(pos1.x() - pos2.x()) == 1 && Math.abs(pos1.y() - pos2.y()) == 1) || Math.abs(pos1.x() -
                pos2.x()) + Math.abs(pos1.y() - pos2.y()) == 1;
    }

    /**
     * This method returns the adjacent meadow to the given zone under the form of an
     * area containing the given meadow zone but all the occupants of the complete
     * meadow without any open connexions.
     *
     * @param pos the positions the given zone.
     * @param meadowZone the given zone.
     * @return the adjacent Meadow.
     */
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
        Set<Zone.Meadow> meadowsInArea = new HashSet<>();
        Set<Zone.Meadow> adjacentMeadows = new HashSet<>();
        List<PlayerColor> occupants = new ArrayList<>();

        for (Area<Zone.Meadow> area : meadowAreas()) {
            if (area.zones().contains(meadowZone)) {
                occupants = area.occupants();
                meadowsInArea.addAll(area.zones());
            }
        }

        for (PlacedTile placedTile : placedTiles) {
            if (placedTile != null && isAdjacent(placedTile.pos(), pos)) {
                for (Zone zone : placedTile.tile().zones()) {
                    if (zone instanceof Zone.Meadow meadow && meadowsInArea.contains(zone)) {
                        adjacentMeadows.add(meadow);
                    }
                }
            }
        }

        adjacentMeadows.add(meadowZone);
        return new Area<>(adjacentMeadows, occupants, 0);
    }

    /**
     * This method returns the number of occupants of the given kind
     * and color on the board
     *
     * @param player the given color.
     * @param occupantKind the given kind.
     * @return the number of occupants of the given kind and color on the board.
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        int count = 0;
        for (PlacedTile tile : placedTiles) {
            if (tile != null && tile.occupant() != null && tile.occupant().kind() == occupantKind
                    && tile.placer() == player) {
                count++;
            }
        }
        return count;
    }

    /**
     * This method returns the set of insertion positions on the board. (The positions
     * where a tile can be placed)
     *
     *
     * @return the set of insertion positions on the board.
     */
    public Set<Pos> insertionPositions() {
        Set<Pos> positions = new HashSet<>();
        for (PlacedTile placedTile : placedTiles) {
            if (placedTile != null) {
                int x = placedTile.pos().x();
                int y = placedTile.pos().y();
                if ((tileAt(new Pos(x - 1, y)) == null) && Math.abs(x-1) <= REACH && Math.abs(y) <= REACH) {
                    positions.add(new Pos(x - 1, y));
                }
                if (tileAt(new Pos(x + 1, y)) == null && Math.abs(x+1) <= REACH && Math.abs(y) <= REACH) {
                    positions.add(new Pos(x + 1, y));
                }
                if (tileAt(new Pos(x, y - 1)) == null && Math.abs(x) <= REACH && Math.abs(y-1) <= REACH) {
                    positions.add(new Pos(x, y - 1));
                }
                if (tileAt(new Pos(x, y + 1)) == null && Math.abs(x) <= REACH && Math.abs(y+1) <= REACH) {
                    positions.add(new Pos(x, y + 1));
                }
            }
        }
        return positions;
    }

    /**
     * This method returns the last tile placed on the board.

     * @return the last tile placed on the board or null if the board is empty.
     */
    public PlacedTile lastPlacedTile() {
        return (index.length > 0) ? placedTiles[index[index.length - 1]] : null;
    }

    /**
     * This method returns the set of all the forest areas that were closed by the placement
     * of the last tile on the board or empty if the board is empty.
     *
     * @return the set of all the forest areas that were closed by the placement of the last tile on the board.
     */
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


    /**
     * This method returns the set of all the river areas that were closed by the placement
     * of the last tile on the board or empty if the board is empty.
     *
     * @return the set of all the forest river that were closed by the placement of the last tile on the board.
     */
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


    /**
     * This method returns if the given PlacedTile can be added to the board. (if its position is an insertion position
     * and if all the sides of the tile are compatible with the sides of the adjacent tiles)
     *
     * @return if the given PlacedTile can be added to the board.
     */
    public boolean canAddTile(PlacedTile placedTile) {
        if (insertionPositions().contains(placedTile.pos())) {
            PlacedTile tileEast = tileAt(placedTile.pos().neighbor(Direction.E));
            PlacedTile tileWest = tileAt(placedTile.pos().neighbor(Direction.W));
            PlacedTile tileNorth = tileAt(placedTile.pos().neighbor(Direction.N));
            PlacedTile tileSouth = tileAt(placedTile.pos().neighbor(Direction.S));

            return (tileEast == null || placedTile.side(Direction.E).isSameKindAs(tileEast.side(Direction.W)))
                    && (tileWest == null || placedTile.side(Direction.W).isSameKindAs(tileWest.side(Direction.E)))
                    && (tileNorth == null || placedTile.side(Direction.N).isSameKindAs(tileNorth.side(Direction.S)))
                    && (tileSouth == null || placedTile.side(Direction.S).isSameKindAs(tileSouth.side(Direction.N)));
        }
        return false;
    }

    /**
     * This method returns if the given tile can be placed on the board. (if its position is an insertion position
     * and if all the sides of the tile are compatible with the sides of the adjacent tiles for any of its rotations)
     *
     * @return if the given tile can be placed on the board.
     */
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

    /**
     * This method returns a new board identical to the current one but with the given tile placed on it or
     * throws a new IllegalArgumentException if the tile cannot be placed on the board and the board isn't empty.
     *
     * @param tile the tile to be placed on the board
     * @throws IllegalArgumentException if the tile cannot be placed on the board and the board isn't empty
     * @return a new board identical to the current one but with the given tile placed on it
     */
    public Board withNewTile(PlacedTile tile) {
        Preconditions.checkArgument(index.length == 0 || canAddTile(tile));
        PlacedTile[] newPlacedTiles = Arrays.copyOf(placedTiles, placedTiles.length);

        int[] newIndex = Arrays.copyOf(index, index.length + 1);
        int i = index(tile.pos());

        newPlacedTiles[i] = tile;
        newIndex[newIndex.length - 1] = i;
        ZonePartitions.Builder newPartitions = new ZonePartitions.Builder(partition);
        newPartitions.addTile(tile.tile());

        for (Direction dir : Direction.ALL) {
            PlacedTile neighbor = tileAt(tile.pos().neighbor(dir));
            if (neighbor != null && neighbor.side(dir.opposite()).isSameKindAs(tile.side(dir))) {
                newPartitions.connectSides(tile.side(dir), neighbor.side(dir.opposite()));
            }
        }

        return new Board(newPlacedTiles, newIndex, newPartitions.build(), cancelledAnimals);
    }

    /**
     * This method returns a new board identical to the current one but with the occupant added or
     * throws a new IllegalArgumentException if the tile that should contain the occupant is already
     * occupied.
     *
     * @param occupant the occupant to be placed on the board
     * @throws IllegalArgumentException if the tile that should contain the occupant is already occupied.
     * @return a new board identical to the current one but with the occupant added
     */
    public Board withOccupant(Occupant occupant) {
        int id = Zone.tileId(occupant.zoneId());
        Preconditions.checkArgument(Objects.isNull(tileWithId(id).occupant()));
        PlacedTile addTile = tileWithId(id).withOccupant(occupant);
        PlacedTile[] placedTiles1 = Arrays.copyOf(placedTiles, placedTiles.length);
        placedTiles1[index(addTile.pos())] = addTile;
        ZonePartitions.Builder newPartition = new ZonePartitions.Builder(partition);

        for (Zone zone : addTile.tile().zones()) {
            if (zone.id() == occupant.zoneId()) {
                newPartition.addInitialOccupant(addTile.placer(), occupant.kind(), zone);
            }
        }

        return new Board(placedTiles1, index, newPartition.build(), cancelledAnimals);
    }


    /**
     * This method returns a new board identical to the current one but with the given occupant removed
     * from it.
     *
     * @param occupant the occupant to be removed from the board
     * @return a new board identical to the current one but with the occupant removed from it
     */
    public Board withoutOccupant(Occupant occupant) {
        int id = Zone.tileId(occupant.zoneId());
        PlacedTile tile = tileWithId(id);
        Preconditions.checkArgument(tile != null);
        ZonePartitions.Builder newZonePartitionsBuilder = new ZonePartitions.Builder(partition);
        PlacedTile[] placedTiles1 = Arrays.copyOf(placedTiles, placedTiles.length);
        placedTiles1[index(tile.pos())] = tile.withNoOccupant();

        for (Zone zone : tile.tile().zones()) {
            if (occupant.zoneId() == zone.id()) {
                newZonePartitionsBuilder.removePawn(tile.placer(), zone);
            }
        }

        return new Board(placedTiles1, index, newZonePartitionsBuilder.build(), cancelledAnimals);
    }


    /**
     * This method returns a new board identical to the current one but without any occupants in the forest area
     * or river areas given as parameters.
     *
     * @param forests the forest areas to be cleared of their occupants
     * @param rivers the river areas to be cleared of their occupants
     * @return a new board identical to the current one but with the given animal added to it
     */
    public Board withoutGatherersOrFishersIn(Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers) {
        ZonePartitions.Builder newBoardZonePartitionsBuilder = new ZonePartitions.Builder(partition);
        PlacedTile[] newPlacedTile = placedTiles.clone();
        Set<PlacedTile> forestPawnsToCancel = new HashSet<>();
        Set<PlacedTile> riversPawnsToCancel = new HashSet<>();

        for (Area<Zone.Forest> forestArea : forests) {
            newBoardZonePartitionsBuilder.clearGatherers(forestArea);
            for (Zone.Forest zoneForest : forestArea.zones()) {
                PlacedTile tile = tileWithId(Zone.tileId(zoneForest.id()));
                if (tile.occupant() != null) {
                    if (tile.occupant().zoneId() == zoneForest.id() && tile.occupant().kind() == Occupant.Kind.PAWN) {
                        forestPawnsToCancel.add(tile);
                    }
                }
            }
            for (PlacedTile tile : forestPawnsToCancel) {
                newPlacedTile[index(tile.pos())] = tile.withNoOccupant();
            }

        }
        for (Area<Zone.River> riverArea : rivers) {
            newBoardZonePartitionsBuilder.clearFishers(riverArea);
            for (Zone.River zoneRiver : riverArea.zones()) {
                PlacedTile tile = tileWithId(Zone.tileId(zoneRiver.id()));
                if (tile.occupant() != null) {
                    if (tile.occupant().zoneId() == zoneRiver.id() && tile.occupant().kind() == Occupant.Kind.PAWN) {
                        riversPawnsToCancel.add(tile);
                    }
                }
            }
            for (PlacedTile tile : riversPawnsToCancel) {
                newPlacedTile[index(tile.pos())] = tile.withNoOccupant();
            }
        }
        ZonePartitions newBoardZonePartitions = newBoardZonePartitionsBuilder.build();
        return new Board(newPlacedTile, index, newBoardZonePartitions, cancelledAnimals);
    }

    /**
     * This method returns a new board identical to the current one but with the additional cancelled animals.
     *
     * @param newlyCancelledAnimals the animals to be added to the cancelled animals
     * @return a new board identical to the current one but with the additional cancelled animals.
     */
    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        Set<Animal> newCancelledAnimals = new HashSet<>(cancelledAnimals);
        newCancelledAnimals.addAll(newlyCancelledAnimals);
        return new Board(placedTiles, index, partition, newCancelledAnimals);
    }

    /**
     * This method redefines the equals method for the Board class.
     * It returns true if the given object is equal to the current board.
     *
     * @param that the object to be compared to the current board
     * @return true if all the attributes of the given object are equal to the current board
     */
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Board board)) return false;
        return Arrays.equals(board.index, index) && Arrays.equals(board.placedTiles, placedTiles)
                && board.partition.equals(partition) && board.cancelledAnimals.equals(cancelledAnimals);
    }

    /**
     * This method redefines the hashCode method for the Board class.
     * It returns the hash code of the current board.
     *
     * @return the hash code of the current board
     */
    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(index), Arrays.hashCode(placedTiles), partition, cancelledAnimals);
    }
}
