package ch.epfl.chacun;

import java.util.List;

/**
 * Represents the four possible rotations.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public enum Rotation {
    NONE,
    RIGHT,
    HALF_TURN,
    LEFT;

    /**
     * The four possible rotations in the order they are declared.
     */
    public static final List<Rotation> ALL = List.of(Rotation.values());


    /**
     * The number of possible rotations.
     */
    public static final int COUNT = ALL.size();


    /**
     * This method adds a certain rotation to this rotation.
     *
     * @param that the rotation to apply.
     * @return the rotation obtained by adding the given rotation to this rotation.
     */
    public Rotation add(Rotation that) {
        return Rotation.values()[(this.quarterTurnsCW() + that.quarterTurnsCW()) % COUNT];
    }


    /**
     * This method returns the opposite rotation of this rotation.
     *
     * @return the rotation opposite to this rotation.
     */
    public Rotation negated() {
        return Rotation.values()[(COUNT - quarterTurnsCW()) % COUNT];
    }


    /**
     * This method returns the number of quarter turns clockwise in this rotation.
     * *
     * @return the number of quarter turns clockwise in this rotation.
     */
    public int quarterTurnsCW() {
        return ordinal();
    }


    /**
     * This method returns the number of degrees clockwise in this rotation.
     *
     * @return the number of degrees clockwise in this rotation.
     */
    public int degreesCW() {
        return quarterTurnsCW() * 90;
    }
}
