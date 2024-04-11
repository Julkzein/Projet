package ch.epfl.chacun;

import java.util.List;

/**
 * Represents the four cardinal directions.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public enum Direction {
    N,
    E,
    S,
    W;

    /**
     * List of the four cardinal directions in the order they are declared.
     */
    public static final List<Direction> ALL = List.of(Direction.values());

    /**
     * The number of cardinal directions.
     */
    public static final int COUNT = ALL.size();

    /**
     * Returns the direction obtained by rotating this direction by 90 degrees clockwise the given number of times.
     *
     * @param rotation the rotation to apply.
     * @return the new direction obtained.
     */
    public Direction rotated(Rotation rotation) {
        return Direction.values()[(this.ordinal() + rotation.quarterTurnsCW()) % COUNT];
    }

    /**
     * Returns the direction opposite to this direction.
     *
     * @return the new direction.
     */
    public Direction opposite() {
        return Direction.values()[(this.ordinal() + 2) % COUNT];
    }
}
