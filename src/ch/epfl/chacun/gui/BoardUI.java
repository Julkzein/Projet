package ch.epfl.chacun.gui;

import ch.epfl.chacun.GameState;
import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Pos;
import ch.epfl.chacun.Rotation;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import java.util.Set;
import java.util.function.Consumer;

public class BoardUI {
    public static Node create(int reach, ObservableValue<GameState> gameState, ObservableValue<Rotation> rotation, ObservableValue<Set<Occupant>> occupants,
                              ObservableValue<Set<Integer>> evidentId, Consumer<Rotation> desiredRotation, Consumer<Pos> desiredPlacement, Consumer<Occupant> desiredRetake) {

        return null;
    }
}
