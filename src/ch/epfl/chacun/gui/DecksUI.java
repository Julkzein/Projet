package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class DecksUI {
    //private constructor to prevent instantiation
    private DecksUI() {}

    public static Node create(ObservableValue<Tile> tile, ObservableValue<Integer> normalCount, ObservableValue<Integer> menhirCount, ObservableValue<String> text, Consumer<Occupant> occupantConsumer) {
        VBox vbox = new VBox();
        vbox.getStylesheets().add("decks.css");

        HBox hbox = new HBox();
        vbox.getChildren().add(hbox);

        StackPane normalStackPane = new StackPane();
        hbox.getChildren().add(normalStackPane);

        StackPane menhirStackPane = new StackPane();
        hbox.getChildren().add(menhirStackPane);

        StackPane nextTileStackPane = new StackPane();
        vbox.getChildren().add(nextTileStackPane);

        
    }
}
