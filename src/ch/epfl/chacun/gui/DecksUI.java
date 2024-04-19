package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import java.util.function.Consumer;

public class DecksUI {
    //private constructor to prevent instantiation
    private DecksUI() {}

    public static Node create(ObservableValue<Tile> tile, ObservableValue<Integer> normalCount, ObservableValue<Integer> menhirCount, ObservableValue<String> text, Consumer<Occupant> occupantConsumer) {
        

    }
}
