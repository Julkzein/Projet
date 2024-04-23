package ch.epfl.chacun.gui;

import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Set;

import static ch.epfl.chacun.gui.ImageLoader.*;
import static javafx.application.Platform.runLater;

public class MessageBoardUI {
    //private constructor to prevent instantiation
    private MessageBoardUI() {}

    public static Node create(ObservableValue<List<MessageBoard.Message>> messages, ObjectProperty<Set<Integer>> tileIds) {
        VBox vbox = new VBox();

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setId("message-board");
        scrollPane.getStylesheets().add("message-board.css");

        vbox.getChildren().add(new Text());

        messages.addListener((o, oV, nV) -> {
            nV.subList(oV.size(), nV.size()).forEach(message -> {
                Text newText = new Text(message.toString());
                newText.setWrappingWidth(LARGE_TILE_FIT_SIZE);
                newText.setOnMouseEntered(e -> {
                    tileIds.set(message.tileIds());
                });
                newText.setOnMouseExited(e -> {
                    tileIds.set(Set.of());
                });

                vbox.getChildren().add(newText);
            });
        });

        runLater(() -> scrollPane.setVvalue(1));

        return scrollPane;
    }
}
