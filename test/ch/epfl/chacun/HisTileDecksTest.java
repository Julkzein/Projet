package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for the TileDecks class
 * @author Adam Bekkar (379476)
 */
class HisTileDecksTest {
    private final TileSide[] sides = new TileSide[] {
            new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS)),
            new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR)),
            new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)),
            new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP)),
            new TileSide.Meadow(new Zone.Meadow(4, new ArrayList<>(), Zone.SpecialPower.RAFT)),


            new TileSide.River(new Zone.Meadow(5, new ArrayList<>(), Zone.SpecialPower.SHAMAN),
                    new Zone.River(6, 123, new Zone.Lake(8, 28, Zone.SpecialPower.LOGBOAT)),
                    new Zone.Meadow(7, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP)),
    };
    private List<Tile> myStartTiles = List.of(
            new Tile(56, Tile.Kind.START, sides[0], sides[1], sides[2], sides[3])
    );
    private List<Tile> myNormalTiles = new ArrayList<>(
            Arrays.asList(
                    new Tile(10, Tile.Kind.NORMAL, sides[4], sides[5], sides[3], sides[2]),
                    new Tile(11, Tile.Kind.NORMAL, sides[1], sides[0], sides[3], sides[4])
            ));
    private List<Tile> myMenhirTiles = new ArrayList<>(
            Arrays.asList(
                    new Tile(80, Tile.Kind.MENHIR, sides[5], sides[4], sides[1], sides[0]),
                    new Tile(8, Tile.Kind.MENHIR,  sides[0], sides[1], sides[3], sides[2]),
                    new Tile(82, Tile.Kind.MENHIR, sides[2], sides[3], sides[4], sides[5])
            ));
    private final TileDecks myTileDecks = new TileDecks(myStartTiles, myNormalTiles, myMenhirTiles);
    @Test
    void deckSizeIsCorrectlyDefined() {
        assertEquals(1, myTileDecks.startTiles().size());
        assertEquals(2, myTileDecks.normalTiles().size());
        assertEquals(3, myTileDecks.menhirTiles().size());
    }
    @Test
    void topTileIsCorrectlyDefined() {
        assertEquals(56, myTileDecks.topTile(Tile.Kind.START).id());
        assertEquals(10, myTileDecks.topTile(Tile.Kind.NORMAL).id());
        assertEquals(80, myTileDecks.topTile(Tile.Kind.MENHIR).id());
    }
    @Test
    void withTopTileDrawnIsCorrectlyDefined() {
        // Remove the first element of the list and compare the sizes
        assertEquals(0, myTileDecks.withTopTileDrawn(Tile.Kind.START).deckSize(Tile.Kind.START));
        assertEquals(1, myTileDecks.withTopTileDrawn(Tile.Kind.NORMAL).deckSize(Tile.Kind.NORMAL));
        assertEquals(2, myTileDecks.withTopTileDrawn(Tile.Kind.MENHIR).deckSize(Tile.Kind.MENHIR));

        assertNotEquals(myTileDecks, myTileDecks.withTopTileDrawn(Tile.Kind.NORMAL));
        assertNotEquals(myTileDecks, myTileDecks.withTopTileDrawn(Tile.Kind.MENHIR));

        // Remove the first element of the list and compare the lists
        TileDecks myTileDecksWithStartTopTileDrawn = myTileDecks.withTopTileDrawn(Tile.Kind.START);
        TileDecks myTileDecksWithNormalTopTileDrawn = myTileDecks.withTopTileDrawn(Tile.Kind.NORMAL);
        TileDecks myTileDecksWithMenhirTopTileDrawn = myTileDecks.withTopTileDrawn(Tile.Kind.MENHIR);

        myStartTiles = myStartTiles.subList(1, myStartTiles.size());
        myNormalTiles = myNormalTiles.subList(1, myNormalTiles.size());
        myMenhirTiles = myMenhirTiles.subList(1, myMenhirTiles.size());

        assertEquals(myStartTiles, myTileDecksWithStartTopTileDrawn.startTiles());
        assertEquals(myNormalTiles, myTileDecksWithNormalTopTileDrawn.normalTiles());
        assertEquals(myMenhirTiles, myTileDecksWithMenhirTopTileDrawn.menhirTiles());
    }

    @Test
    void withTopTileDrawnUntilIsCorrectlyDefined() {
        // Remove the first element of the list and compare the sizes
        assertEquals(1, myTileDecks.withTopTileDrawnUntil(Tile.Kind.START, tile -> tile.id() == 56).deckSize(Tile.Kind.START));
        assertEquals(2, myTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, tile -> tile.id() == 10).deckSize(Tile.Kind.NORMAL));
        assertEquals(2, myTileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, tile -> tile.id() == 8).deckSize(Tile.Kind.MENHIR));

        assertEquals(myTileDecks, myTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, tile -> tile.id() == 10));
        assertNotEquals(myTileDecks, myTileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, tile -> tile.id() == 8));

        // Remove the first element of the list and compare the lists
        TileDecks myTileDecksWithStartTopTileDrawn = myTileDecks.withTopTileDrawnUntil(Tile.Kind.START, tile -> tile.id() == 56);
        TileDecks myTileDecksWithNormalTopTileDrawn = myTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, tile -> tile.id() == 10);
        TileDecks myTileDecksWithMenhirTopTileDrawn = myTileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, tile -> tile.id() == 80);

        myMenhirTiles = myMenhirTiles.subList(1, myMenhirTiles.size());

        assertEquals(myTileDecks, myTileDecks.withTopTileDrawnUntil(Tile.Kind.START, tile -> tile.id() == 56));
        assertEquals(myTileDecks, myTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, tile -> tile.id() == 10));
        assertEquals(myTileDecks, myTileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, tile -> tile.id() == 80));
    }
}