package ch.epfl.chacun;

/**
 * Represents an animal in the game.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public record Animal(int id, Kind kind) {

    /**
     * Represents the four possible kinds of animals.
     */
    public enum Kind {
        MAMMOTH,
        AUROCHS,
        DEER,
        TIGER
    }


    /**
     * Returns the tile id of the animal.
     * @return the tile id of the animal.
     */
    public int tileId() {
        return Zone.tileId(id) / 10;
    }
}
