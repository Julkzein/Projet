package ch.epfl.chacun;

import static java.util.Objects.requireNonNull;

public record Occupant(Kind kind, int zoneId) {
    public enum Kind {
        PAWN,
        HUT;
    }

    public Occupant {
        requireNonNull(kind);
        if (zoneId < 0) {
            throw new IllegalArgumentException();
        }
    }

    // vérifier si ça marche (2ème), sinon:

    /**
     * switch (kind) {
     *             case PAWN:
     *                 return 5;
     *             case HUT:
     *                 return 3;
     *         }
     *
     * @param kind
     * @return
     */

    public static int occupantsCount(Kind kind) {
        return switch (kind) {
            case PAWN -> 5;
            case HUT -> 3;
        };
    }
}
