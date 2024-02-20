package ch.epfl.chacun;

import java.util.List;

/**
 * Represents the five possible colors of a player.
 */
public enum PlayerColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    PURPLE;

    /**
     * The five possible colors of a player in the order they are declared.
     */
    final public static List<PlayerColor> ALL = List.of(PlayerColor.values());

}
