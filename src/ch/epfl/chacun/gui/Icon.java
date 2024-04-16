package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import javafx.scene.Node;
import javafx.scene.shape.SVGPath;

public class Icon {
    private Icon() {}
    public static Node newFor(PlayerColor color, Occupant.Kind occupant) {
        SVGPath icon = new SVGPath();
        if (occupant == Occupant.Kind.PAWN) {
            icon.setContent("M -10 10 H -4 L 0 2 L 6 10 H 12 L 5 0 L 12 -2 L 12 -4 L 6 -6 L 6 -10 L 0 -10 L -2 -4 L -6 -2 L -8 -10 L -12 -10 L -8 6 Z");
        } else {
            icon.setContent("M -8 10 H 8 V 2 H 12 L 0 -10 L -12 2 H -8 Z");
        }
        icon.setFill(ColorMap.fillColor(color));
        icon.setStroke(ColorMap.strokeColor(color));
        return icon;
    }
}
