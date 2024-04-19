package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

import static ch.epfl.chacun.gui.ImageLoader.*;

public class DecksUI {
    //private constructor to prevent instantiation
    private DecksUI() {}

    public static Node create(ObservableValue<Tile> tile,
                              ObservableValue<Integer> normalCount,
                              ObservableValue<Integer> menhirCount,
                              ObservableValue<String> text,
                              Consumer<Occupant> occupantConsumer) {
        VBox vbox = new VBox();
        vbox.getStylesheets().add("decks.css");

        HBox hbox = new HBox();
        vbox.getChildren().add(hbox);

        StackPane nextTileStackPane = new StackPane();
        vbox.getChildren().add(nextTileStackPane);

        Text newText = new Text();
        newText.setWrappingWidth(0.8 * LARGE_TILE_FIT_SIZE);
        text.addListener((o, oV, nV) -> newText.setText(nV));
        nextTileStackPane.getChildren().add(newText);

        ImageView tileImageView = new ImageView();
        tileImageView.setImage(largeImageForTile(tile.getValue().id()));
        tileImageView.setFitWidth(LARGE_TILE_FIT_SIZE);
        tileImageView.setFitHeight(LARGE_TILE_FIT_SIZE);
        nextTileStackPane.getChildren().add(tileImageView);

        ObservableValue<Boolean> textVisible = text.map(t -> !t.equals(""));
        newText.visibleProperty().bind(textVisible);

        StackPane normalStackPane = new StackPane();
        hbox.getChildren().add(normalStackPane);

        StackPane menhirStackPane = new StackPane();
        hbox.getChildren().add(menhirStackPane);

        Text normalCountValue = new Text();
        normalCountValue.textProperty().bind(normalCount.map(String::valueOf));
        normalStackPane.getChildren().add(normalCountValue);

        tile.addListener((o, oV, nV) -> {
            tileImageView.setImage(largeImageForTile(nV.id()));
            newText.setText("");
        });

        return vbox;
    }
}
