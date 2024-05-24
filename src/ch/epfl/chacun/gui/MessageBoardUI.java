package ch.epfl.chacun.gui;

import ch.epfl.chacun.MessageBoard;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Set;

import static ch.epfl.chacun.gui.ImageLoader.LARGE_TILE_FIT_SIZE;

/**
 * This class represents the UI elements that display the message board.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class MessageBoardUI {
    //private constructor to prevent instantiation
    private MessageBoardUI() {}

    /**
     * This method creates the UI elements that display the message board.
     *
     * @param messages the observable value of the list of messages
     * @param tileIds the object property of the tile ids
     * @return the ui elements of the message board
     */
    public static Node create(ObservableValue<List<MessageBoard.Message>> messages, ObjectProperty<Set<Integer>> tileIds) {
        VBox vbox = new VBox();

        ScrollPane scrollPane = new ScrollPane(vbox);

        scrollPane.getStylesheets().add("message-board.css");
        scrollPane.setId("message-board");

        //When the list of messages changes, add the new messages to the VBox
        messages.addListener((_, oV, nV) -> {
            nV.subList(oV.size(), nV.size()).forEach(message -> {
                Text newText = new Text(message.text());
                newText.setWrappingWidth(LARGE_TILE_FIT_SIZE);
                newText.setOnMouseEntered(e -> {
                    tileIds.set(message.tileIds());
                });
                newText.setOnMouseExited(e -> {
                    tileIds.set(Set.of());
                });
                vbox.getChildren().add(newText);
            });
            scrollPane.layout();
            //Here we use runLater because on Windows, the scrollPane does not update correctly without runLater
            Platform.runLater(() -> scrollPane.setVvalue(1.0));
        });
        return scrollPane;
    }
}
