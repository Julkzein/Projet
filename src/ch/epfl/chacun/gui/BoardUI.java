package ch.epfl.chacun.gui;

import ch.epfl.chacun.GameState;
import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Pos;
import ch.epfl.chacun.Rotation;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Set;
import java.util.function.Consumer;

public class BoardUI {
    public static Node create(int reach, ObservableValue<GameState> gameState, ObservableValue<Rotation> rotation, ObservableValue<Set<Occupant>> occupants,
                              ObservableValue<Set<Integer>> evidentId, Consumer<Rotation> desiredRotation, Consumer<Pos> desiredPlacement, Consumer<Occupant> desiredRetake) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("board-scroll-pane");
        scrollPane.getStylesheets().add("board.css");
        GridPane gridPane = new GridPane();
        gridPane.setId("board-grid");
        for (int i = 0; i < 2 * reach + 1; i++) {
            for (int j = 0; j < 2 * reach + 1; j++) {
                Pos pos = new Pos(i - reach, j - reach);
                gridPane.add(createGroup(reach, gameState, rotation, occupants, evidentId, desiredRotation, desiredPlacement, desiredRetake, pos), i, j);
            }
        }
        //TODO call methode creation group par case
        return null;
    }

    private static Node createGroup(int reach, ObservableValue<GameState> gameState, ObservableValue<Rotation> rotation, ObservableValue<Set<Occupant>> occupants,
                                    ObservableValue<Set<Integer>> evidentId, Consumer<Rotation> desiredRotation, Consumer<Pos> desiredPlacement, Consumer<Occupant> desiredRetake, Pos pos) {
        Group group = new Group();
        //group.getChildren().add(ImageView(""));
        return null;
    }
}
