package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.util.*;
import java.util.function.Consumer;

import static ch.epfl.chacun.GameState.Action.PLACE_TILE;
import static ch.epfl.chacun.gui.ColorMap.fillColor;
import static ch.epfl.chacun.gui.ImageLoader.*;
import static javafx.scene.effect.BlendMode.SRC_OVER;

public class BoardUI {


    //cache for images
    private static Map<Integer, Image> cache = new HashMap<>();
    public static Node create(int reach,
                              ObservableValue<GameState> gameState,
                              ObservableValue<Rotation> rotation,
                              ObservableValue<Set<Occupant>> occupants,
                              ObservableValue<Set<Integer>> evidentId,
                              Consumer<Rotation> desiredRotation,
                              Consumer<Pos> desiredPlacement,
                              Consumer<Occupant> desiredRetake) {

        //initial scrollPane and gridPane setup
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("board-scroll-pane");
        scrollPane.getStylesheets().add("board.css");

        GridPane gridPane = new GridPane();
        gridPane.setId("board-grid");
        scrollPane.setContent(gridPane);

        //initial grid creation
        for (int i = -reach; i <= reach; i++) {
            for (int j = -reach; j <= reach; j++) {
                gridPane.add(createGroup(new Pos(i, j), gameState, rotation, occupants, evidentId, desiredRotation, desiredPlacement, desiredRetake), i+reach, j+reach);
            }
        }

        return scrollPane;
    }


    private static Node createGroup(Pos pos,
                                    ObservableValue<GameState> gameState,
                                    ObservableValue<Rotation> rotation,
                                    ObservableValue<Set<Occupant>> occupants,
                                    ObservableValue<Set<Integer>> evidentId,
                                    Consumer<Rotation> desiredRotation,
                                    Consumer<Pos> desiredPlacement,
                                    Consumer<Occupant> desiredRetake) {


        Group group = new Group();  /**
        CellData cellData = new CellData(gameState.map(GameState::board).map(Board -> Board.tileAt(pos)),
                gameState.map(GameState::board).map(Board::insertionPositions),
                gameState.map(GameState::board));
        cellData.placeTile.addListener((_, _, nV) -> {
            //TODO : call des méthodes ?????
        });
         */

        ObjectBinding<CellData> cell = Bindings.createObjectBinding(() ->  {
            Rotation rotationCell = rotation.getValue();



            Color colorCell = Color.TRANSPARENT;
            Boolean hoverCell = group.hoverProperty().getValue();
            if (!evidentId.getValue().isEmpty() &&
                    !evidentId.getValue().contains(gameState.getValue().board().tileAt(pos).id())) {
                //TODO : vérifier si tuile à l'emplacement
                colorCell = Color.BLACK;
            }
            if (gameState.getValue().nextAction() == PLACE_TILE) {
                if (gameState.getValue().board().insertionPositions().contains(pos)) {
                    colorCell = fillColor(gameState.getValue().currentPlayer());
                    if (hoverCell) {
                        PlacedTile tempoTile = new PlacedTile(gameState.map(GameState::tileToPlace).getValue(), gameState.getValue().currentPlayer(), rotation.getValue(), pos);
                        if (!gameState.getValue().board().canAddTile(tempoTile)) {
                            colorCell = Color.WHITE;
                        }
                    }
                }
            }
            return new CellData();
        });

        //empty tile
        WritableImage emptyTileImage = new WritableImage(1, 1);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));
        ImageView imageView = new ImageView(emptyTileImage);
        imageView.setFitWidth(NORMAL_TILE_FIT_SIZE);
        imageView.setFitHeight(NORMAL_TILE_FIT_SIZE);
        group.getChildren().add(imageView);

        //the placed tile concerned by the group
        ObservableValue<PlacedTile> placedTile = gameState.map(GameState::board).map(Board -> Board.tileAt(pos));
        //when the tile changes (and a tile is placed), we change the image and we create the cancel token and the occupants
        placedTile.addListener((_, _, nV) -> {
            imageView.setImage(cache.computeIfAbsent(nV.id(), _ -> ImageLoader.normalImageForTile(nV.tile().id())));
            createCancelledTocken(nV.id(), pos, gameState, group);
            createOccupants(nV.id(), pos, gameState, group, occupants);
        });



        //gestion rotation
        rotation.addListener((_, _, nV) -> {
            group.setRotate(nV.degreesCW());
        });

        //gestion  frange
        ObservableValue<Set<Pos>> insertionPos = gameState.map(GameState::board).map(Board::insertionPositions);
        ObservableValue<Board> boardObservableValue = gameState.map(GameState::board);
        insertionPos.addListener((_, _, nV) -> {

            //verif si action good
            if (gameState.getValue().nextAction() == PLACE_TILE) {

                //frange sans survol

                if (nV.contains(pos)) {
                    ColorInput colorInput = new ColorInput();
                    colorInput.setHeight(NORMAL_TILE_FIT_SIZE);
                    colorInput.setWidth(NORMAL_TILE_FIT_SIZE);
                    colorInput.setPaint(fillColor(gameState.getValue().currentPlayer()));
                    Blend blend = new Blend(SRC_OVER, null, colorInput);
                    blend.setOpacity(0.5);
                    group.setEffect(blend);
                    ObservableValue<Boolean> hover = group.hoverProperty();
                    hover.addListener((_, _, nV2) -> {
                        if (hover.getValue()) {
                            PlacedTile tempoTile = new PlacedTile(gameState.map(GameState::tileToPlace).getValue(), gameState.getValue().currentPlayer(), rotation.getValue(), pos);
                            if (!boardObservableValue.getValue().canAddTile(tempoTile)) {
                                colorInput.setPaint(Color.WHITE);
                                Blend blend1 = new Blend(SRC_OVER, null, colorInput);
                                blend1.setOpacity(0.5);
                                group.setEffect(blend);
                            } else {
                                group.setEffect(null);
                            }
                        } else {
                            colorInput.setPaint(fillColor(gameState.getValue().currentPlayer()));
                            Blend blend1 = new Blend(SRC_OVER, null, colorInput);
                            blend1.setOpacity(0.5);
                            group.setEffect(blend1);
                        }
                    });








                   // colorInput.setPaint(fillColor(gameState.getValue().currentPlayer()));
                    //Blend blend = new Blend(SRC_OVER, null, colorInput);
                    //blend.setOpacity(0.5);
                    //group.setEffect(blend);

                    //frange avec survoll
                    group.setOnMouseEntered(_ -> {
                        PlacedTile tempoTile = new PlacedTile(gameState.map(GameState::tileToPlace).getValue(), gameState.getValue().currentPlayer(), rotation.getValue(), pos);
                        if (!boardObservableValue.getValue().canAddTile(tempoTile)) {
                            colorInput.setPaint(Color.WHITE);
                            Blend blend1 = new Blend(SRC_OVER, null, colorInput);
                            blend1.setOpacity(0.5);
                            group.setEffect(blend);
                        } else {
                            group.setEffect(null);
                        }
                    });
                }
            }
        });
        return group;
    }

    /**
     * Creates the SVG paths for the potential occupants of a tile
     * @param id the id of the placed tile
     * @param pos the position of the tile on the board
     * @param gameState the observable value of the game state
     * @param group the group to which the SVG paths are added
     * @param occupants the observable value of the occupants to be displayed
     */
    private static void createOccupants(int id, Pos pos, ObservableValue<GameState> gameState, Group group, ObservableValue<Set<Occupant>> occupants) {
        for (Occupant o : gameState.getValue().board().tileAt(pos).potentialOccupants()) {
            SVGPath occupantSVGPath = new SVGPath();
            if ((o.kind() == Occupant.Kind.PAWN)) occupantSVGPath.setId(STR. "pawn_\{ id }" );
            else occupantSVGPath.setId(STR. "hut_\{ id }" );
            occupantSVGPath.visibleProperty().bind(occupants.map(s -> s.contains(o)));
            group.getChildren().add(occupantSVGPath);
        }
    }

    /**
     * Creates the image views of the cancel token for the animals of a tile
     * @param id the id of the placed tile
     * @param pos the position of the tile on the board
     * @param gameState the observable value of the game state
     * @param group the group to which the image views of the cancel tokens are added
     */
    private static void createCancelledTocken(int id, Pos pos, ObservableValue<GameState> gameState, Group group) {
        ObservableValue<Board> boardObservableValue = gameState.map(GameState::board);
        ObservableValue<Set<Animal>> cancelledAnimals = boardObservableValue.map(Board::cancelledAnimals);

        for (Zone.Meadow meadow : boardObservableValue.getValue().tileAt(pos).meadowZones()) {
            for (Animal a : meadow.animals()) {
                ImageView cancelTocken = new ImageView();
                cancelTocken.setId(STR."marker_\{id}");
                cancelTocken.getStyleClass().add("marker");
                cancelTocken.setFitWidth(MARKER_FIT_SIZE);
                cancelTocken.setFitHeight(MARKER_FIT_SIZE);
                cancelTocken.visibleProperty().bind(cancelledAnimals.map(s -> s.contains(a)));
                group.getChildren().add(cancelTocken);
            }
        }
    }

    private record CellData (Image image, int rotation, Color veilColor) {
        private static Map<Integer, Image> cache = new HashMap<>();
        private static WritableImage emptyTileImage = new WritableImage(1, 1);

        static ImageView imageView = new ImageView(emptyTileImage);

        public CellData(PlacedTile pLacedTile) {
            rotation = pLacedTile.rotation().degreesCW();
            veilColor = fillColor(pLacedTile.placer());
        }

        /**
        private Image gestionImage() {
            if () {
                if (Node.isHover()) {
                   //image avec survol;
                } else {
                    //image sans survol;
                }
            }
            WritableImage suitebt = new WritableImage(1, 1);
            emptyTileImage
                    .getPixelWriter()
                    .setColor(0, 0, Color.gray(0.98));
            ImageView imageView = new ImageView(emptyTileImage);
            return imageView;
        }
            */
    }

}
