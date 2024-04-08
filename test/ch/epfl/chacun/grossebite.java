package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class grossebite {
    /** Three players of different colors */
    private final List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN);
    /** All the possible player colors */
    private final List<PlayerColor> allPlayers = PlayerColor.ALL;
    /** Map of all the tiles by kind */
    private final Map<Tile.Kind, List<Tile>> mapTiles = Tiles.TILES.stream()
            .collect(Collectors.groupingBy(Tile::kind));
    /** Map of all the other tiles by kind */
    private final Map<Tile.Kind, List<Tile>> otherMapTiles = Tiles.TILES.stream()
            .collect(Collectors.groupingBy(Tile::kind));
    /** The tile decks of the game */
    private final TileDecks tileDecks = new TileDecks(mapTiles.get(Tile.Kind.START),
            mapTiles.get(Tile.Kind.NORMAL), mapTiles.get(Tile.Kind.MENHIR));
    /** The other tile decks of the game */
    private final TileDecks otherTileDecks = new TileDecks(otherMapTiles.get(Tile.Kind.START),
            otherMapTiles.get(Tile.Kind.NORMAL), otherMapTiles.get(Tile.Kind.MENHIR));
    /** The text maker of the game */
    private final TextMaker textMaker = new BasicTextMaker();
    /** The initial board of the game */
    private final Board initialBoard = Board.EMPTY;
    private final ch.epfl.chacun.GameState.Action actionSTART = ch.epfl.chacun.GameState.Action.START_GAME;
    /** The initial message board of the game */
    private final MessageBoard messageBoard = new MessageBoard(textMaker, List.of());

    /* ################### Placed Tiles & Board  ################## */

    // ------------------------ CLOSED FOREST WITH MENHIR ------------------------ //
    PlacedTile firstPlacedTile = new PlacedTile(tileDecks.startTiles().getFirst(), null, Rotation.NONE, Pos.ORIGIN);
    PlacedTile placedTile1 = new PlacedTile(tileDecks.normalTiles().getFirst(), PlayerColor.GREEN, Rotation.NONE, new Pos(1, 0));
    PlacedTile placedTile2 = new PlacedTile(tileDecks.normalTiles().get(1), PlayerColor.BLUE, Rotation.HALF_TURN, new Pos(0, 1));
    // ------------------------ CLOSED FOREST WITH MENHIR ------------------------ //

    // ------------------------ CLOSED RIVER SYSTEM  ------------------------ //
    PlacedTile placedTile3 = new PlacedTile(tileDecks.menhirTiles().getFirst(), PlayerColor.RED, Rotation.NONE, new Pos(0, 2));
    PlacedTile placedTile4 = new PlacedTile(tileDecks.normalTiles().get(2), PlayerColor.GREEN, Rotation.LEFT, new Pos(0, 3));
    // ------------------------ CLOSED RIVER SYSTEM  ------------------------ //

    // -------------------- CLOSED FOREST WITHOUT MENHIR -------------------- //
    PlacedTile placedTile5 = new PlacedTile(tileDecks.normalTiles().get(3), PlayerColor.BLUE, Rotation.NONE, new Pos(- 1, 0));
    PlacedTile placedTile6 = new PlacedTile(tileDecks.normalTiles().get(4), PlayerColor.GREEN, Rotation.LEFT, new Pos(1, 1));
    // -------------------- CLOSED FOREST WITHOUT MENHIR -------------------- //
    PlacedTile placedTile7 = new PlacedTile(tileDecks.normalTiles().get(5), PlayerColor.RED, Rotation.NONE, new Pos(1, -1));

    // ----------------------------- BOARD ----------------------------- //
    Board board = initialBoard.withNewTile(firstPlacedTile).withNewTile(placedTile1)
            .withNewTile(placedTile2).withOccupant(new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10))
            .withNewTile(placedTile3)
            .withNewTile(placedTile4).withOccupant(new Occupant(Occupant.Kind.HUT, placedTile4.id() * 10 + 8))
            .withNewTile(placedTile5);
    // ----------------------------- BOARD ----------------------------- //

    /* ################### Placed Tiles & Board  ################## */

    @Test
    void constructorIfCorrectlyDefined() {
        TileDecks newTileDecks = new TileDecks(mapTiles.get(Tile.Kind.START),
                mapTiles.get(Tile.Kind.NORMAL), mapTiles.get(Tile.Kind.MENHIR));

        List<PlayerColor> onePlayer = List.of(PlayerColor.YELLOW);
        assertThrows(IllegalArgumentException.class, () -> ch.epfl.chacun.GameState.initial(onePlayer, newTileDecks, textMaker));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(onePlayer, newTileDecks, newTileDecks.startTiles().getFirst(),
                initialBoard, null, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(onePlayer, newTileDecks, newTileDecks.startTiles().getFirst(),
                initialBoard, actionSTART, null));

        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(onePlayer, newTileDecks, null,
                initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(onePlayer, newTileDecks, null,
                initialBoard, ch.epfl.chacun.GameState.Action.OCCUPY_TILE, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(onePlayer, newTileDecks, null,
                initialBoard, ch.epfl.chacun.GameState.Action.RETAKE_PAWN, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(onePlayer, newTileDecks, null,
                initialBoard, ch.epfl.chacun.GameState.Action.END_GAME, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(players, newTileDecks, null,
                initialBoard, ch.epfl.chacun.GameState.Action.PLACE_TILE, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(players, newTileDecks, newTileDecks.startTiles().getFirst(),
                initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(players, newTileDecks, newTileDecks.normalTiles().getFirst(),
                initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(players, newTileDecks, newTileDecks.menhirTiles().getFirst(),
                initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(players, null, newTileDecks.startTiles().getFirst(),
                initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(List.of(), newTileDecks, newTileDecks.startTiles().getFirst(),
                initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(List.of(PlayerColor.RED), newTileDecks, newTileDecks.startTiles().getFirst(),
                initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard));
        assertThrows(IllegalArgumentException.class, () -> new ch.epfl.chacun.GameState(players, newTileDecks, null,
                initialBoard, ch.epfl.chacun.GameState.Action.PLACE_TILE, messageBoard));

        assertDoesNotThrow(() -> new ch.epfl.chacun.GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), newTileDecks, null,
                initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard));
        assertDoesNotThrow(() -> new ch.epfl.chacun.GameState(players, newTileDecks, newTileDecks.startTiles().getFirst(),
                initialBoard, ch.epfl.chacun.GameState.Action.PLACE_TILE, messageBoard));
        assertDoesNotThrow(() -> new ch.epfl.chacun.GameState(players, newTileDecks, newTileDecks.normalTiles().getFirst(),
                initialBoard, ch.epfl.chacun.GameState.Action.PLACE_TILE, messageBoard));
    }

    @Test
    void initialIsCorrectlyDefined() {
        TileDecks newTileDecks = new TileDecks(mapTiles.get(Tile.Kind.START),
                mapTiles.get(Tile.Kind.NORMAL), mapTiles.get(Tile.Kind.MENHIR));
        ch.epfl.chacun.GameState initial = ch.epfl.chacun.GameState.initial(players, newTileDecks, textMaker);
        ch.epfl.chacun.GameState expected = new ch.epfl.chacun.GameState(players, newTileDecks, null,
                initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard);
        assertEquals(expected, initial);
    }

    @Test
    void currentPlayer() {
        TileDecks newTileDecks = new TileDecks(mapTiles.get(Tile.Kind.START),
                mapTiles.get(Tile.Kind.NORMAL), mapTiles.get(Tile.Kind.MENHIR));
        List<PlayerColor> twoPlayers = List.of(PlayerColor.YELLOW, PlayerColor.RED);
        List<PlayerColor> threePlayers = List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE);
        List<PlayerColor> fourPlayers = List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN);
        List<List<PlayerColor>> biListPlayer = List.of(twoPlayers, threePlayers, fourPlayers, players);

        for (int i = 0 ; i < biListPlayer.size() ; i++) {
            ch.epfl.chacun.GameState gameState = new ch.epfl.chacun.GameState(biListPlayer.get(i), newTileDecks, null,
                    initialBoard, ch.epfl.chacun.GameState.Action.OCCUPY_TILE, messageBoard);
            assertEquals(biListPlayer.get(i).getFirst(), gameState.currentPlayer());

            gameState = new ch.epfl.chacun.GameState(biListPlayer.get(i), newTileDecks, null,
                    initialBoard, ch.epfl.chacun.GameState.Action.RETAKE_PAWN, messageBoard);
            assertEquals(biListPlayer.get(i).getFirst(), gameState.currentPlayer());

            gameState = new ch.epfl.chacun.GameState(biListPlayer.get(i), newTileDecks, null,
                    initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard);
            assertNull(gameState.currentPlayer());

            gameState = new ch.epfl.chacun.GameState(biListPlayer.get(i), newTileDecks, null,
                    initialBoard, ch.epfl.chacun.GameState.Action.END_GAME, messageBoard);
            assertNull(gameState.currentPlayer());

            gameState = new ch.epfl.chacun.GameState(biListPlayer.get(i), newTileDecks, newTileDecks.normalTiles().getFirst(),
                    initialBoard, ch.epfl.chacun.GameState.Action.PLACE_TILE, messageBoard);
            assertEquals(biListPlayer.get(i).getFirst(), gameState.currentPlayer());

            gameState = new ch.epfl.chacun.GameState(biListPlayer.get(i), newTileDecks, newTileDecks.menhirTiles().getFirst(),
                    initialBoard, ch.epfl.chacun.GameState.Action.PLACE_TILE, messageBoard);
            assertEquals(biListPlayer.get(i).getFirst(), gameState.currentPlayer());
        }
    }

    @Test
    void freeOccupantsCount() {
        TileDecks newTileDecks = new TileDecks(mapTiles.get(Tile.Kind.START),
                mapTiles.get(Tile.Kind.NORMAL), mapTiles.get(Tile.Kind.MENHIR));
        ch.epfl.chacun.GameState gameState = new ch.epfl.chacun.GameState(players, newTileDecks, null, initialBoard, ch.epfl.chacun.GameState.Action.START_GAME, messageBoard);
        for (PlayerColor player : allPlayers) {
            assertEquals(Occupant.occupantsCount(Occupant.Kind.PAWN), gameState.freeOccupantsCount(player, Occupant.Kind.PAWN));
            assertEquals(Occupant.occupantsCount(Occupant.Kind.HUT), gameState.freeOccupantsCount(player, Occupant.Kind.HUT));
        }

        PlacedTile placedTile1 = new PlacedTile(newTileDecks.startTiles().getFirst(), null, Rotation.NONE, Pos.ORIGIN);
        PlacedTile placedTile2 = new PlacedTile(newTileDecks.normalTiles().getFirst(), PlayerColor.RED, Rotation.RIGHT, new Pos(- 1, 0));
        PlacedTile placedTile3 = new PlacedTile(newTileDecks.normalTiles().get(1), PlayerColor.BLUE, Rotation.RIGHT, new Pos(1, 0));

        Board newBoard = initialBoard.withNewTile(placedTile1);
        gameState = new ch.epfl.chacun.GameState(players, newTileDecks, placedTile2.tile(), newBoard, ch.epfl.chacun.GameState.Action.PLACE_TILE, messageBoard);
        for (PlayerColor player : allPlayers) {
            assertEquals(Occupant.occupantsCount(Occupant.Kind.PAWN), gameState.freeOccupantsCount(player, Occupant.Kind.PAWN));
            assertEquals(Occupant.occupantsCount(Occupant.Kind.HUT), gameState.freeOccupantsCount(player, Occupant.Kind.HUT));
        }

        newBoard = newBoard.withNewTile(placedTile2).withOccupant(new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10 + 1));
        gameState = new ch.epfl.chacun.GameState(players, newTileDecks, placedTile3.tile(), newBoard, ch.epfl.chacun.GameState.Action.PLACE_TILE, messageBoard);
        for (PlayerColor player : allPlayers) {
            if (player == PlayerColor.RED)
                assertEquals(Occupant.occupantsCount(Occupant.Kind.PAWN) - 1, gameState.freeOccupantsCount(player, Occupant.Kind.PAWN));
            else
                assertEquals(Occupant.occupantsCount(Occupant.Kind.PAWN), gameState.freeOccupantsCount(player, Occupant.Kind.PAWN));
            assertEquals(Occupant.occupantsCount(Occupant.Kind.HUT), gameState.freeOccupantsCount(player, Occupant.Kind.HUT));
        }

        newBoard = newBoard.withNewTile(placedTile3).withOccupant(new Occupant(Occupant.Kind.PAWN, placedTile3.id() * 10 + 1));
        gameState = new ch.epfl.chacun.GameState(players, newTileDecks, newTileDecks.normalTiles().getFirst(), newBoard, ch.epfl.chacun.GameState.Action.PLACE_TILE, messageBoard);
        for (PlayerColor player : allPlayers) {
            if (player == PlayerColor.RED || player == PlayerColor.BLUE)
                assertEquals(Occupant.occupantsCount(Occupant.Kind.PAWN) - 1, gameState.freeOccupantsCount(player, Occupant.Kind.PAWN));
            else
                assertEquals(Occupant.occupantsCount(Occupant.Kind.PAWN), gameState.freeOccupantsCount(player, Occupant.Kind.PAWN));
            assertEquals(Occupant.occupantsCount(Occupant.Kind.HUT), gameState.freeOccupantsCount(player, Occupant.Kind.HUT));
        }
    }

    @Test
    void lastTilePotentialOccupants() {
        TileDecks newTileDecks = new TileDecks(mapTiles.get(Tile.Kind.START),
                mapTiles.get(Tile.Kind.NORMAL), mapTiles.get(Tile.Kind.MENHIR));
        GameState gameState = new GameState(players, newTileDecks, null, initialBoard, GameState.Action.START_GAME, messageBoard);
        assertThrows(IllegalArgumentException.class, gameState::lastTilePotentialOccupants);

        gameState = gameState.withStartingTilePlaced();
        assertDoesNotThrow(gameState::lastTilePotentialOccupants);
        assertEquals(Set.of(), gameState.lastTilePotentialOccupants());

        PlacedTile placedTile2 = new PlacedTile(newTileDecks.normalTiles().getFirst(), PlayerColor.RED, Rotation.RIGHT, new Pos(- 1, 0));
        PlacedTile placedTile3 = new PlacedTile(newTileDecks.normalTiles().get(1), PlayerColor.BLUE, Rotation.RIGHT, new Pos(1, 0));

        // placedTile2 is a normal tile
        assertEquals(GameState.Action.PLACE_TILE, gameState.nextAction());
        assertEquals(placedTile2.tile(), gameState.tileToPlace());

        gameState = gameState.withPlacedTile(placedTile2);

        assertEquals(Set.of(
                new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10),
                new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10 + 1),
                new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10 + 2),
                new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10 + 3),
                new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10 + 4),
                new Occupant(Occupant.Kind.HUT, placedTile2.id() * 10 + 8)
        ), gameState.lastTilePotentialOccupants());

        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10 + 8));

        assertEquals(Set.of(), gameState.lastTilePotentialOccupants());

        // placedTile3 is a normal tile
        assertEquals(GameState.Action.PLACE_TILE, gameState.nextAction());
        assertEquals(placedTile3.tile(), gameState.tileToPlace());

        gameState = gameState.withPlacedTile(placedTile3);
        assertEquals(Set.of(
                new Occupant(Occupant.Kind.PAWN, placedTile3.id() * 10),
                new Occupant(Occupant.Kind.PAWN, placedTile3.id() * 10 + 1),
                new Occupant(Occupant.Kind.PAWN, placedTile3.id() * 10 + 2),
                new Occupant(Occupant.Kind.PAWN, placedTile3.id() * 10 + 3),
                new Occupant(Occupant.Kind.PAWN, placedTile3.id() * 10 + 4),
                new Occupant(Occupant.Kind.PAWN, placedTile3.id() * 10 + 5),
                new Occupant(Occupant.Kind.HUT, placedTile3.id() * 10 + 8)
        ), gameState.lastTilePotentialOccupants());

        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, placedTile3.id() * 10));
        assertEquals(Set.of(), gameState.lastTilePotentialOccupants());
        assertEquals(GameState.Action.PLACE_TILE, gameState.nextAction());
    }

    @Test
    void withStartingTilePlaced() {
        TileDecks newTileDecks = new TileDecks(mapTiles.get(Tile.Kind.START),
                mapTiles.get(Tile.Kind.NORMAL), mapTiles.get(Tile.Kind.MENHIR));
        GameState gameState = new GameState(players, newTileDecks, null, initialBoard, GameState.Action.START_GAME, messageBoard);
        PlacedTile placedTile = new PlacedTile(newTileDecks.startTiles().getFirst(), null, Rotation.NONE, Pos.ORIGIN);
        PlacedTile normalTile = new PlacedTile(newTileDecks.normalTiles().getFirst(), null, Rotation.NONE, Pos.ORIGIN);
        TileDecks newOtherDecks = newTileDecks.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL);
        GameState expected = new GameState(players, newOtherDecks, normalTile.tile(),
                initialBoard.withNewTile(placedTile), GameState.Action.PLACE_TILE, messageBoard);

        GameState newGameState = gameState.withStartingTilePlaced();

        assertEquals(expected.tileDecks().normalTiles().getFirst(), newGameState.tileDecks().normalTiles().getFirst());

        assertEquals(expected.tileDecks().startTiles().size(), newGameState.tileDecks().startTiles().size());
        assertEquals(expected.tileDecks().normalTiles().size(), newGameState.tileDecks().normalTiles().size());
        assertEquals(expected.tileDecks().menhirTiles().size(), newGameState.tileDecks().menhirTiles().size());

        assertEquals(expected, newGameState);
        assertEquals(placedTile, newGameState.board().lastPlacedTile());
        assertEquals(expected.tileToPlace(), newGameState.tileToPlace());
        assertEquals(newGameState.players().getFirst(), newGameState.currentPlayer());
        assertEquals(Set.of(), newGameState.lastTilePotentialOccupants());
        assertEquals(Occupant.occupantsCount(Occupant.Kind.PAWN), newGameState.freeOccupantsCount(newGameState.players().getFirst(), Occupant.Kind.PAWN));
        assertEquals(Occupant.occupantsCount(Occupant.Kind.HUT), newGameState.freeOccupantsCount(newGameState.players().getFirst(), Occupant.Kind.HUT));
        assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, newGameState.nextAction());

        for (ch.epfl.chacun.GameState.Action action : List.of(ch.epfl.chacun.GameState.Action.START_GAME, ch.epfl.chacun.GameState.Action.OCCUPY_TILE, ch.epfl.chacun.GameState.Action.RETAKE_PAWN, ch.epfl.chacun.GameState.Action.END_GAME)) {
            gameState = new ch.epfl.chacun.GameState(players, newTileDecks, null, initialBoard, action, messageBoard);
            if (action == ch.epfl.chacun.GameState.Action.START_GAME) {
                assertDoesNotThrow(gameState::withStartingTilePlaced);
                assertThrows(IllegalArgumentException.class, newGameState::withStartingTilePlaced);
            }
            else assertThrows(IllegalArgumentException.class, gameState::withStartingTilePlaced);
        }
    }

    @Test
    void withPlacedTile() {
    }

    @Test
    void withOccupantRemoved() {
        Board board = initialBoard.withNewTile(firstPlacedTile).withNewTile(placedTile1)
                .withNewTile(placedTile2).withOccupant(new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10))
                .withNewTile(placedTile3).withOccupant(new Occupant(Occupant.Kind.PAWN, placedTile3.id() * 10 + 1))
                .withNewTile(placedTile4).withOccupant(new Occupant(Occupant.Kind.HUT, placedTile4.id() * 10 + 8))
                .withNewTile(placedTile5).withOccupant(new Occupant(Occupant.Kind.HUT, placedTile5.id() * 10 + 8))
                .withNewTile(placedTile6).withOccupant(new Occupant(Occupant.Kind.HUT, placedTile6.id() * 10 + 8))
                .withNewTile(placedTile7).withOccupant(new Occupant(Occupant.Kind.PAWN, placedTile7.id() * 10 + 2));
        ch.epfl.chacun.GameState gameState = new ch.epfl.chacun.GameState(players, tileDecks, null, board, ch.epfl.chacun.GameState.Action.RETAKE_PAWN, messageBoard);

        List<PlacedTile> placedTiles = List.of(placedTile1, placedTile2, placedTile3, placedTile4, placedTile5, placedTile6, placedTile7);

        for (PlacedTile placedTile : placedTiles) {
            Occupant occupant = placedTile.occupant();
            if (occupant == null) continue;
            ch.epfl.chacun.GameState newGameState = gameState.withOccupantRemoved(occupant);
            assertEquals(Occupant.occupantsCount(occupant.kind()) - 1, newGameState.freeOccupantsCount(placedTile.placer(), occupant.kind()));
            assertFalse(newGameState.board().occupants().contains(occupant));
        }
    }

    @Test
    void withNewOccupantThrowsCorrectly() {
        TileDecks newTileDecks = new TileDecks(mapTiles.get(Tile.Kind.START),
                mapTiles.get(Tile.Kind.NORMAL), mapTiles.get(Tile.Kind.MENHIR));
        for (ch.epfl.chacun.GameState.Action action : List.of(ch.epfl.chacun.GameState.Action.START_GAME, ch.epfl.chacun.GameState.Action.PLACE_TILE, ch.epfl.chacun.GameState.Action.OCCUPY_TILE, ch.epfl.chacun.GameState.Action.RETAKE_PAWN, ch.epfl.chacun.GameState.Action.END_GAME)) {
            ch.epfl.chacun.GameState gameState = new ch.epfl.chacun.GameState(List.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED), newTileDecks,
                    action == ch.epfl.chacun.GameState.Action.PLACE_TILE ? placedTile6.tile() : null, board, action, messageBoard);
            if (action == ch.epfl.chacun.GameState.Action.OCCUPY_TILE) {
                assertDoesNotThrow(() -> gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0)));
            } else assertThrows(IllegalArgumentException.class, () -> gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0)));
        }
    }

    @Test
    void withNewOccupantCorrectlyDefined() {
        TileDecks newTileDecks = new TileDecks(mapTiles.get(Tile.Kind.START),
                mapTiles.get(Tile.Kind.NORMAL), mapTiles.get(Tile.Kind.MENHIR));
        ch.epfl.chacun.GameState gameState = ch.epfl.chacun.GameState.initial(players, newTileDecks, textMaker);
        gameState = gameState.withStartingTilePlaced();

        assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState.nextAction());
        assertEquals(PlayerColor.RED, gameState.currentPlayer());
        assertEquals(placedTile1.tile(), gameState.tileToPlace());
        gameState = gameState.withPlacedTile(placedTile1);

        assertEquals(ch.epfl.chacun.GameState.Action.OCCUPY_TILE, gameState.nextAction());
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));

        assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState.nextAction());
        assertEquals(placedTile2.tile(), gameState.tileToPlace());
        gameState = gameState.withPlacedTile(placedTile2);
        assertEquals(ch.epfl.chacun.GameState.Action.OCCUPY_TILE, gameState.nextAction());

        for (Occupant occupant : gameState.lastTilePotentialOccupants()) {
            ch.epfl.chacun.GameState gameState1 = gameState.withNewOccupant(occupant);
            assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState1.nextAction());
            assertEquals(placedTile3.tile(), gameState1.tileToPlace());
        }
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, placedTile2.id() * 10 + 1));
        gameState = gameState.withPlacedTile(placedTile3);
        assertEquals(ch.epfl.chacun.GameState.Action.OCCUPY_TILE, gameState.nextAction());


        for (Occupant occupant : gameState.lastTilePotentialOccupants()) {
            ch.epfl.chacun.GameState gameState1 = gameState.withNewOccupant(occupant);
            assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState1.nextAction());
            assertEquals(placedTile4.tile(), gameState1.tileToPlace());
        }
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.HUT, placedTile3.id() * 10 + 8));
        assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState.nextAction());
        assertEquals(placedTile4.tile(), gameState.tileToPlace());

        gameState = gameState.withPlacedTile(placedTile4);
        assertEquals(ch.epfl.chacun.GameState.Action.OCCUPY_TILE, gameState.nextAction());

        for (Occupant occupant : gameState.lastTilePotentialOccupants()) {
            ch.epfl.chacun.GameState gameState1 = gameState.withNewOccupant(occupant);
            assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState1.nextAction());
            assertEquals(placedTile5.tile(), gameState1.tileToPlace());
        }
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.HUT, placedTile4.id() * 10 + 8));
        assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState.nextAction());
        assertEquals(placedTile5.tile(), gameState.tileToPlace());

        gameState = gameState.withPlacedTile(placedTile5);
        assertEquals(ch.epfl.chacun.GameState.Action.OCCUPY_TILE, gameState.nextAction());

        for (Occupant occupant : gameState.lastTilePotentialOccupants()) {
            ch.epfl.chacun.GameState gameState1 = gameState.withNewOccupant(occupant);
            assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState1.nextAction());
            assertEquals(placedTile6.tile(), gameState1.tileToPlace());
        }
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.HUT, placedTile5.id() * 10 + 8));
        assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState.nextAction());
        assertEquals(placedTile6.tile(), gameState.tileToPlace());

        gameState = gameState.withPlacedTile(placedTile6);
        assertEquals(ch.epfl.chacun.GameState.Action.OCCUPY_TILE, gameState.nextAction());

        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, placedTile6.id() * 10 + 2));
        assertEquals(ch.epfl.chacun.GameState.Action.PLACE_TILE, gameState.nextAction());
        assertEquals(placedTile7.tile(), gameState.tileToPlace());

        gameState = gameState.withPlacedTile(placedTile7);
        assertEquals(ch.epfl.chacun.GameState.Action.OCCUPY_TILE, gameState.nextAction());
    }
}