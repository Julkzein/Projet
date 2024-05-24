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

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * This class represents the UI elements that display the encoded actions of the players.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class ActionsUI {
    //private constructor to prevent instantiation
    private ActionsUI() {}


    /**
     * This method creates the UI elements that display the encoded actions of the players.
     *
     * @param actionList the list of the last four actions
     * @param action the consumer that will be called when the user presses enter
     * @return the ui elements of the actions
     */
    public static Node create(ObservableValue<List<String>> actionList,
                       Consumer<String> action) {

        HBox actions = new HBox();
        actions.getStylesheets().add("actions.css");
        actions.setId("actions");

        Text fourLastActions = new Text(fourLastActionsToString(actionList.getValue()));
        actions.getChildren().add(fourLastActions);


        actionList.addListener((_, _, nV) -> fourLastActions.setText(fourLastActionsToString(nV)));

        TextField textField = new TextField();
        textField.setId("action-field");
        actions.getChildren().add(textField);

        // Checks if the input character is valid
        final int ENCODED_ACTION_SIZE = 2;
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String existingText = change.getControlText();
            int spaceRemaining = ENCODED_ACTION_SIZE - existingText.length();
            String newText = change.getText().chars()
                    .map(Character::toUpperCase)
                    .mapToObj(c -> String.valueOf((char) c))
                    .filter(Base32::isValid)
                    .limit(spaceRemaining)
                    .collect(Collectors.joining());
            change.setText(newText);
            return change;
        }));

        // When the user presses enter, the consumer is called with the text field's content
        textField.setOnAction(_ -> {  //TODO : futur check if all good
            action.accept(textField.getText());
            textField.clear();
        });

        return actions;
    }


    /**
     * This method converts the last four actions to a string.
     *
     * @param actionList the list of the last four actions
     * @return the string of the last four actions
     */
    private static String fourLastActionsToString(List<String> actionList) {
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = Math.max(actionList.size() - 4, 0); i < actionList.size(); i++) {
            joiner.add(STR."\{i + 1}:\{actionList.get(i)}");
        }
        return joiner.toString();
    }

}
