package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;


/**
 * This class represents the UI elements that display the players' names, points and remaining pawns and huts.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class PlayersUI {
    //private constructor to prevent instantiation
    private PlayersUI() {}

    /**
     * This method creates the UI elements that display the players' names, points and remaining pawns and huts.
     *
     * @param gameState the current state of the game
     * @param textMaker the text maker used to generate the text in French
     * @return the ui elements of the players
     */
    public static Node create(ObservableValue<GameState> gameState, TextMakerFr textMaker) {
        VBox vbox = new VBox();
        vbox.getStylesheets().add("players.css");
        vbox.setId("players");
        for (PlayerColor player : gameState.getValue().players()) {
            vbox.getChildren().add(createPlayerTextFlow(gameState, player, textMaker));
        }
        /**
        for (PlayerColor player : PlayerColor.ALL) {
            if (textMaker.playerName(player) != null) {
                vbox.getChildren().add(createPlayerTextFlow(gameState, player, textMaker));
            }
        }
         */
        return vbox;
    }

    /**
     * This method creates the text flow that displays the name, points and remaining pawns and huts of a player.
     *
     * @param gameState the current state of the game
     * @param player the color of the player
     * @param textMaker the text maker used to generate the text in French
     * @return the ui element of the given player
     */
    private static TextFlow createPlayerTextFlow(ObservableValue<GameState> gameState, PlayerColor player, TextMakerFr textMaker) {
        //points
        ObservableValue<Map<PlayerColor, Integer>> pointsO = gameState.map(GameState::messageBoard).map(MessageBoard::points);
        ObservableValue<String> pointTextO = pointsO.map(points -> STR." \{textMaker.playerName(player)} : \{textMaker.points(points.getOrDefault(player, 0))} \n");
        Text playerNameAndPoints = new Text();
        playerNameAndPoints.textProperty().bind(pointTextO);

        //player circle
        Circle playerCircle = new Circle(5, ColorMap.fillColor(player));

        //textFlow
        TextFlow textFlow =  new TextFlow(playerCircle, playerNameAndPoints);

        //icons and space
        Node[] icons = new Node[5];
        for (int i = 0; i < 3; i++) {
            icons[i] = Icon.newFor(player, Occupant.Kind.HUT);
            final int finalI1 = i;
            icons[i].opacityProperty().bind(gameState.map(gameState1 -> gameState1.freeOccupantsCount(player, Occupant.Kind.HUT) > finalI1 ? 1 : 0.1));
            textFlow.getChildren().add(icons[i]);
        }
        textFlow.getChildren().add(new Text("   "));
        for (int i = 0; i < 5; i++) {
            icons[i] = Icon.newFor(player, Occupant.Kind.PAWN);
            final int finalI2 = i;
            icons[i].opacityProperty().bind(gameState.map(gameState1 -> gameState1.freeOccupantsCount(player, Occupant.Kind.PAWN) > finalI2 ? 1 : 0.1));
            textFlow.getChildren().add(icons[i]);
        }

        //current player
        ObservableValue<PlayerColor> currentPlayer = gameState.map(GameState::currentPlayer);
        currentPlayer.addListener((_, _, nV) -> {
            if (nV == player) {
                textFlow.getStyleClass().add("current");
            } else {
                textFlow.getStyleClass().remove("current");
            }
        });
        textFlow.getStyleClass().add("player");
        return textFlow;
    }
}
