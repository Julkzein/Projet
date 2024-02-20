package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.List;

public enum Rotation {
    NONE,
    RIGHT,
    HALF_TURN,
    LEFT;

    public static final List<Rotation> ALL = List.of(Rotation.values());

    public static final int COUNT = ALL.size();

    public Rotation add(Rotation that) {
        return Rotation.values()[(this.quarterTurnsCW() + that.quarterTurnsCW()) % COUNT];
    }

    public Rotation negated() {
        return Rotation.values()[(COUNT - this.quarterTurnsCW()) % COUNT];
    }

    public int quarterTurnsCW() {
        return this.ordinal();
    }

    public int degreesCW() {
        return this.quarterTurnsCW() * 90;
    }
}
