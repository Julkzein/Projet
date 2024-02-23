package ch.epfl.chacun;

public record PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant) {
    public PlacedTile {
        if (tile == null || rotation == null) {
            throw new IllegalArgumentException();
        }
    }

    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }
}
