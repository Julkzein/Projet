
package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Board.REACH;
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
        RandomGeneratorFactory<RandomGenerator> rngFactory = RandomGeneratorFactory.getDefault();
        RandomGenerator random;
        if (seedString != null) {
            long seed1 = parseUnsignedLong(seedString);
            random = rngFactory.create(seed1);
        } else {
            random = rngFactory.create();
        }
        List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        shuffle(tiles, random); //TODO : check dns le cs ou ps de seed pssée en rg
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


        ObservableValue<GameState> gameState = new SimpleObjectProperty<>(GameState.initial(playerNameMap.keySet().stream().toList(), tileDecks, textMaker).withStartingTilePlaced());
        ObservableValue<List<MessageBoard.Message>> messages = gameState.map(GameState::messageBoard).map(MessageBoard::messages);

        BorderPane root = new BorderPane();
        //tout check
        ObservableValue<Rotation> currentRotation = new SimpleObjectProperty<>(/**gameState.map(GameState::tileToPlace).map((Tile t) -> PlacedTile.rotation(t))*/);
        ObservableValue<Set<Occupant>> visibleOccupants = new SimpleObjectProperty<>(Set.of());
        ObservableValue<Set<Integer>> evidentTiles = new SimpleObjectProperty<>(Set.of());

        Consumer<Rotation> rotationSetter = r -> { //TODO : check
            currentRotation.getValue().quarterTurnsCW();
        };
        Consumer<Pos> desiredPlacement = pos -> {
            gameState.getValue().withPlacedTile(new PlacedTile(
                    gameState.getValue().tileToPlace(),
                    gameState.getValue().currentPlayer(),
                    currentRotation.getValue(),
                    pos));
        };
        Consumer<Occupant> desiredRetake = occupant -> {
            gameState.getValue().withOccupantRemoved(occupant);
        };

        Node boardUI = BoardUI.create(
                REACH,
                gameState,
                currentRotation,
                visibleOccupants, //TODO : pk visible occupants ?
                evidentTiles,
                rotationSetter,
                desiredPlacement,
                desiredRetake
            );

        root.setCenter(boardUI);

        BorderPane rightNode = new BorderPane();
        rightNode.setTop(PlayersUI.create(gameState, textMaker));
        rightNode.setCenter(MessageBoardUI.create(messages, new SimpleObjectProperty<>(Set.of())));
        VBox vbox = new VBox();

        //actions mon gars

        ObservableValue<List<String>> actions = new SimpleObjectProperty<>(List.of());
        Consumer<String> actionConsumer = a -> System.out.println("Action: " + a);
        vbox.getChildren().add(ActionsUI.create(actions, actionConsumer)); //TODO

        ObservableValue<Tile> currentTile = gameState.map(GameState::tileToPlace);
        ObservableValue<Integer> normalCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.NORMAL));
        ObservableValue<Integer> menhirCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.MENHIR));
        ObservableValue<String> text = new SimpleObjectProperty<>("");
        Consumer<Occupant> occupantConsumer = o -> {}; //TODO : jsp quoi mettre

        vbox.getChildren().add(DecksUI.create(
                currentTile,
                normalCount,
                menhirCount,
                text,
                occupantConsumer
                ));

        rightNode.setBottom(vbox);
        root.setRight(rightNode);

        Scene scene = new Scene(root);
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());

        primaryStage.setTitle("ChaCuN");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

