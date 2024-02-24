package ch.epfl.chacun;

import java.util.List;
import java.util.function.Predicate;

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
            return startTiles.get(0);
        } else if (kind == Tile.Kind.NORMAL) {
            return normalTiles.get(0);
        } else {
            return menhirTiles.get(0);
        }
    }

    /**
     *
     * @param kind the type of tile for which we would like the pile without the top tile
     * @return The tileDecks without the top tile of the given type of tile 
     */
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        if (deckSize(kind) == 0) {
            throw new IllegalStateException();
        }
        if (kind == Tile.Kind.START) {
            return new TileDecks(startTiles.subList(1, startTiles.size()), normalTiles, menhirTiles);
        } else if (kind == Tile.Kind.NORMAL) {
            return new TileDecks(startTiles, normalTiles.subList(1, normalTiles.size()), menhirTiles);
        } else {
            return new TileDecks(startTiles, normalTiles, menhirTiles.subList(1, menhirTiles.size()));
        }
    }

    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        //louis à l'aide
    }

}
