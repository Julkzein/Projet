package ch.epfl.chacun;

import java.util.List;

public enum Direction {
    N,
    E,
    S,
    W;

    public static final List<Direction> ALL = List.of(Direction.values());
    public static final int COUNT = ALL.size();

    public Direction rotated(Rotation rotation) {
        return Direction.values()[(this.ordinal() + rotation.quarterTurnsCW()) % COUNT];
    }

    public Direction opposite() {
        return Direction.values()[(this.ordinal() + 2) % COUNT];
    }
}
