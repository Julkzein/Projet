package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static ch.epfl.chacun.GameState.Action.PLACE_TILE;
import static ch.epfl.chacun.gui.ColorMap.fillColor;
import static ch.epfl.chacun.gui.ImageLoader.*;
import static javafx.scene.effect.BlendMode.SRC_OVER;

/**
 * This represents the UI elements of the board as they evolve during a game.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class BoardUI {

    //cache for images
    private final static Map<Integer, Image> cache = new HashMap<>();

    /**
     * This method creates the UI elements of the board as they evolve during a game.
     *
     * @param reach the reach of the Board
     * @param gameState the observable value of the game state
     * @param rotation the observable value of the rotation
     * @param occupants the observable value of the occupants
     * @param evidentId the observable value of the set of ids of the tiles put in evidence
     * @param desiredRotation the consumer of the desired rotation
     * @param desiredPlacement the consumer of the desired placement
     * @param desiredRetake the consumer of the desired retake
     * @return the node representing the board
     */
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
        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);
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


    /**
     * This method creates a group of UI elements representing a tile on the board.
     *
     * @param pos the position of the tile on the board
     * @param gameState the observable value of the game state
     * @param rotation the observable value of the rotation
     * @param occupants the observable value of the occupants
     * @param evidentId the observable value of the set of ids of the tiles put in evidence
     * @param desiredRotation the consumer of the desired rotation
     * @param desiredPlacement the consumer of the desired placement
     * @param desiredRetake the consumer of the desired retake
     * @return the group representing the tile
     */
    private static Group createGroup(Pos pos,
                                    ObservableValue<GameState> gameState,
                                    ObservableValue<Rotation> rotation,
                                    ObservableValue<Set<Occupant>> occupants,
                                    ObservableValue<Set<Integer>> evidentId,
                                    Consumer<Rotation> desiredRotation,
                                    Consumer<Pos> desiredPlacement,
                                    Consumer<Occupant> desiredRetake) {



        Group group = new Group();

        //creation of the cell data that will evolve
        ObjectBinding<CellData> cellData = Bindings.createObjectBinding(() ->  {

            //rotation data
            Rotation rotationCell = rotation.getValue();
            
            //image data
            PlacedTile placedTile = gameState.getValue().board().tileAt(pos);
            WritableImage emptyTileImage = new WritableImage(1, 1);
            emptyTileImage
                    .getPixelWriter()
                    .setColor(0, 0, Color.gray(0.98));

            Image imageCell = placedTile == null ? emptyTileImage : cache.computeIfAbsent(placedTile.id(), _ -> normalImageForTile(placedTile.tile().id()));


            //color data
            Color colorCell = Color.TRANSPARENT;
            Boolean hoverCell = group.hoverProperty().getValue();
            if (!evidentId.getValue().isEmpty()) {
                if (placedTile != null && !evidentId.getValue().contains(placedTile.id())) {
                    colorCell = Color.BLACK;
                }
            }
            if (placedTile == null && gameState.getValue().nextAction() == PLACE_TILE && gameState.getValue().board().insertionPositions().contains(pos)) {
                if (!hoverCell) {
                    colorCell = fillColor(Objects.requireNonNull(gameState.getValue().currentPlayer()));
                    rotationCell = Rotation.NONE; //TODO : si c fout l mrd c ici
                } else {
                    imageCell = cache.computeIfAbsent(gameState.getValue().tileToPlace().id(), _ -> normalImageForTile(gameState.getValue().tileToPlace().id()));
                    if (!gameState.getValue().board().canAddTile(new PlacedTile(gameState.getValue().tileToPlace(), gameState.getValue().currentPlayer(), rotationCell, pos))) {
                        colorCell = Color.WHITE;
                    }
                }
            }
            return new CellData(imageCell, rotationCell, colorCell);
        }, gameState, rotation, evidentId, group.hoverProperty());

        //empty tile that will change as the cell data evolves
        ImageView imageView = new ImageView();
        imageView.setFitWidth(NORMAL_TILE_FIT_SIZE);
        imageView.setFitHeight(NORMAL_TILE_FIT_SIZE);
        imageView.imageProperty().bind(cellData.map(CellData::image));
        group.getChildren().add(imageView);

        //the placed tile concerned by the group
        ObservableValue<PlacedTile> placedTile = gameState.map(GameState::board).map(Board -> Board.tileAt(pos));

        //when the tile changes (and a tile is placed), we change the image and create the cancel token and the occupants
        placedTile.addListener((_, _, nV) -> {
            group.rotateProperty().unbind();
            createCancelledToken(nV.id(), pos, gameState, group);
            createOccupants(pos, gameState, group, occupants, desiredRetake);
        });

        //rotation binding
        group.rotateProperty().bind(cellData.map(CellData::rotation).map(Rotation::degreesCW));

        //veil color binding
        Blend blend = new Blend(SRC_OVER);
        ColorInput colorInputBlend = new ColorInput();
        colorInputBlend.paintProperty().bind(cellData.map(CellData::veilColor));
        colorInputBlend.setX(pos.x());
        colorInputBlend.setY(pos.y());
        colorInputBlend.setHeight(NORMAL_TILE_FIT_SIZE);
        colorInputBlend.setWidth(NORMAL_TILE_FIT_SIZE);
        blend.setOpacity(0.5);
        blend.topInputProperty().bind(cellData.map(_ -> colorInputBlend));
        group.setEffect(blend);

        //click handling
        group.setOnMouseClicked( e -> {
            if (e.isStillSincePress()) {
                switch (e.getButton()) {
                    case PRIMARY -> {
                        if (gameState.getValue().nextAction() == PLACE_TILE && gameState.getValue().board().insertionPositions().contains(pos)) {
                            desiredPlacement.accept(pos);
                        }
                    }
                    case SECONDARY -> {
                        if (gameState.getValue().nextAction() == PLACE_TILE && gameState.getValue().board().insertionPositions().contains(pos)) {
                            if (e.isAltDown()) {
                                desiredRotation.accept(Rotation.RIGHT);
                            } else {
                                desiredRotation.accept(Rotation.LEFT);
                            }
                        }
                    }
                    default -> {}
                }
            }
        });
        return group;
    }

    /**
     * Creates the SVG paths for the potential occupants of a tile
     * @param pos the position of the tile on the board
     * @param gameState the observable value of the game state
     * @param group the group to which the SVG paths are added
     * @param occupants the observable value of the occupants to be displayed
     */
    private static void createOccupants(Pos pos, ObservableValue<GameState> gameState, Group group, ObservableValue<Set<Occupant>> occupants, Consumer<Occupant> desiredRetake) {
        for (Occupant o : Objects.requireNonNull(gameState.getValue().board().tileAt(pos)).potentialOccupants()) {
            Node occupantSVGPath = Icon.newFor(gameState.getValue().currentPlayer(), o.kind());
            occupantSVGPath.setId(STR."\{o.kind().toString().toLowerCase()}_\{o.zoneId()}" );
            occupantSVGPath.visibleProperty().bind(occupants.map(s -> s.contains(o)));
            occupantSVGPath.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.isStillSincePress()) {
                    desiredRetake.accept(o);
                }
            });
            occupantSVGPath.rotateProperty().bind(group.rotateProperty().negate());
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
    private static void createCancelledToken(int id, Pos pos, ObservableValue<GameState> gameState, Group group) {
        ObservableValue<Board> boardObservableValue = gameState.map(GameState::board);
        ObservableValue<Set<Animal>> cancelledAnimals = boardObservableValue.map(Board::cancelledAnimals);

        for (Zone.Meadow meadow : Objects.requireNonNull(boardObservableValue.getValue().tileAt(pos)).meadowZones()) {
            for (Animal a : meadow.animals()) {
                ImageView cancelToken = new ImageView();
                cancelToken.setId(STR."marker_\{id}");
                cancelToken.getStyleClass().add("marker");
                cancelToken.setFitWidth(MARKER_FIT_SIZE);
                cancelToken.setFitHeight(MARKER_FIT_SIZE);
                cancelToken.visibleProperty().bind(cancelledAnimals.map(s -> s.contains(a)));
                group.getChildren().add(cancelToken);
            }
        }

    }

    //private record to store the data of a cell
    private record CellData (Image image, Rotation rotation, Color veilColor) {}

}
