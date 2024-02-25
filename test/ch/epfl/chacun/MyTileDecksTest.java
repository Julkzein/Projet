package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyTileDecksTest {

    public Tile td(int id, Tile.Kind kind) {
        return new Tile(id, kind,
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)));

    }

    @Test
    void checkThrowExceptions() {
        assertThrows(IllegalArgumentException.class, ()-> new TileDecks(null, new ArrayList<>(), new ArrayList<>()));
        assertThrows(IllegalArgumentException.class, ()-> new TileDecks(new ArrayList<>(), null, new ArrayList<>()));
        assertThrows(IllegalArgumentException.class, ()-> new TileDecks(new ArrayList<>(), new ArrayList<>(), null));

    }

    @Test
    void checkDeckSizeEmpty() {
        TileDecks tileDecks = new TileDecks(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assert(tileDecks.deckSize(Tile.Kind.START) == 0);
        assert(tileDecks.deckSize(Tile.Kind.NORMAL) == 0);
        assert(tileDecks.deckSize(Tile.Kind.MENHIR) == 0);
    }

    @Test
    void checkSizeOneTypeOneElement() {
        List<Tile> startTiles = new ArrayList<>();
        startTiles.add(td(78, Tile.Kind.START));
        TileDecks tileDecks = new TileDecks(startTiles, new ArrayList<>(), new ArrayList<>());
        assert(tileDecks.deckSize(Tile.Kind.START) == 1);
        assert(tileDecks.deckSize(Tile.Kind.NORMAL) == 0);
        assert(tileDecks.deckSize(Tile.Kind.MENHIR) == 0);
    }

    @Test
    void chekSizeOneElementOfEach() {
        List<Tile> startTiles = new ArrayList<>();
        startTiles.add(td(78, Tile.Kind.START));
        List<Tile> normalTiles = new ArrayList<>();
        normalTiles.add(td(78, Tile.Kind.NORMAL));
        List<Tile> menhirTiles = new ArrayList<>();
        menhirTiles.add(td(78, Tile.Kind.MENHIR));
        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);
        assert(tileDecks.deckSize(Tile.Kind.START) == 1);
        assert(tileDecks.deckSize(Tile.Kind.NORMAL) == 1);
        assert(tileDecks.deckSize(Tile.Kind.MENHIR) == 1);
    }

    @Test
    void checkTopTileEmpty() {
        TileDecks tileDecks = new TileDecks(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assert(tileDecks.topTile(Tile.Kind.START) == null);
        assert(tileDecks.topTile(Tile.Kind.NORMAL) == null);
        assert(tileDecks.topTile(Tile.Kind.MENHIR) == null);
    }

    @Test
    void checkTopTileOfEach() {
        List<Tile> startTiles = new ArrayList<>();
        startTiles.add(td(78, Tile.Kind.START));
        startTiles.add(td(90, Tile.Kind.START));
        List<Tile> normalTiles = new ArrayList<>();
        normalTiles.add(td(78, Tile.Kind.NORMAL));
        normalTiles.add(td(90, Tile.Kind.NORMAL));
        List<Tile> menhirTiles = new ArrayList<>();
        menhirTiles.add(td(78, Tile.Kind.MENHIR));
        menhirTiles.add(td(90, Tile.Kind.MENHIR));
        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);
        assertEquals(td(78, Tile.Kind.START), tileDecks.topTile(Tile.Kind.START));
        assertEquals(td(78, Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL));
        assertEquals(td(78, Tile.Kind.MENHIR), tileDecks.topTile(Tile.Kind.MENHIR));
    }

    @Test
    void withTopTileDrawnEmpty() {
        TileDecks tileDecks = new TileDecks(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertThrows(IllegalArgumentException.class, ()-> tileDecks.withTopTileDrawn(Tile.Kind.START));
        assertThrows(IllegalArgumentException.class, ()-> tileDecks.withTopTileDrawn(Tile.Kind.NORMAL));
        assertThrows(IllegalArgumentException.class, ()-> tileDecks.withTopTileDrawn(Tile.Kind.MENHIR));
    }

    @Test
    void withTopTileDrawnOneElementOfEach() {
        List<Tile> startTiles = new ArrayList<>();
        startTiles.add(td(78, Tile.Kind.START));
        startTiles.add(td(90, Tile.Kind.START));
        List<Tile> normalTiles = new ArrayList<>();
        normalTiles.add(td(78, Tile.Kind.NORMAL));
        normalTiles.add(td(90, Tile.Kind.NORMAL));
        List<Tile> menhirTiles = new ArrayList<>();
        menhirTiles.add(td(78, Tile.Kind.MENHIR));
        menhirTiles.add(td(90, Tile.Kind.MENHIR));
        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);
        assertEquals(new TileDecks(List.of(td(90, Tile.Kind.START)), normalTiles, menhirTiles), tileDecks.withTopTileDrawn(Tile.Kind.START));
        assertEquals(new TileDecks(startTiles, List.of(td(90, Tile.Kind.NORMAL)), menhirTiles), tileDecks.withTopTileDrawn(Tile.Kind.NORMAL));
        assertEquals(new TileDecks(startTiles, normalTiles, List.of(td(90, Tile.Kind.MENHIR))), tileDecks.withTopTileDrawn(Tile.Kind.MENHIR));

    }
}
