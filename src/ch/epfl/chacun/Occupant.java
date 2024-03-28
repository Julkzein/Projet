package ch.epfl.chacun;

import static java.util.Objects.requireNonNull;


/**
 * Represents an occupant of a tile in the game.
 *
 * @param kind the Kind of the occupant.
 * @param zoneId the id of the zone to which the occupant belongs.
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public record Occupant(Kind kind, int zoneId) {
    /**
     * Represents the two possible kinds of occupants of a tile.
     */
    public enum Kind {
        PAWN,
        HUT
    }

    /**
     * Constructs an occupant of the given kind and in the given zone.
     *
     * @param kind the kind of the occupant.
     * @param zoneId the zone id of the occupant.
     * @throws NullPointerException if the kind is null.
     * @throws IllegalArgumentException if the zone id is negative.
     */
    public Occupant {
        requireNonNull(kind);
        Preconditions.checkArgument(zoneId >= 0);
    }


    /**
     * Returns the number of occupants of the given kind.
     * @param kind : kind of the occupant.
     * @return the number of occupants of the given kind.
     */
    public static int occupantsCount(Kind kind) {
        return switch (kind) {
            case PAWN -> 5;
            case HUT -> 3;
        };
    }
}
