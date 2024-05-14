package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

import static ch.epfl.chacun.gui.ImageLoader.*;

/**
 * This class represents the UI elements that display the decks of tiles.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class DecksUI {
    //private constructor to prevent instantiation
    private DecksUI() {}

    /**
     * This method creates the UI elements that display the decks of tiles.
     *
     * @param tile the observable value of the current tile
     * @param normalCount the observable value of the normal tile count
     * @param menhirCount the observable value of the menhir tile count
     * @param text the observable value of the text
     * @param occupantConsumer the consumer accepted when the text is displayed and clicked
     * @return the ui elements of the decks
     */
    public static Node create(ObservableValue<Tile> tile,
                              ObservableValue<Integer> normalCount,
                              ObservableValue<Integer> menhirCount,
                              ObservableValue<String> text,
                              Consumer<Occupant> occupantConsumer) {

        VBox vbox = new VBox();
        vbox.getStylesheets().add("decks.css");

        HBox hbox = new HBox();
        vbox.getChildren().add(hbox);
        hbox.setId("decks");

        //The next tile to be drawn ---------------------------------------------------------------
        StackPane nextTileStackPane = new StackPane();
        vbox.getChildren().add(nextTileStackPane);
        nextTileStackPane.setId("next-tile");

        ImageView tileImageView = new ImageView();
        //tileImageView.setImage(largeImageForTile(tile.getValue().id())); //TODO : is this necessary?
        tileImageView.setFitWidth(LARGE_TILE_FIT_SIZE);
        tileImageView.setFitHeight(LARGE_TILE_FIT_SIZE);
        nextTileStackPane.getChildren().add(tileImageView);

        Text newText = new Text(text.getValue());
        newText.setWrappingWidth(0.8 * LARGE_TILE_FIT_SIZE);
        text.addListener((_, _, nV) -> newText.setText(nV));
        nextTileStackPane.getChildren().add(newText);
        //-----------------------------------------------------------------------------------------

        ObservableValue<Boolean> textVisible = text.map(t -> !t.isEmpty());
        newText.visibleProperty().bind(textVisible); // visible when text is not empty
        tileImageView.visibleProperty().bind(text.map(String::isEmpty)); // visible when text is empty

        newText.setOnMouseClicked(_ -> {
            if (textVisible.getValue()) occupantConsumer.accept(null); //TODO : is the if really necessary?
        });

        tile.addListener((_, _, nV) -> {
            tileImageView.setImage(largeImageForTile(nV.id()));
            newText.setText("");
        });

        //The decks of normal and menhir tiles ----------------------------------------------------
        StackPane normalStackPane = new StackPane();
        StackPane menhirStackPane = new StackPane();
        Text normalCountValue = new Text();
        Text menhirCountValue = new Text();
        ImageView normalTileImageView = new ImageView();
        ImageView menhirTileImageView = new ImageView();
        normalTileImageView.setId("NORMAL");
        menhirTileImageView.setId("MENHIR");
        Image normalTileImage = new Image(STR."/256/NORMAL.jpg");
        Image menhirTileImage = new Image(STR."/256/MENHIR.jpg");

        createDeck(normalStackPane, hbox, normalCountValue, normalCount, normalTileImageView, normalTileImage);
        createDeck(menhirStackPane, hbox, menhirCountValue, menhirCount, menhirTileImageView, menhirTileImage);
        //-----------------------------------------------------------------------------------------

        return vbox;
    }

    /**
     * Creates the deck of normal or menhir tiles.
     *
     * @param stack the stack pane that contains the deck
     * @param hBox the horizontal box that contains the stack pane
     * @param tileCountText the text that displays the tile count
     * @param tileCount the observable value of the tile count
     * @param imageView the image view that displays the image
     * @param image the image of the tile deck
     */
    private static void createDeck(StackPane stack, HBox hBox, Text tileCountText, ObservableValue<Integer> tileCount, ImageView imageView, Image image) {
        hBox.getChildren().add(stack);
        tileCountText.textProperty().bind(tileCount.map(String::valueOf));
        imageView.setImage(image);
        imageView.setFitWidth(NORMAL_TILE_FIT_SIZE);
        imageView.setFitHeight(NORMAL_TILE_FIT_SIZE);
        stack.getChildren().add(imageView);
        stack.getChildren().add(tileCountText);
    }
}
