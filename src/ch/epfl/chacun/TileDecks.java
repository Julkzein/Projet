package ch.epfl.chacun;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents the three piles of tiles.
 *
 * @param startTiles pile containing only the starting tile
 * @param normalTiles pile containing the normal tiles
 * @param menhirTiles pile containing the menhir tiles
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {

    /**
     * Compact constructor with a given startTiles
     */
    public TileDecks {
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }


    /**
     * This method returns the size of the pile of the given type
     *
     * @param kind the type of tile for which we would like the size of the pile
     * @return the size of the pile of the given type of tile
     */
    public int deckSize(Tile.Kind kind) {
        return switch(kind) {
            case START -> startTiles.size();
            case NORMAL -> normalTiles.size();
            case MENHIR -> menhirTiles.size();
        };
    }

    /**
     * This method returns the top tile of the pile of the given type of tile
     *
     * @param kind the type of tile for which we would like the top tile
     * @return the top tile of the pile of the given type of tile
     */
    public Tile topTile(Tile.Kind kind) {
        if (kind == Tile.Kind.START) return startTiles.isEmpty() ? null : startTiles.getFirst();
        else if (kind == Tile.Kind.NORMAL) return normalTiles.isEmpty() ? null : normalTiles.getFirst();
        else return menhirTiles.isEmpty() ? null : menhirTiles.getFirst();
    }

    /**
     * This method returns the tile tileDecks without the top tile of the given type of tile
     * and throws an IllegalArgumentException if the pile is empty
     *
     * @param kind the type of tile for which we would like the pile without the top tile
     * @return The tileDecks without the top tile of the given type of tile
     * @throws IllegalArgumentException if the pile is empty
     */
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        Preconditions.checkArgument(deckSize(kind) != 0);
        return switch (kind) {
            case START -> new TileDecks(startTiles.subList(1, startTiles.size()), normalTiles, menhirTiles);
            case NORMAL -> new TileDecks(startTiles, normalTiles.subList(1, normalTiles.size()), menhirTiles);
            case MENHIR -> new TileDecks(startTiles, normalTiles, menhirTiles.subList(1, menhirTiles.size()));
        };
    }

    /**
     * Returns the tileDecks without the top tile of the given type of tile, until the given predicate is true
     *
     * @param kind the type of tile for which we would like the pile without the top tile
     * @param predicate the condition for which we would like to stop drawing the top tile
     * @return The tileDecks without the top tile of the given type of tile
     * @throws IllegalArgumentException if the pile is empty
     */
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        List<Tile> newTileDeck = switch (kind) {
            case START -> startTiles;
            case NORMAL -> normalTiles;
            case MENHIR -> menhirTiles;
        };
        newTileDeck = newTileDeck.stream().dropWhile(tile -> !predicate.test(tile)).toList();
        return new TileDecks(kind == Tile.Kind.START ? newTileDeck : startTiles,
                             kind == Tile.Kind.NORMAL ? newTileDeck : normalTiles,
                             kind == Tile.Kind.MENHIR ? newTileDeck : menhirTiles);
    }
}