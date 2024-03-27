package ch.epfl.chacun;

import java.util.List;

/**
 * Represents the five possible colors of a player.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public enum PlayerColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    PURPLE;

    /**
     * List of the five possible colors of a player in the order they are declared.
     */
    public static final List<PlayerColor> ALL = List.of(PlayerColor.values());

}
