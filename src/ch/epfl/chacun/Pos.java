package ch.epfl.chacun;

public record Pos(int x, int y) {
    final static public Pos origin = new Pos(0,0);

    public Pos translated(int dX, int dY) {
        return new Pos(this.x + dX, this.y + dY);
    }

    public Pos neighbor(Direction direction) {
        return switch (direction) {
            case N -> new Pos(this.x, this.y + 1);
            case S -> new Pos(this.x, this.y - 1);
            case E -> new Pos(this.x - 1, this.y);
            case W -> new Pos(this.x + 1, this.y);
        };
    }
}
