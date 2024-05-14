
package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
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

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        //Argument management
        List<String> playerNames = getParameters().getUnnamed();
        String seedString = getParameters().getNamed().get("seed");

        //Gets the random tile decks
        TileDecks tileDecks = getRandomTileDecks(seedString);

        //Creation of the playerNameMap and the textMaker
        Map<PlayerColor, String> playerNameMap = new HashMap<>();
        for (String playerName : playerNames) {
            PlayerColor playerColor = PlayerColor.values()[playerNameMap.size()];
            playerNameMap.put(playerColor, playerName);
        }
        TextMakerFr textMaker = new TextMakerFr(playerNameMap);

        GameState gameState = GameState.initial(
                playerNameMap.keySet().stream().toList(),
                tileDecks,
                textMaker);

        gameState = gameState.withStartingTilePlaced();

        //Creation of the game state
        ObservableValue<GameState> observableGameState = new SimpleObjectProperty<>(gameState);

        //Creation of the observable value of the messages
        ObservableValue<List<MessageBoard.Message>> messages = observableGameState.map(GameState::messageBoard).map(MessageBoard::messages);

        //Creation of the actions and decks vbox
        VBox actionsDecksVbox = getActionsDecksVbox(observableGameState);

        //Creation of the root parameters
        Node boardUI = getBoardUI(observableGameState);

        BorderPane sideBorderPane = new BorderPane();
        sideBorderPane.setTop(PlayersUI.create(observableGameState, textMaker));
        sideBorderPane.setCenter(MessageBoardUI.create(messages, new SimpleObjectProperty<>(Set.of())));
        sideBorderPane.setBottom(actionsDecksVbox);

        //Creation of the root
        BorderPane root = new BorderPane();
        root.setCenter(boardUI);
        root.setRight(sideBorderPane);

        Scene scene = new Scene(root);
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());

        primaryStage.setTitle("ChaCuN");
        primaryStage.setScene(scene);
        primaryStage.show();
        gameState = gameState.withPlacedTile(new PlacedTile(Tiles.TILES.get(11), PlayerColor.RED, Rotation.NONE, new Pos(-1, 0), null));
        //gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.HUT, 118));
    }


    /**
     * Returns a new TileDecks object with the tiles shuffled according to the given seed.
     * If the seed is null, the tiles are shuffled randomly.
     *
     * @param seedString the seed to shuffle the tiles with
     * @return a new TileDecks object with the tiles shuffled according to the given seed
     */
    private static TileDecks getRandomTileDecks(String seedString) {
        RandomGeneratorFactory<RandomGenerator> rngFactory = RandomGeneratorFactory.getDefault();
        RandomGenerator random;

        if (seedString != null) {
            long seed1 = parseUnsignedLong(seedString);
            random = rngFactory.create(seed1);
        } else random = rngFactory.create();
        
        List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        Collections.shuffle(tiles, random);

        Map<Tile.Kind, List<Tile>> tilesByKind = Tiles.TILES.stream().collect(Collectors.groupingBy(Tile::kind));

        TileDecks tileDecks = new TileDecks(
                tilesByKind.get(Tile.Kind.START),
                tilesByKind.get(Tile.Kind.NORMAL),
                tilesByKind.get(Tile.Kind.MENHIR));

        return tileDecks;
    }

    /**
     * Returns a VBox containing the actions and the decks.
     *
     * @param gameState the observable value of the game state
     * @return a VBox containing the actions and the decks
     */
    private static VBox getActionsDecksVbox(ObservableValue<GameState> gameState) {
        //Creation of the parameters for the actions
        ObservableValue<List<String>> actions = new SimpleObjectProperty<>(List.of());
        Consumer<String> actionConsumer = a -> System.out.println(Base32.decode(a)); //TODO : ecrire le consumer

        //Creation of the parameters for the decks
        //ObservableValue<Tile> observableCurrentTile = new SimpleObjectProperty<>(currentTile); //TODO : check si observable value ou object property
        ObjectProperty<Tile> currentTile = new SimpleObjectProperty<>(gameState.getValue().tileToPlace());
        ObservableValue<Integer> normalCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.NORMAL));
        ObservableValue<Integer> menhirCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.MENHIR));
        ObservableValue<String> text = new SimpleObjectProperty<>("");
        Consumer<Occupant> occupantConsumer = o -> {}; //TODO : jsp quoi mettre

        gameState.addListener((_,_,nV) -> {
            currentTile.set(nV.tileToPlace());
        });


        //Creation of the vbox containing the actions and the decks
        VBox vbox = new VBox();
        vbox.getChildren().add(ActionsUI.create(actions, actionConsumer)); //TODO
        vbox.getChildren().add(DecksUI.create(
                currentTile,
                normalCount,
                menhirCount,
                text,
                occupantConsumer
        ));
        return vbox;
    }

    /**
     * Returns the board UI.
     *
     * @param gameState the observable value of the game state
     * @return the board UI
     */
    private static Node getBoardUI(ObservableValue<GameState> gameState) {
        ObjectProperty<Rotation> currentRotation = new SimpleObjectProperty<>(Rotation.NONE);
        ObservableValue<Set<Occupant>> visibleOccupants = new SimpleObjectProperty<>(Set.of());
        ObservableValue<Set<Integer>> evidentTiles = new SimpleObjectProperty<>(Set.of());

        Consumer<Rotation> rotationSetter = r -> { //TODO : check
            currentRotation.set(currentRotation.getValue().add(r));
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

        return boardUI;
    }

    //TODO : check for retake 
}

