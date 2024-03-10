package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a tile placed on the board.
 *
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant) {

    /**
     * Compact constructor with a given tile, placer, rotation, position and occupant.
     *
     * @param tile the tile to place.
     * @param placer the player who placed the tile (null if first tile).
     * @param rotation the rotation of the tile.
     * @param pos the position of the tile.
     * @param occupant the occupant of the tile.
     * @throws IllegalArgumentException if the tile, rotation or position is null.
     */
    public PlacedTile {
        Objects.requireNonNull(tile);
        Objects.requireNonNull(rotation);
        Objects.requireNonNull(pos);
    }


    /**
     * Constructor with a given tile, placer, rotation and position.
     *
     * @param tile the tile to place.
     * @param placer the player who placed the tile (null if first tile).
     * @param rotation the rotation of the tile.
     * @param pos the position of the tile.
     * @throws IllegalArgumentException if the tile or rotation is null.
     */
    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }


    /**
     * This method return the current tile id
     *
     * @return current tile id
     */
    public int id() {
        return tile.id();
    }

    /**
     * This method return the current tile kind
     *
     * @return current tile kind
     */
    public Tile.Kind kind() {
        return tile.kind();
    }


    /**
     * Returns the side corresponding to the direction after rotation
     *
     * @param direction the direction we want to know the tile's side of
     * @return the side of the tile in the given direction
     */
    public TileSide side(Direction direction) {
        return switch (direction.rotated(rotation.negated())) {
            case N -> tile.n();
            case E -> tile.e();
            case S -> tile.s();
            case W -> tile.w();
        };
    }


    /**
     * Returns the zone associated to the given id, or null if no zone is associated to such id
     *
     * @param id the id of the zone we want
     * @return the type of zone associated to the given id
     */
    public Zone zoneWithId(int id) {
        for (Zone zone : tile.zones()) {
            if (zone.id() == id) {
                return zone;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the zone with a special power, or null if no zone of the tile has a special power
     *
     * @return the zone of the given tile with a special power
     */
    public Zone specialPowerZone() {
        for (Zone zone : tile.zones()) {
            if (zone.specialPower() != null) {
                return zone;
            }
        }
        return null;
    }

    /**
     * Returns a set containing all the forest zones of the tile, or null if no zone of the tile is of type forest
     *
     * @return a set containing all the forest zones of the tile
     */
    public Set<Zone.Forest> forestZones() {
        Set<Zone.Forest> set = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Forest forest) {
                set.add(forest);
            }
        }
        return set;
    }

    /**
     * Returns a set containing all the meadow zones of the tile, or null if no zone of the tile is of type meadow
     *
     * @return a set containing all the meadow zones of the tile
     */
    public Set<Zone.Meadow> meadowZones() {
        Set<Zone.Meadow> set = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Meadow meadow) {
                set.add(meadow);
            }
        }
        return set;
    }

    /**
     * Returns a set containing all the river zones of the tile, or null if no zone of the tile is of type river
     *
     * @return a set containing all the river zones of the tile
     */
    public Set<Zone.River> riverZones() {
        Set<Zone.River> set = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.River river) {
                set.add(river);
            }
        }
        return set;
    }

    /**
     * Returns a set containing all the possible occupants of a tile, or null if said tile is the starting tile
     *
     * @return a set containing all the possible occupants of a tile
     */
    public Set<Occupant> potentialOccupants() {
        Set<Occupant> potentialOccupantsSet = new HashSet<>();
        if(placer == null) {
            return potentialOccupantsSet;
        }
        else {
            for (Zone zone : tile.zones()) {
                if (tile.sideZones().contains(zone)) {
                    potentialOccupantsSet.add(new Occupant(Occupant.Kind.PAWN, zone.id()));
                }
                if (zone instanceof Zone.River river && !river.hasLake()) {
                    potentialOccupantsSet.add(new Occupant(Occupant.Kind.HUT, zone.id()));
                } else if (zone instanceof Zone.Lake) {
                    potentialOccupantsSet.add(new Occupant(Occupant.Kind.HUT, zone.id()));

                }
            }
        }
        return potentialOccupantsSet;
    }


    /**
     * This method returns the same tile but with the given occupant
     *
     * @param occupant we desire to add to the tile
     * @return a tile identical to the given tile but with the given occupant
     * @throws IllegalArgumentException if the tile already has an occupant
     */
    public PlacedTile withOccupant(Occupant occupant) {
        Preconditions.checkArgument(this.occupant == null);
        return new PlacedTile(this.tile, this.placer, this.rotation, this.pos, occupant);
    }


    /**
     * This method return the same tile
     *
     * @return a tile identical to the given tile but with no occupant
     */
    public PlacedTile withNoOccupant() {
        return new PlacedTile(this.tile, this.placer, this.rotation, this.pos);
    }

    /**
     * This method returns the id of the zone occupied by an occupant of the given
     * type in the given tile or -1 if no zone is occupied by an occupant of the given type
     *
     * @param occupantKind the kind of occupant we want to know the zone id of
     * @return the id of the zone occupied by the given type of occupant in the given tile,
     * -1 if no zone is occupied by an occupant of given kind
     */
    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        if (this.occupant != null) {
            if (occupantKind == this.occupant.kind()) {
                return this.occupant.zoneId();
            }
        }
        return -1;
    }
}
