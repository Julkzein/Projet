package ch.epfl.chacun;

import java.util.HashSet;
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
     * @throws IllegalArgumentException if the tile or rotation is null.
     */
    public PlacedTile {
        if (tile == null || rotation == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Compact constructor with a given tile, placer, rotation and position.
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


    public int id() {
        return tile.id();
    }

    public Tile.Kind kind() {
        return tile.kind();
    }
    

    public TileSide side(Direction direction) {
        Direction newDirection = direction.rotated(rotation.negated());
        switch (newDirection) {
            case N:
                return tile.n();
            case E:
                return tile.e();
            case S:
                return tile.s();
            case W:
                return tile.w();
            default:
                return null;
        }

    }

    public Zone zoneWithId(int id) {
        for (Zone zone : tile.zones()) {
            if (zone.localId() == id) {
                return zone;
            }
        }
        return null;
    }

    public Zone specialPowerZone() {
        for (Zone zone : tile.zones()) {
            if (zone.specialPower() != null) {
                return zone;
            }
        }
        return null;
    }

    public Set<Zone.Forest> forestZones() {
        Set<Zone.Forest> set = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Forest forest) {
                set.add(forest);
            }
        }
        return set;
    }

    public Set<Zone.Meadow> meadowZones() {
        Set<Zone.Meadow> set = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Meadow meadow) {
                set.add(meadow);
            }
        }
        return set;
    }

    public Set<Zone.River> riversZones() {
        Set<Zone.River> set = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.River river) {
                set.add(river);
            }
        }
        return set;
    }

    public Set<Occupant> potentialOccupants() {
        Set<Occupant> potentialOccupantsSet = new HashSet<>();
        if(placer.equals(null)) {
            return null;
        }
        else {
            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.Meadow meadow) {
                    potentialOccupantsSet.add(new Occupant(Occupant.Kind.PAWN, id()));
                }
                else if(zone instanceof Zone.River river) {
                    potentialOccupantsSet.add(new Occupant(Occupant.Kind.PAWN, id()));
                    if (!river.hasLake()) {
                        potentialOccupantsSet.add(new Occupant(Occupant.Kind.HUT, id()));
                    }
                }
                else if (zone instanceof Zone.Forest forest) {
                    potentialOccupantsSet.add(new Occupant(Occupant.Kind.PAWN, id()));
                }
                else if (zone instanceof Zone.Lake lake) {
                    potentialOccupantsSet.add(new Occupant(Occupant.Kind.HUT, id()));
                }
            }
        }
        return potentialOccupantsSet;
    }

    public PlacedTile withOccupant(Occupant occupant) {
        if (this.occupant != null) {
            throw new IllegalArgumentException();
        }
        return new PlacedTile(this.tile, this.placer, this.rotation, this.pos, occupant);
    }

    public PlacedTile withNoOccupant() {
        return new PlacedTile(this.tile, this.placer, this.rotation, this.pos);
    }

    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        if (occupantKind != null ) {
            if (occupantKind == this.occupant.kind()) {
                return this.occupant.zoneId();
            }
        }
        return -1;
    }

}
