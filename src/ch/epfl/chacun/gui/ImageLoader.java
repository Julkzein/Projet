package ch.epfl.chacun.gui;

import javafx.scene.image.Image;

/**
 * This class loads the images of the tiles used in the game.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class ImageLoader {
    //private constructor to prevent instantiation
    private ImageLoader() {}

    public static final int LARGE_TILE_PIXEL_SIZE = 512;
    public static final int LARGE_TILE_FIT_SIZE = 256;
    public static final int NORMAL_TILE_PIXEL_SIZE = 256;
    public static final int NORMAL_TILE_FIT_SIZE = 128;
    public static final int MARKER_PIXEL_SIZE = 96; //TODO : check if used
    public static final int MARKER_FIT_SIZE = 48;

    /**
     * This method returns the image of the tile with the given id in normal size.
     *
     * @param id the id of the tile
     * @return the image of the tile with the given id in normal size
     */
    public static Image normalImageForTile(int id) {
        return new Image(STR."/\{NORMAL_TILE_PIXEL_SIZE}/\{(id < 10) ? "0" : ""}\{id}.jpg");
    }

    /**
     * This method returns the image of the tile with the given id in large size.
     *
     * @param id the id of the tile
     * @return the image of the tile with the given id in large size
     */
    public static Image largeImageForTile(int id) {
        return new Image(STR."/\{LARGE_TILE_PIXEL_SIZE}/\{(id < 10) ? "0" : ""}\{id}.jpg");
    }
}
