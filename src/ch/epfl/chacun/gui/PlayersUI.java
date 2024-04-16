package ch.epfl.chacun.gui;

import ch.epfl.chacun.GameState;
import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMakerFr;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PlayersUI {
    private PlayersUI() {}
    public static Node create(ObservableValue<GameState> gameState, TextMakerFr textMaker) {
        VBox vbox = new VBox();
        vbox.getStylesheets().add(STR."/players.css");
        vbox.setId("players");
        for (PlayerColor player : PlayerColor.ALL) {
            if (textMaker.playerName(player) != null) {
                vbox.getChildren().add(createPlayerTextFlow(gameState, player, textMaker));
            }
        }
        ObservableValue<PlayerColor> currentPlayerO = gameState.map(GameState::currentPlayer);
        currentPlayerO.addListener((o, oldPlayer, newPlayer) -> {
            vbox.getChildren().get(oldPlayer).getStyleClass().remove("current"));
            vbox.getChildren().get(newPlayer).getStyleClass().add("current");
        });


    }

    private static TextFlow createPlayerTextFlow(ObservableValue<GameState> gameState, PlayerColor player, TextMakerFr textMaker) {
        Circle playerCircle = new Circle(5, ColorMap.fillColor(player));
        //Text playerNameAndPoints = new Text(STR."\{textMaker.playerName()} : \{textMaker.Points(gameState.points(player))}");
        //Icon hutIcon = Icon.newFor(player, Occupant.Kind.HUT);
        //Icon pawnIcon = Icon.newFor(player, Occupant.Kind.PAWN);
        TextFlow textFlow =  new TextFlow(playerCircle, playerNameAndPoints, hutIcon, hutIcon, hutIcon, pawnIcon, pawnIcon, pawnIcon, pawnIcon, pawnIcon);
        textFlow.getStyleClass().add("player");
    }
}
