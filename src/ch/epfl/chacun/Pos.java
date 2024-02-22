package ch.epfl.chacun;

/**
 * Represents a position in the game.
 */
public record Pos(int x, int y) {

    /**
     * The origin of the coordinate system.
     */
    final static public Pos ORIGIN = new Pos(0,0);

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
            case N -> new Pos(this.x, this.y - 1);
            case S -> new Pos(this.x, this.y + 1);
            case E -> new Pos(this.x + 1, this.y);
            case W -> new Pos(this.x - 1, this.y);
        };
    }
}
