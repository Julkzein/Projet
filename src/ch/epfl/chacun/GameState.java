package ch.epfl.chacun;

import java.util.List;
import java.util.Set;

import static ch.epfl.chacun.Occupant.occupantsCount;

/**
 * This record represents the state of the game
 *
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace, Board board, Action NextAction, MessageBoard messageBoard) {
    /**
     * This constructor creates a game state with the given players, tile decks, tile to place, board, next action and message board. It also checks if
     * the number of players is at least 2, if the tile to place is null and the next action is place tile, if the tile decks, board, next action or
     *message board is not null
     *
     * @param players : the list of players
     * @param tileDecks : the tile decks
     * @param tileToPlace : the tile to place
     * @param board : the board
     * @param NextAction : the next action
     * @param messageBoard : the message board
     * @throws IllegalArgumentException if the number of players is less than 2, if the tile to place is not null and the next action is not place tile, if the tile decks, board, next action or message board is null
     */
    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(tileToPlace == null || NextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tileDecks != null && board != null && NextAction != null && messageBoard != null);
        players = List.copyOf(players);
        //reste arguments déjà immutable ???
    }


    /**
     * This method creates the initial game state with the given players, tile decks and text maker
     *
     * @param players : the list of players
     * @param tileDecks : the tile decks
     * @param textMaker : the text maker
     * @return the initial game state
     */
    GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker) {
        return new GameState(players, tileDecks, null, Board.EMPTY, Action.START_GAME, null);
        //what about text Maker ?????
    }

    /**
     * This method returns the player who has the turn or null if there is none
     *
     * @return the player who has the turn
     */
    PlayerColor currentPlayer() {
    if (NextAction == Action.START_GAME || NextAction == Action.END_GAME) {
            return null;
        } else {
            return players.get(0); //pas sur mais je pense que c'est ça
        }
    }

    /**
     * This method returns number of free occupants (that are not currently on a tile) of the given color and type
     *
     * @return the number of free occupants of the given color and type
     */
    int freeOccupants(PlayerColor color, Occupant.Kind kind) {
        //return players.stream().filter(p -> p == color).count() - board.occupants().stream().filter(o -> o.color() == color && o.kind() == kind).count(); //truc du sheitan copilote
        return occupantsCount(kind) - board.occupantCount(color, kind);
    }

    /**
     * This method retursn the set of potential occupants of the last placed tile or throws
     * IllegalArgumentException if the board is empty
     *
     * @throws IllegalArgumentException if the board is empty
     * @return the set of potential occupants of the last placed tile
     */
    Set<Occupant> lasTilePotentialOccupants() {
        if (board == Board.EMPTY) { //suffisant ou index + placed tile check
            throw new IllegalArgumentException();
        }
        return board.lastPlacedTile().potentialOccupants();
    }


    /**
     * This method returns the game state with the starting tile placed
     *
     * @return the game state with the starting tile placed
     */
    GameState withStartingTilePlaced() {
        Preconditions.checkArgument(NextAction == Action.START_GAME);
        Board newBoard = board.withNewTile(new PlacedTile(tileDecks.topTile(Tile.Kind.START), null, Rotation.NONE, new Pos(0, 0)));
        return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, null);
    }

    /**
     * This method returns the game state after the PLACE_TILE action has been taken
     *
     * @param tile : the tile to place
     * @return the game state after the PLACE_TILE action has been taken
     */
    GameState withTilePlaced(Tile tile) {
        Preconditions.checkArgument(NextAction == Action.PLACE_TILE);
        //le sheitan
    }

    /**
     * This method returns the game state after the RETAKE_PAWN action has been taken
     *
     * @param occupant: the occupant to retake
     * @return the game state after the RETAKE_PAWN action has been taken
     */
    GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(NextAction == Action.RETAKE_PAWN);
        Preconditions.checkArgument(occupant.kind() == Occupant.Kind.PAWN || occupant == null);
        if (occupant == null) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.PLACE_TILE, null); //check ou occupy tile
        }
        else {
            return new GameState(players, tileDecks, tileToPlace, board.withOccupant(occupant), Action.OCCUPY_TILE, null);
        }
    }

    /**
     * This method returns the game state after the OCCUPY_TILE action has been taken
     *
     * @param occupant: the occupant to occupy the tile
     * @return the game state after the OCCUPY_TILE action has been taken
     */
    GameState withNewOccupantPlaced(Occupant occupant) {
        Preconditions.checkArgument(NextAction == Action.OCCUPY_TILE);
        if (occupant == null) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.PLACE_TILE, null);
        }
        else {
            Board newBoard = this.board.withNewTile(this.board.lastPlacedTile().withOccupant(occupant));  // pas 100% sur
            if (tileDecks.deckSize(Tile.Kind.NORMAL) == 0) {
                return new GameState(players, tileDecks, tileToPlace, newBoard, Action.END_GAME, null);
            } else {
                return new GameState(players, tileDecks, tileToPlace, newBoard, Action.PLACE_TILE, null);
            }

        }
    }
    /**
     * This nested class represents the possible actions that can be taken in the game
     */
    public enum Action {
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
    }
}
