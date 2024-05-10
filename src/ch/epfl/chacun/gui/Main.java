package ch.epfl.chacun.gui;

import ch.epfl.chacun.Tile;
import ch.epfl.chacun.TileDecks;
import ch.epfl.chacun.Tiles;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
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



    }

    public static void main(String[] args) {
        launch(args);
    }

}
