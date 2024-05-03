package ch.epfl.chacun;

import ch.epfl.chacun.gui.BoardUI;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class BoardUITest22 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var playerNames = Map.of(PlayerColor.RED, "Rose", PlayerColor.BLUE, "Bernard");
        var playerColors = playerNames.keySet().stream().sorted().toList();
        var tilesByKind = Tiles.TILES.stream().collect(Collectors.groupingBy(Tile::kind));
        var tileDecks = new TileDecks(tilesByKind.get(Tile.Kind.START),
                tilesByKind.get(Tile.Kind.NORMAL),
                tilesByKind.get(Tile.Kind.MENHIR));
        var textMaker = new TextMakerFr(playerNames);
        var gameState = GameState.initial(playerColors, tileDecks, textMaker);

        var tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        var visibleOccupantsP =
                new SimpleObjectProperty<>(Set.<Occupant>of());
        var highlightedTilesP =
                new SimpleObjectProperty<>(Set.<Integer>of());

        Consumer<Rotation> rotationConsumer = r -> {
            tileToPlaceRotationP.setValue((Rotation.ALL.get(r.ordinal() + tileToPlaceRotationP.getValue().ordinal()%4)));
        };
        var gameStateO = new SimpleObjectProperty<>(gameState);
        var boardNode = BoardUI
                .create(2,
                        gameStateO,
                        tileToPlaceRotationP,
                        visibleOccupantsP,
                        highlightedTilesP,
                        r -> System.out.println(STR."Rotate: \{r}"),
                        t -> System.out.println(STR."Place: \{t}"),
                        o -> System.out.println(STR."Select: \{o}"));

        gameStateO.set(gameStateO.get().withStartingTilePlaced());
        gameStateO.set(gameStateO.get().withMoreCancelledAnimals(Set.of(new Animal(5600, Animal.Kind.AUROCHS))));
        tileToPlaceRotationP.set(Rotation.RIGHT);
        gameStateO.set(gameStateO.get().withPlacedTile(new PlacedTile(Tiles.TILES.getFirst(), PlayerColor.BLUE, Rotation.RIGHT, new Pos(-1, 0))));
        visibleOccupantsP.set(Set.of(new Occupant(Occupant.Kind.HUT, 8)));
        gameStateO.set(gameStateO.get().withNewOccupant(new Occupant(Occupant.Kind.HUT, 8)));

        var rootNode = new BorderPane(boardNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}