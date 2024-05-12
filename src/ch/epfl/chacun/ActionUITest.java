package ch.epfl.chacun;

import ch.epfl.chacun.gui.ActionsUI;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ActionUITest extends Application {
    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        var playerNames = Map.of(PlayerColor.RED, "Rose",
                PlayerColor.GREEN, "Bernard");
        var playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = ch.epfl.chacun.Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        var tileDecks = new TileDecks(tilesByKind.get(Tile.Kind.START),
                tilesByKind.get(Tile.Kind.NORMAL),
                tilesByKind.get(Tile.Kind.MENHIR));
        var textMaker = new TextMakerFr(playerNames);
        var gameState = GameState.initial(playerColors,
                tileDecks,
                textMaker);


        var tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        var visibleOccupantsP =
                new SimpleObjectProperty<>(Set.<Occupant>of());
        var highlightedTilesP =
                new SimpleObjectProperty<>(Set.<Integer>of());

        var actionListObs = new SimpleObjectProperty<>(List.of("AG","C","AM","EP", "B2", "B6"));
        var actionUINode = ActionsUI.create(actionListObs, r -> System.out.println("enterHasBeenPressed: " + ActionEncoder.decodeAndApply(gameState,r)));

        //gameStateO.set(gameStateO.get().withStartingTilePlaced());

        var rootNode = new BorderPane(actionUINode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}
