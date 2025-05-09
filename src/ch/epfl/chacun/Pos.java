package ch.epfl.chacun;

/**
 * Represents a position in the game.
 *
 * @param x The x coordinate of the position.
 * @param y The y coordinate of the position.
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public record Pos(int x, int y) {

    /**
     * The origin of the coordinate system.
     */
    public static final Pos ORIGIN = new Pos(0,0);

    /**
     * Returns the position translated by the given amounts.
     *
     * @param dX the amount to translate the x-coordinate by.
     * @param dY the amount to translate the y-coordinate by.
     * @return the position translated by the given amounts.
     */
    public Pos translated(int dX, int dY) {
        return new Pos(this.x + dX, this.y + dY);
    }

    /**
     * Returns the position of the neighbor in the given direction.
     *
     * @param direction the direction of the neighbor.
     * @return the position of the neighbor in the given direction.
     */
    public Pos neighbor(Direction direction) {
        return switch (direction) {
            case N -> new Pos(x, y - 1);
            case S -> new Pos(x, y + 1);
            case E -> new Pos(x + 1, y);
            case W -> new Pos(x - 1, y);
        };
    }
}
