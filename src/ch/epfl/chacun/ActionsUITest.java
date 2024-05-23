package ch.epfl.chacun.gui;

import ch.epfl.chacun.gui.ActionsUI;
import javafx.application.Application;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ActionsUITest extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        var actions = List.of("A5", "D", "AU", "C");

        var actionsO = new SimpleObjectProperty<>(actions);

        Consumer<String> actionHandler = action -> {
            List<String> newActions = new ArrayList<>(actionsO.getValue());
            newActions.add(action);
            actionsO.set(List.copyOf(newActions));

        };

        var actionsNode = ActionsUI.create(actionsO, actionHandler);

        var rootNode = new BorderPane(actionsNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}
