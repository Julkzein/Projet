package ch.epfl.chacun;


/**
 * Represents an animal in the game.
 */
public record Animal(int id, Kind kind) {
    public enum Kind {
        MAMMOTH,
        AUROCHS,
        DEER,
        TIGER;
    }


    /**
     * Returns the tile id of the animal.
     * @return the tile id of the animal.
     */
    public int tileId() {
        return 3;
        //faire une fois zone finie IOOKKNNLNLNNLNN
    }
}
