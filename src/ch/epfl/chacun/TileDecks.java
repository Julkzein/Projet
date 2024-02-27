package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents the three piles of tiles.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public final record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {

    /**
     * Compact constructor with a given startTiles
     *
     * @param startTiles pile containing only the starting tile
     * @param normalTiles pile containing the normal tiles
     * @param menhirTiles pile containing the menhir tiles
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
        if (kind == Tile.Kind.START) {
            if (startTiles.isEmpty()) {
                return null;
            }
            return startTiles.get(0);
        } else if (kind == Tile.Kind.NORMAL) {
            if (normalTiles.isEmpty()) {
                return null;
            }
            return normalTiles.get(0);
        } else {
            if (menhirTiles.isEmpty()) {
                return null;
            }
            return menhirTiles.get(0);
        }
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
        if (deckSize(kind) == 0) {
            throw new IllegalArgumentException();
        }
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
        switch (kind) {
            case START -> {
                int predicateTrue = 0;
                for (Tile t : startTiles) {
                    if (predicate.test(t)) {
                        return new TileDecks(startTiles.subList(predicateTrue, startTiles.size()), normalTiles, menhirTiles);
                    } else ++predicateTrue;
                }
            }
            case NORMAL -> {
                int predicateTrue = 0;
                for (Tile t : normalTiles) {
                    if (predicate.test(t)) {
                        return new TileDecks(startTiles, normalTiles.subList(predicateTrue, normalTiles.size()), menhirTiles);
                    } else ++predicateTrue;
                }
            }
            case MENHIR -> {
                int predicateTrue = 0;
                for (Tile t : menhirTiles) {
                    if (predicate.test(t)) {
                        return new TileDecks(startTiles, normalTiles, menhirTiles.subList(predicateTrue, menhirTiles.size()));
                    } else ++predicateTrue;
                }
            }
        }
        return switch (kind) {
            case START -> new TileDecks(new ArrayList<>(), normalTiles, menhirTiles);
            case NORMAL -> new TileDecks(startTiles, new ArrayList<>(), menhirTiles);
            case MENHIR -> new TileDecks(startTiles, normalTiles, new ArrayList<>());
        };
    }
}