package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;

import static java.lang.Long.parseUnsignedLong;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.groupingBy;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        //argument gestion
        List<String> playerNames = getParameters().getUnnamed();
        String seedString = getParameters().getNamed().get("seed");

        //tile mixing
        RandomGeneratorFactory rngFactory = RandomGeneratorFactory.getDefault();
        RandomGenerator random;
        if (seedString != null) {
            long seed1 = parseUnsignedLong(seedString);
            random = rngFactory.create(seed1);
        } else {
            random = rngFactory.create();
        }
        List<Tile> tiles = List.copyOf(Tiles.TILES);
        shuffle(tiles, random);
        Map<Tile.Kind, List<Tile>> tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));
        TileDecks tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.NORMAL),
                        tilesByKind.get(Tile.Kind.MENHIR));

        //Graphe ta mère
        Map<PlayerColor, String> playerNameMap = new HashMap<>();
        for (String playerName : playerNames) {
            PlayerColor playerColor = PlayerColor.values()[playerNameMap.size()];
            playerNameMap.put(playerColor, playerName);
        }
        TextMakerFr textMaker = new TextMakerFr(playerNameMap);


        ObservableValue<GameState> gameState = new SimpleObjectProperty<>(GameState.initial(playerNameMap.keySet().stream().toList(), tileDecks, textMaker));
        ObservableValue<List<MessageBoard.Message>> messages = gameState.map(GameState::messageBoard).map(MessageBoard::messages);


        BorderPane root = new BorderPane();
        //tout check
        ObservableValue<Rotation> currentRotation = new SimpleObjectProperty<>(gameState.map(GameState::tileToPlace).map(PlacedTile::rotation));
        ObservableValue<Set<Occupant>> visibleOccupants = new SimpleObjectProperty<>(Set.of());
        ObservableValue<Set<Integer>> evidentTiles = new SimpleObjectProperty<>(Set.of());
        //consuler lol

        root.setCenter(new BoardUI(gameState, textMaker));//TODO



        BorderPane rightNode = new BorderPane();
        rightNode.setTop(PlayersUI.create(gameState, textMaker));
        rightNode.setCenter(MessageBoardUI.create(messages, new SimpleObjectProperty<>(Set.of())));
        VBox vbox = new VBox();

        //actions mon gars
        //vbox.getChildren().add(new ActionsUI(gameState, textMaker)); //TODO

        ObservableValue<Tile> currentTile = gameState.map(GameState::tileToPlace);

        ObservableValue<Integer> normalCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.NORMAL));
        ObservableValue<Integer> menhirCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.MENHIR));

        vbox.getChildren().add(new DecksUI(gameState, textMaker));
        rightNode.setBottom();



        vbox.getChildren().add(new DecksUI(currentTile, normalCount, menhirCount,));
        rightNode.setBottom(vbox);
        root.setRight(rightNode);


        Scene scene = new Scene(root);
        primaryStage.setHeight(1440);
        primaryStage.setWidth(1080);
    }

    public static void main(String[] args) {
        launch(args);
    }

}

