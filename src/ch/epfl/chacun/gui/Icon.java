package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import javafx.scene.Node;
import javafx.scene.shape.SVGPath;

/**
 * This class represents the icons used to represent the pawns and huts of the players.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class Icon {
    //private constructor to prevent instantiation
    private Icon() {}

    /**
     * This method returns a new node representing a given player's pawn or hut.
     *
     * @param color the color of the player
     * @param occupant the kind of occupant to represent
     * @return a new node representing a given player's pawn or hut
     */
    public static Node newFor(PlayerColor color, Occupant.Kind occupant) {
        SVGPath icon = new SVGPath();
        if (occupant == Occupant.Kind.PAWN) {
            icon.setContent("M -10 10 H -4 L 0 2 L 6 10 H 12 L 5 0 L 12 -2 L 12 -4 L 6 -6 L 6 -10 L 0 -10 L -2 -4" +
                    " L -6 -2 L -8 -10 L -12 -10 L -8 6 Z");
        } else {
            icon.setContent("M -8 10 H 8 V 2 H 12 L 0 -10 L -12 2 H -8 Z");
        }
        icon.setFill(ColorMap.fillColor(color));
        icon.setStroke(ColorMap.strokeColor(color));
        return icon;
    }
}
