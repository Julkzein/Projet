package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import javafx.scene.paint.Color;

/**
 * This class allows to get the color to use for the representation of each player.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class ColorMap {

    //private constructor to prevent instantiation
    private ColorMap() {}

    /**
     * This method returns the color to use to fill in the representation of the given player.
     *
     * @param color the color of the player
     * @return the color to use to fill in the representation of the given player
     */
    public static javafx.scene.paint.Color fillColor(PlayerColor color) {
        return switch (color) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.LIME;
            case YELLOW -> Color.YELLOW;
            case PURPLE -> Color.PURPLE;
        };
    }

    /**
     * This method returns the color to use for the outline of the representation of the given player.
     *
     * @param color the color of the player
     * @return the color to use to draw the representation of the given player
     */
    public static javafx.scene.paint.Color strokeColor(PlayerColor color) {
        return switch (color) {
            case GREEN, YELLOW -> fillColor(color).deriveColor(0, 1, 0.6, 1);
            default -> Color.WHITE;
        };
    }
}
