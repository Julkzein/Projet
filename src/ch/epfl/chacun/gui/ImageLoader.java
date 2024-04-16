package ch.epfl.chacun.gui;

import javafx.scene.image.Image;

public class ImageLoader {
    private ImageLoader() {}

    public static final int LARGE_TITLE_PIXEL_SIZE = 512;
    public static final int LARGE_TITLE_FIT_SIZE = 256;
    public static final int NORMAL_TITLE_PIXEL_SIZE = 256;
    public static final int NORMAL_TITLE_FIT_SIZE = 128;
    public static final int MARKER_PIXEL_SIZE = 96;
    public static final int MARKER_FIT_SIZE = 48;

    public static Image normalImageForTile(int id) {
        return new Image(STR."/256/\{id}.png"); //appel truc au dessus ?
    }

    public static Image largeImageForTile(int id) {
        return new Image(STR."/512/\{id}.png");
    }
}
