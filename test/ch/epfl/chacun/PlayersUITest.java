package ch.epfl.chacun;

import ch.epfl.chacun.gui.MessageBoardUI;
import ch.epfl.chacun.gui.PlayersUI;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class PlayersUITest extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {

        var playerNames = Map.of(PlayerColor.RED, "Rose",
                PlayerColor.BLUE, "Bernard");
        var playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));
        var tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.NORMAL),
                        tilesByKind.get(Tile.Kind.MENHIR));

        var textMaker = new TextMakerFr(playerNames);

        /**
        var gameState =
                GameState.initial(playerColors,
                        tileDecks,
                        textMaker);

        //var gameStateO = new SimpleObjectProperty<>(gameState);

        /**
        var playersNode = PlayersUI.create(gameStateO, textMaker);
        var rootNode = new BorderPane(playersNode);
        primaryStage.setScene(new Scene(rootNode));
        */

        System.out.println("test");

        ObservableValue<List<MessageBoard.Message>> messages = new SimpleObjectProperty<>(List.of(new MessageBoard.Message("test text", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of())));
        ObjectProperty<Set<Integer>> tileIds = new SimpleObjectProperty<>(Set.of());
        var messageBoardNode = MessageBoardUI.create(messages, tileIds);
        var root2Node = new ScrollPane(messageBoardNode);
        primaryStage.setScene(new Scene(root2Node));


        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}