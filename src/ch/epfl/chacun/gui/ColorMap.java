package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import javafx.scene.paint.Color;

public class ColorMap {
    private ColorMap() {}
    public static javafx.scene.paint.Color fillColor(PlayerColor color) {
        return switch (color) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.LIME;
            case YELLOW -> Color.YELLOW;
            case PURPLE -> Color.PURPLE;
        };
    }

    public static javafx.scene.paint.Color strokeColor(PlayerColor color) {
        return switch (color) {
            case GREEN -> Color.GREEN.deriveColor(0, 1, 0.6, 1);
            case YELLOW -> Color.YELLOW.deriveColor(0, 1, 0.6, 1);
            default -> fillColor(color);
        };
    }
}
