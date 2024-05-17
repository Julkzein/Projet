
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
    public void start(Stage primaryStage) {

        //Argument management
        List<String> playerNames = getParameters().getUnnamed(); //TODO : order is importnt for plyers
        String seedString = getParameters().getNamed().get("seed");

        //Gets the random tile decks
        TileDecks tileDecks = getRandomTileDecks(seedString);

        //Creation of the playerNameMap and the textMaker
        Map<PlayerColor, String> playerNameMap = new TreeMap<>();
        for (String playerName : playerNames) {
            PlayerColor playerColor = PlayerColor.values()[playerNameMap.size()];
            playerNameMap.put(playerColor, playerName);
        }
        TextMakerFr textMaker = new TextMakerFr(playerNameMap);

        GameState gameState = GameState.initial(
                playerNameMap.keySet().stream().toList(),
                tileDecks,
                textMaker);

        //Creation of the game state
        ObjectProperty<GameState> observableGameState = new SimpleObjectProperty<>(gameState);

        //Creation of the parameters for the actions
        ObjectProperty<List<String>> actions = new SimpleObjectProperty<>(new ArrayList<>());

        //Creation of the observable value of the tile to highlight
        ObjectProperty<Set<Integer>> tileToHighLight = new SimpleObjectProperty<>(Set.of());

        //Creation of the side border pane
        BorderPane sideBorderPane = getSideBorderPane(observableGameState, actions, textMaker, tileToHighLight);

        //Creation of the root parameters
        Node boardUI = getBoardUI(observableGameState, actions, tileToHighLight);

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

        observableGameState.set(gameState.withStartingTilePlaced());
    }

    /**
     * Returns a BorderPane containing the side elements of the game.
     * @param observableGameState the observable value of the game state
     * @param actions the observable value of the actions
     * @param textMaker the text maker
     * @param tileToHighLight the observable value of the tile to highlight
     * @return a BorderPane containing the side elements of the game
     */
    private static BorderPane getSideBorderPane(
            ObjectProperty<GameState> observableGameState,
            ObjectProperty<List<String>> actions,
            TextMakerFr textMaker,
            ObjectProperty<Set<Integer>> tileToHighLight) {

        //Creation of the observable value of the messages
        ObservableValue<List<MessageBoard.Message>> messages = observableGameState.map(GameState::messageBoard).map(MessageBoard::messages); //TODO : changer la mise en page des messages

        //Creation of the actions and decks vbox
        VBox actionsDecksVbox = getActionsDecksVbox(observableGameState, actions);

        BorderPane sideBorderPane = new BorderPane();
        sideBorderPane.setTop(PlayersUI.create(observableGameState, textMaker));
        sideBorderPane.setCenter(MessageBoardUI.create(messages, tileToHighLight));
        sideBorderPane.setBottom(actionsDecksVbox);
        return sideBorderPane;
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

        return new TileDecks(
                tilesByKind.get(Tile.Kind.START),
                tilesByKind.get(Tile.Kind.NORMAL),
                tilesByKind.get(Tile.Kind.MENHIR));
    }

    /**
     * Returns a VBox containing the actions and the decks.
     *
     * @param gameState the observable value of the game state
     * @return a VBox containing the actions and the decks
     */
    private static VBox getActionsDecksVbox(ObjectProperty<GameState> gameState, ObjectProperty<List<String>> actions) {

        //Creation of the action consumer
        Consumer<String> actionConsumer = str -> {
            System.out.println(Base32.decode(str));
            System.out.println(str);
            ActionEncoder.StateAction stateAction = ActionEncoder.decodeAndApply(gameState.getValue(), str);
            if (stateAction != null) {
                gameState.set(stateAction.gameState());
                List<String> newActions = new ArrayList<>(actions.getValue());
                newActions.add(stateAction.action());
                actions.set(newActions);
            }
        };

        //ObservableValue<Tile> observableCurrentTile = new SimpleObjectProperty<>(currentTile); //TODO : check si observable value ou object property
        ObjectProperty<Tile> currentTile = new SimpleObjectProperty<>(gameState.getValue().tileToPlace());
        ObservableValue<Integer> normalCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.NORMAL));
        ObservableValue<Integer> menhirCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.MENHIR));
        ObjectProperty<String> text = new SimpleObjectProperty<>("");

        Consumer<Occupant> noActionConsumer = o -> consumeOccupant(gameState, actions, o);

        gameState.addListener((_,_,nV) -> {
            if (nV.tileToPlace() != null) currentTile.set(nV.tileToPlace());
            switch (nV.nextAction()) {
                case PLACE_TILE -> text.set("");
                case OCCUPY_TILE -> text.set("Cliquez sur un occupant pour le placer ou cliquez ici pour continuer");
                case RETAKE_PAWN -> text.set("Cliquez sur un pion pour le reprendre ou cliquez ici pour continuer");
            }
        });

        //Creation of the vbox containing the actions and the decks
        VBox vbox = new VBox();
        vbox.getChildren().add(ActionsUI.create(actions, actionConsumer)); //TODO
        vbox.getChildren().add(DecksUI.create(
                currentTile,
                normalCount,
                menhirCount,
                text,
                noActionConsumer
        ));
        return vbox;
    }

    /**
     * Consumes the given occupant according to the next action of the game state.
     *
     * @param gameState the observable value of the game state
     * @param actions the observable value of the actions
     * @param o the occupant to consume
     */
    private static void consumeOccupant(ObjectProperty<GameState> gameState, ObjectProperty<List<String>> actions, Occupant o) {
        List<String> newActions = new ArrayList<>(actions.getValue());
        switch (gameState.getValue().nextAction()) { //TODO : repetition
            case OCCUPY_TILE -> {
                ActionEncoder.StateAction stateAction = ActionEncoder.withNewOccupant(gameState.getValue(), o);
                gameState.set(stateAction.gameState());
                newActions.add(stateAction.action());
                actions.set(newActions);
            }
            case RETAKE_PAWN -> {
                ActionEncoder.StateAction stateAction = ActionEncoder.withOccupantRemoved(gameState.getValue(), o);
                gameState.set(stateAction.gameState());
                newActions.add(stateAction.action());
                actions.set(newActions);
            }
            default -> {
                return;
            }
        }
    }

    /**
     * Returns the board UI.
     *
     * @param gameState the observable value of the game state
     * @return the board UI
     */
    private static Node getBoardUI(ObjectProperty<GameState> gameState, ObjectProperty<List<String>> actions, ObjectProperty<Set<Integer>> evidentTiles) {
        ObjectProperty<Rotation> currentRotation = new SimpleObjectProperty<>(Rotation.NONE);
        ObjectProperty<Set<Occupant>> visibleOccupants = new SimpleObjectProperty<>(Set.of());

        Consumer<Rotation> rotationSetter = r -> { //TODO : check
            currentRotation.set(currentRotation.getValue().add(r));
        };

        Consumer<Pos> desiredPlacement = pos -> {
            //creation of the current game state
            GameState gS = gameState.getValue();

            //creation of the placed tile
            PlacedTile pT = new PlacedTile(
                    gS.tileToPlace(),
                    gS.currentPlayer(),
                    currentRotation.getValue(),
                    pos);

            if (gS.board().canAddTile(pT)) {
                ActionEncoder.StateAction stateAction = ActionEncoder.withPlacedTile(gameState.getValue(), pT);
                gameState.set(stateAction.gameState());
                currentRotation.set(Rotation.NONE); //TODO : ps l bonne solution
                List<String> newActions = new ArrayList<>(actions.getValue());
                newActions.add(stateAction.action());
                actions.set(newActions);
            }
        };

        Consumer<Occupant> occupantConsumer = o -> {
            consumeOccupant(gameState, actions, o);
            /**GameState gS = gameState.getValue();
            switch (gS.nextAction()) {
                case OCCUPY_TILE -> {
                    ActionEncoder.StateAction stateAction = ActionEncoder.withNewOccupant(gameState.getValue(), o);
                    gameState.set(stateAction.gameState());
                    actions.getValue().add(stateAction.action());
                    actions.set(actions.getValue());
                }
                case RETAKE_PAWN -> {
                    ActionEncoder.StateAction stateAction = ActionEncoder.withOccupantRemoved(gameState.getValue(), o);
                    gameState.set(stateAction.gameState());
                    actions.getValue().add(stateAction.action());
                    actions.set(actions.getValue());
                }
            }*/
        };

        gameState.addListener((_,_,nV) -> {
            if (Objects.requireNonNull(gameState.getValue().nextAction()) == GameState.Action.OCCUPY_TILE) {
                visibleOccupants.set(nV.lastTilePotentialOccupants());
            } else {
                visibleOccupants.set(nV.board().occupants());
            }
        });

        return BoardUI.create(
                REACH,
                gameState,
                currentRotation,
                visibleOccupants,
                evidentTiles,
                rotationSetter,
                desiredPlacement,
                occupantConsumer
        );
    }
}

