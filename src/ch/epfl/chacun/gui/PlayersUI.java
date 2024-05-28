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
    //Private constructor to prevent instantiation
    private PlayersUI() {}

    /**
     * This method creates the UI elements that display the players' names, points and remaining pawns and huts.
     *
     * @param gameState the observable value of the current state of the game
     * @param textMaker the text maker used to generate the text in French
     * @return the node that displays the players
     */
    public static Node create(ObservableValue<GameState> gameState, TextMakerFr textMaker) {
        VBox vbox = new VBox();
        vbox.getStylesheets().add("players.css");
        vbox.setId("players");
        for (PlayerColor player : gameState.getValue().players())
            vbox.getChildren().add(createPlayerTextFlow(gameState, player, textMaker));

        return vbox;
    }

    /**
     * This method creates the text flow that displays the name, points and remaining pawns and huts of a player.
     *
     * @param gameState the observable value of the current state of the game
     * @param player the color of the player
     * @param textMaker the text maker used to generate the text in French
     * @return the text flow that displays the name, points and remaining pawns and huts of the player
     */
    private static TextFlow createPlayerTextFlow(ObservableValue<GameState> gameState,
                                                 PlayerColor player,
                                                 TextMakerFr textMaker) {
        //Player name and points
        ObservableValue<Map<PlayerColor, Integer>> pointsMap =
                gameState.map(GameState::messageBoard).map(MessageBoard::points);

        ObservableValue<String> pointsString = pointsMap.map(points ->
            STR." \{textMaker.playerName(player)} : \{textMaker.points(points.getOrDefault(player, 0))} \n"
        );

        Text playerNameAndPoints = new Text();
        playerNameAndPoints.textProperty().bind(pointsString);

        //Player circle
        Circle playerCircle = new Circle(5, ColorMap.fillColor(player));

        //TextFlow with player circle, name and points
        TextFlow textFlow =  new TextFlow(playerCircle, playerNameAndPoints);

        //Icons and space
        Node[] icons = new Node[5];
        for (int i = 0; i < 3; i++) {
            icons[i] = Icon.newFor(player, Occupant.Kind.HUT);
            icons[i].opacityProperty().bind(getOpacity(gameState, player, Occupant.Kind.HUT, i));
            textFlow.getChildren().add(icons[i]);
        }
        textFlow.getChildren().add(new Text("   "));
        for (int i = 0; i < 5; i++) {
            icons[i] = Icon.newFor(player, Occupant.Kind.PAWN);
            icons[i].opacityProperty().bind(getOpacity(gameState, player, Occupant.Kind.PAWN, i));
            textFlow.getChildren().add(icons[i]);
        }

        //Current player style
        ObservableValue<PlayerColor> currentPlayer = gameState.map(GameState::currentPlayer);
        currentPlayer.addListener((_, _, nV) -> {
            if (nV == player) textFlow.getStyleClass().add("current");
            else textFlow.getStyleClass().remove("current");
        });
        textFlow.getStyleClass().add("player");
        return textFlow;
    }

    /**
     * This method returns the opacity to use for the icon of the given player and kind.
     *
     * @param gameState the observable value of the current state of the game
     * @param player the color of the player
     * @param kind the kind of the occupant
     * @param finalI2 the index of the icon
     * @return the opacity to use for the icon of the given player and kind
     */
    private static ObservableValue<Double> getOpacity(ObservableValue<GameState> gameState,
                                                      PlayerColor player,
                                                      Occupant.Kind kind,
                                                      int finalI2) {
        return gameState.map(gS -> gS.freeOccupantsCount(player, kind) > finalI2 ? 1 : 0.1);
    }
}
