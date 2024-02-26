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
    //final redundant ?

    /**
     * Compact constructor with a given startTiles
     * @param startTiles pile containing only the starting tile
     * @param normalTiles pile containing the normal tiles
     * @param menhirTiles pile containing the menhir tiles
     */
    public TileDecks {
        if (startTiles == null || normalTiles == null || menhirTiles == null) {
            throw new IllegalArgumentException();
        } //nécessaire?
        startTiles = List.copyOf(startTiles); //bien ça ?
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }


    /**
     *
     * @param kind the type of tile for which we would like the size of the pile
     * @return the size of the pile of the given type of tile
     */
    public int deckSize(Tile.Kind kind) {
        if (kind == Tile.Kind.START) {
            return startTiles.size();
        } else if (kind == Tile.Kind.NORMAL) {
            return normalTiles.size();
        } else {
            return menhirTiles.size();
        }
    }

    /**
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
     *
     * @param kind the type of tile for which we would like the pile without the top tile
     * @return The tileDecks without the top tile of the given type of tile
     * @throws IllegalArgumentException if the pile is empty
     */
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        if (deckSize(kind) == 0) {
            throw new IllegalArgumentException();
        }
        if (kind == Tile.Kind.START) {
            return new TileDecks(startTiles.subList(1, startTiles.size()), normalTiles, menhirTiles);
        } else if (kind == Tile.Kind.NORMAL) {
            return new TileDecks(startTiles, normalTiles.subList(1, normalTiles.size()), menhirTiles);
        } else {
            return new TileDecks(startTiles, normalTiles, menhirTiles.subList(1, menhirTiles.size()));
        }
    }

    /**
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        if (kind == Tile.Kind.START) {
            int firstPlayableTile = 0;
            while(predicate.test(startTiles.get(firstPlayableTile))) { //verifier la condition, et créer si nécéssaire une classe
                firstPlayableTile++;
            }
            return new TileDecks(startTiles.subList(firstPlayableTile, startTiles.size()), normalTiles, menhirTiles);
        } else if (kind == Tile.Kind.NORMAL) {
            int firstPlayableTile = 0;
            while(predicate.test(startTiles.get(firstPlayableTile))) {
                firstPlayableTile++;
            }
            return new TileDecks(startTiles, normalTiles.subList(firstPlayableTile, normalTiles.size()), menhirTiles);
        } else {
            int firstPlayableTile = 0;
            while(predicate.test(startTiles.get(firstPlayableTile))) {
                firstPlayableTile++;
            }
            return new TileDecks(startTiles, normalTiles, menhirTiles.subList(firstPlayableTile, menhirTiles.size()));
        }
    }
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
        if (kind == Tile.Kind.START) {
            return new TileDecks(new ArrayList<>(), normalTiles, menhirTiles);
        } else if (kind == Tile.Kind.NORMAL) {
            return new TileDecks(startTiles, new ArrayList<>(), menhirTiles);
        } else {
            return new TileDecks(startTiles, normalTiles, new ArrayList<>());
        }
    }
}