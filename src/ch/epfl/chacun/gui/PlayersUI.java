package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

public class PlayersUI {
    private PlayersUI() {}
    public static Node create(ObservableValue<GameState> gameState, TextMakerFr textMaker) {
        VBox vbox = new VBox();
        vbox.getStylesheets().add("/players.css");
        vbox.setId("players");
        for (PlayerColor player : PlayerColor.ALL) {
            if (textMaker.playerName(player) != null) {
                vbox.getChildren().add(createPlayerTextFlow(gameState, player, textMaker));
            }
        }
        return vbox;
    }

    private static TextFlow createPlayerTextFlow(ObservableValue<GameState> gameState, PlayerColor player, TextMakerFr textMaker) {
        //points
        ObservableValue<Map<PlayerColor, Integer>> pointsO = gameState.map(GameState::messageBoard).map(MessageBoard::points);
        ObservableValue<String> pointTextO = pointsO.map(points -> STR."\{player} : \{textMaker.points(points.get(player))}");
        Text playerNameAndPoints = new Text();
        playerNameAndPoints.textProperty().bind(pointTextO);

        //player circle
        Circle playerCircle = new Circle(5, ColorMap.fillColor(player));

        //textFlow
        TextFlow textFlow =  new TextFlow(playerCircle, playerNameAndPoints);


        //icons and space
        Node[] icons = new Node[8];
        for (int i = 0; i < 3; i++) {
            icons[i] = Icon.newFor(player, Occupant.Kind.HUT);
            final int finalI1 = i;
            icons[i].opacityProperty().bind(gameState.map(gameState1 -> gameState1.freeOccupantsCount(player, Occupant.Kind.HUT) > finalI1 ? 1 : 0.1));
            textFlow.getChildren().add(icons[i]);
        }
        textFlow.getChildren().add(new Text("   "));
        for (int i = 3; i < 8; i++) {
            icons[i] = Icon.newFor(player, Occupant.Kind.PAWN);
            final int finalI2 = i;
            icons[i].opacityProperty().bind(gameState.map(gameState1 -> gameState1.freeOccupantsCount(player, Occupant.Kind.PAWN) > finalI2 ? 1 : 0.1));
            textFlow.getChildren().add(icons[i]);
        }

        //current player
        ObservableValue<PlayerColor> currentPlayerO = gameState.map(GameState::currentPlayer);
        if (currentPlayerO.getValue() == player) {
            textFlow.getStyleClass().add(".player.current");
        }
        textFlow.getStyleClass().add(".player");
        return textFlow;
    }
}
