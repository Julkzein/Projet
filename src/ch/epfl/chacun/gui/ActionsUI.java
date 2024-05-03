package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Base64;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class ActionsUI {
    //private constructor to prevent instantiation
    private ActionsUI() {}

    public Node create(ObservableValue<List<String>> actionList,
                       Consumer<String> action) {

        HBox actions = new HBox();
        actions.getStylesheets().add("actions.css");
        actions.setId("actions");

        Text fourLastActions = new Text(fourLastActionsToString(actionList.getValue()));
        actions.getChildren().add(fourLastActions);

        // Create a StringBinding and binding it to the Text
        StringBinding textBinding = Bindings.createStringBinding(
            () -> fourLastActionsToString(actionList.getValue()), actionList
        );
        fourLastActions.textProperty().bind(textBinding);

        TextField textField = new TextField();
        textField.setId("action-field");
        actions.getChildren().add(textField);

        // Checks if the input character is valid
        textField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(
                (Base32.isValid(change.getText()) ? change.getText() : "")
            );
            return change;
        }));

        return actions;
    }
    private String fourLastActionsToString(List<String> actionList) {
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = actionList.size() - 4; i < actionList.size(); i++) {
            joiner.add(i + ":" + actionList.get(i));
        }
        return joiner.toString();
    }

}
