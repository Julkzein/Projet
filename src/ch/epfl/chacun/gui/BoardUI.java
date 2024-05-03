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
import javafx.scene.effect.BlendMode;
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


    private static Group createGroup(Pos pos,
                                    ObservableValue<GameState> gameState,
                                    ObservableValue<Rotation> rotation,
                                    ObservableValue<Set<Occupant>> occupants,
                                    ObservableValue<Set<Integer>> evidentId,
                                    Consumer<Rotation> desiredRotation,
                                    Consumer<Pos> desiredPlacement,
                                    Consumer<Occupant> desiredRetake) {



        Group group = new Group();

        ObjectBinding<CellData> cellData = Bindings.createObjectBinding(() ->  {

            WritableImage emptyTileImage = new WritableImage(1, 1);
            emptyTileImage
                    .getPixelWriter()
                    .setColor(0, 0, Color.gray(0.98));
            
            //rotation
            Rotation rotationCell = rotation.getValue();
            
            //tile 
            PlacedTile placedTile = gameState.getValue().board().tileAt(pos);
            Image imageCell = placedTile == null ? emptyTileImage : cache.computeIfAbsent(placedTile.id(), _ -> normalImageForTile(placedTile.tile().id()));

            //color
            Color colorCell = Color.TRANSPARENT;
            Boolean hoverCell = group.hoverProperty().getValue();
            if (!evidentId.getValue().isEmpty()) {
                if (placedTile != null && !evidentId.getValue().contains(placedTile.id())) {
                    colorCell = Color.BLACK;
                }
            }
            else if (placedTile == null && gameState.getValue().nextAction() == PLACE_TILE) {
                if (!hoverCell) {
                    colorCell = fillColor(Objects.requireNonNull(gameState.getValue().currentPlayer()));
                } else {
                    if (!gameState.getValue().board().canAddTile(new PlacedTile(gameState.getValue().tileToPlace(), gameState.getValue().currentPlayer(), rotationCell, pos))) {
                        if (gameState.getValue().tileToPlace() != null) {
                            imageCell = cache.computeIfAbsent(gameState.getValue().tileToPlace().id(), _ -> normalImageForTile(gameState.getValue().tileToPlace().id()));
                        }
                    } else {
                        colorCell = Color.WHITE;
                    }
                }
            }

            return new CellData(imageCell, rotationCell, colorCell);
        });

        //empty tile
        ImageView imageView = new ImageView();
        imageView.setFitWidth(NORMAL_TILE_FIT_SIZE);
        imageView.setFitHeight(NORMAL_TILE_FIT_SIZE);
        imageView.imageProperty().bind(cellData.map(CellData::image));

        //the placed tile concerned by the group
        ObservableValue<PlacedTile> placedTile = gameState.map(GameState::board).map(Board -> Board.tileAt(pos));
        //when the tile changes (and a tile is placed), we change the image and create the cancel token and the occupants
        placedTile.addListener((_, _, nV) -> {
            createCancelledTocken(nV.id(), pos, gameState, group);
            createOccupants(nV.id(), pos, gameState, group, occupants);
        });



        //gestion rotation
        group.rotateProperty().bind(cellData.map(CellData::rotation).map(Rotation::degreesCW));

        //gestion frange

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

    private record CellData (Image image, Rotation rotation, Color veilColor) {}

}
