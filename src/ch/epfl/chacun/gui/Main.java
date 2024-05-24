
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
import static ch.epfl.chacun.Tiles.TILES;
import static java.lang.Long.parseUnsignedLong;

public class Main extends Application {

    private static final int HEIGHT = 1080;
    private static final int WIDTH = 1440;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //Argument management
        List<String> playerNames = getParameters().getUnnamed();
        String seedString = getParameters().getNamed().get("seed");

        //Gets the random tile decks
        TileDecks tileDecks = getRandomTileDecks(seedString);



        //        //Logboat Deck Normale
//       TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(3), Tiles.TILES.get(4), Tiles.TILES.get(5), Tiles.TILES.get(6), Tiles.TILES.get(7), Tiles.TILES.get(11), Tiles.TILES.get(1)),
//                List.of(Tiles.TILES.get(93)));

//        //LogBoat limit
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of( Tiles.TILES.get(14), Tiles.TILES.get(1), Tiles.TILES.get(60)),
//                List.of());

//        //Chaman Deck
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(36), Tiles.TILES.get(35), Tiles.TILES.get(27), Tiles.TILES.get(39), Tiles.TILES.get(37)),
//                List.of(Tiles.TILES.get(88)));

//////        //Hunting trap full animaux
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(61), Tiles.TILES.get(62), Tiles.TILES.get(15), Tiles.TILES.get(35), Tiles.TILES.get(16), Tiles.TILES.get(36),  Tiles.TILES.get(37), Tiles.TILES.get(76), Tiles.TILES.get(64), Tiles.TILES.get(68), Tiles.TILES.get(1)),
//                List.of(Tiles.TILES.get(94)));

        //Hunting trap no animal


//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(4), Tiles.TILES.get(3), Tiles.TILES.get(15), Tiles.TILES.get(46), Tiles.TILES.get(29), Tiles.TILES.get(12), Tiles.TILES.get(18), Tiles.TILES.get(1), Tiles.TILES.get(37), Tiles.TILES.get(7), Tiles.TILES.get(8), Tiles.TILES.get(9), Tiles.TILES.get(22), Tiles.TILES.get(23)),
//                List.of(Tiles.TILES.get(94)));


//        //Hunting trap tiger
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(61), Tiles.TILES.get(62), Tiles.TILES.get(18), Tiles.TILES.get(35), Tiles.TILES.get(16), Tiles.TILES.get(36),  Tiles.TILES.get(37), Tiles.TILES.get(31), Tiles.TILES.get(64), Tiles.TILES.get(68), Tiles.TILES.get(1)),
//                List.of(Tiles.TILES.get(94)));

//        //Pit trap full animaux
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(61), Tiles.TILES.get(62), Tiles.TILES.get(15), Tiles.TILES.get(35), Tiles.TILES.get(16), Tiles.TILES.get(36),  Tiles.TILES.get(37), Tiles.TILES.get(76), Tiles.TILES.get(64), Tiles.TILES.get(68), Tiles.TILES.get(1)),
//                List.of(Tiles.TILES.get(92)));

//        //Pit trap tiger et PitFire
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(61), Tiles.TILES.get(62), Tiles.TILES.get(18), Tiles.TILES.get(35), Tiles.TILES.get(16), Tiles.TILES.get(36),  Tiles.TILES.get(37), Tiles.TILES.get(31), Tiles.TILES.get(64), Tiles.TILES.get(68), Tiles.TILES.get(15), Tiles.TILES.get(26)),
//                List.of(Tiles.TILES.get(92), Tiles.TILES.get(85)));

//        //Logboat Deck Normale
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(3), Tiles.TILES.get(4), Tiles.TILES.get(5), Tiles.TILES.get(6), Tiles.TILES.get(7), Tiles.TILES.get(11), Tiles.TILES.get(1)),
//                List.of(Tiles.TILES.get(91)));

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
        
        ObjectProperty<Rotation> visibleRotation = new SimpleObjectProperty<>(Rotation.NONE);

        //Creation of the side border pane
        BorderPane sideBorderPane = getSideBorderPane(observableGameState, actions, textMaker, tileToHighLight, tileDecks, visibleRotation);

        //Creation of the root parameters
        Node boardUI = getBoardUI(observableGameState, actions, tileToHighLight, visibleRotation);

        //Creation of the root
        BorderPane root = new BorderPane();
        root.setCenter(boardUI);
        root.setRight(sideBorderPane);

        Scene scene = new Scene(root);
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight()); //TODO : avant rendu, mettre les constantes
        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());

        primaryStage.setTitle("ChaCuN");
        primaryStage.setScene(scene);
        primaryStage.show();

        observableGameState.set(gameState.withStartingTilePlaced()); //TODO : belek belek

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
            ObjectProperty<Set<Integer>> tileToHighLight,
            TileDecks tiledecks, ObjectProperty<Rotation> visibleRotation) {

        //Creation of the observable value of the messages
        ObservableValue<List<MessageBoard.Message>> messages = observableGameState.map(GameState::messageBoard).map(MessageBoard::messages); //TODO : changer la mise en page des messages

        //Creation of the actions and decks vbox
        VBox actionsDecksVbox = getActionsDecksVbox(observableGameState, actions, visibleRotation);

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

        RandomGenerator random = seedString == null ? RandomGeneratorFactory.getDefault().create() : RandomGeneratorFactory.getDefault().create(parseUnsignedLong(seedString));


        List<Tile> tiles = new ArrayList<>(TILES);

        Collections.shuffle(tiles, random);

        Map<Tile.Kind, List<Tile>> tilesByKind = tiles.stream().collect(Collectors.groupingBy(Tile::kind));

        List<Tile> menhir = tilesByKind.get(Tile.Kind.MENHIR);


        List<Tile> norml = tilesByKind.get(Tile.Kind.NORMAL);

        return new TileDecks(
                tilesByKind.get(Tile.Kind.START),
                norml,
                menhir);
    }

    /**
     * Returns a VBox containing the actions and the decks.
     *
     * @param gameState the observable value of the game state
     * @return a VBox containing the actions and the decks
     */
    private static VBox getActionsDecksVbox(ObjectProperty<GameState> gameState, ObjectProperty<List<String>> actions, ObjectProperty<Rotation> visibleRotation) {

        //Creation of the action consumer
        Consumer<String> actionConsumer = str -> {
            ActionEncoder.StateAction stateAction = ActionEncoder.decodeAndApply(gameState.getValue(), str);

            if (stateAction != null) {
                visibleRotation.setValue(stateAction.gameState().board().lastPlacedTile().rotation());
                gameState.set(stateAction.gameState());
                List<String> newActions = new LinkedList<>(actions.getValue());
                newActions.add(stateAction.action());
                actions.set(newActions);
            }
        };

        ObservableValue<Tile> currentTile = gameState.map(GameState::tileToPlace);

        ObservableValue<Integer> normalCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.NORMAL));
        ObservableValue<Integer> menhirCount = gameState.map(GameState::tileDecks).map(TileDecks -> TileDecks.deckSize(Tile.Kind.MENHIR));
        ObjectProperty<String> text = new SimpleObjectProperty<>("");

        Consumer<Occupant> noActionConsumer = o -> consumeOccupant(gameState, actions, o);

        gameState.addListener((_,_,nV) -> {
            switch (nV.nextAction()) {
                case PLACE_TILE -> text.set("");
                case OCCUPY_TILE -> text.set("Cliquez sur un occupant pour le placer ou cliquez ici pour continuer");
                case RETAKE_PAWN -> text.set("Cliquez sur un pion pour le reprendre ou cliquez ici pour continuer");
            }
        });

        //Creation of the vbox containing the actions and the decks
        VBox vbox = new VBox();
        vbox.getChildren().add(ActionsUI.create(actions, actionConsumer));
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
        GameState gS = gameState.getValue();
        switch (gS.nextAction()) { //TODO : repetition
            case OCCUPY_TILE -> {
                if (o == null || gS.lastTilePotentialOccupants().contains(o)) {
                    ActionEncoder.StateAction stateAction = ActionEncoder.withNewOccupant(gameState.getValue(), o);
                    gameState.set(stateAction.gameState());
                    newActions.add(stateAction.action());
                    actions.set(newActions);
                }
            }
            case RETAKE_PAWN -> {
                if (o == null ||
                        (gS.board().occupants().contains(o)
                        && o.kind() == Occupant.Kind.PAWN
                        && gS.currentPlayer() == gS.board().tileWithId(Zone.tileId(o.zoneId())).placer())) {

                    ActionEncoder.StateAction stateAction = ActionEncoder.withOccupantRemoved(gameState.getValue(), o);
                    gameState.set(stateAction.gameState());
                    newActions.add(stateAction.action());
                    actions.set(newActions);
                }
            }
            default -> {}
        }
    }

    /**
     * Returns the board UI.
     *
     * @param gameState the observable value of the game state
     * @return the board UI
     */
    private static Node getBoardUI(ObjectProperty<GameState> gameState, ObjectProperty<List<String>> actions, ObjectProperty<Set<Integer>> evidentTiles, ObjectProperty<Rotation> visbleRotation) {
        ObjectProperty<Set<Occupant>> visibleOccupants = new SimpleObjectProperty<>(Set.of());

        Consumer<Rotation> rotationSetter = r -> { //TODO : check
            visbleRotation.set(visbleRotation.getValue().add(r));
        };

        Consumer<Pos> desiredPlacement = pos -> {
            //creation of the current game state
            GameState gS = gameState.getValue();

            //creation of the placed tile
            PlacedTile pT = new PlacedTile(
                    gS.tileToPlace(),
                    gS.currentPlayer(),
                    visbleRotation.getValue(),
                    pos);


            if (gS.board().canAddTile(pT)) {
                ActionEncoder.StateAction stateAction = ActionEncoder.withPlacedTile(gameState.getValue(), pT);
                gameState.set(stateAction.gameState());
                visbleRotation.set(Rotation.NONE); //TODO : ps l bonne solution
                List<String> newActions = new ArrayList<>(actions.getValue());
                newActions.add(stateAction.action());
                actions.set(newActions);
            }
        };

        Consumer<Occupant> occupantConsumer = o -> {
            consumeOccupant(gameState, actions, o);
        };

        gameState.addListener((_,_,nV) -> { //crete binding with board
            if (Objects.requireNonNull(gameState.getValue().nextAction()) == GameState.Action.OCCUPY_TILE) {
                Set<Occupant> occupantsToDisplay = new HashSet<>(nV.lastTilePotentialOccupants());
                occupantsToDisplay.addAll(nV.board().occupants());
                visibleOccupants.set(occupantsToDisplay);
            } else {
                visibleOccupants.set(nV.board().occupants());
            }
        });

        return BoardUI.create(
                REACH,
                gameState,
                visbleRotation,
                visibleOccupants,
                evidentTiles,
                rotationSetter,
                desiredPlacement,
                occupantConsumer
        );
    }
}

