package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.epfl.chacun.Area.hasMenhir;
import static ch.epfl.chacun.Occupant.occupantsCount;

/**
 * This record represents the state of the game
 *
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace, Board board, Action nextAction, MessageBoard messageBoard) {
    /**
     * This constructor creates a game state with the given players, tile decks, tile to place, board, next action and message board. It also checks if
     * the number of players is at least 2, if the tile to place is null and the next action is place tile, if the tile decks, board, next action or
     *message board is not null
     *
     * @param players : the list of players
     * @param tileDecks : the tile decks
     * @param tileToPlace : the tile to place
     * @param board : the board
     * @param nextAction : the next action
     * @param messageBoard : the message board
     * @throws IllegalArgumentException if the number of players is less than 2, if the tile to place is not null and the next action is not place tile, if the tile decks, board, next action or message board is null
     */
    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(tileToPlace == null || nextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tileDecks != null && board != null && nextAction != null && messageBoard != null);
        players = List.copyOf(players);
    }


    /**
     * This method creates the initial game state with the given players, tile decks and text maker
     *
     * @param players : the list of players
     * @param tileDecks : the tile decks
     * @param textMaker : the text maker
     * @return the initial game state
     */
    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker) {
        return new GameState(players, tileDecks, null, Board.EMPTY, Action.START_GAME, new MessageBoard(textMaker, List.of()));
    }


    /**
     * This method ends the current game
     *
     * @return the same GameState as before but with the action Action.END_GAME
     */
    private GameState end() {
        return new GameState(players, tileDecks,tileToPlace, board, Action.END_GAME, messageBoard);
    }
    /**
     * This method returns the player who has the turn or null if there is none
     *
     * @return the player who has the turn
     */
    public PlayerColor currentPlayer() {
    if (nextAction == Action.START_GAME || nextAction == Action.END_GAME) {
            return null;
        } else {
            return players.get(0);
        }
    }

    /**
     * This method returns number of free occupants (that are not currently on a tile) of the given color and type
     *
     * @return the number of free occupants of the given color and type
     */
    public int freeOccupants(PlayerColor color, Occupant.Kind kind) {
        return occupantsCount(kind) - board.occupantCount(color, kind);
    }

    /**
     * This method retursn the set of potential occupants of the last placed tile or throws
     * IllegalArgumentException if the board is empty
     *
     * @throws IllegalArgumentException if the board is empty
     * @return the set of potential occupants of the last placed tile
     */
    public Set<Occupant> lasTilePotentialOccupants() {
        Preconditions.checkArgument(board != Board.EMPTY); //suffisant ?
        return board.lastPlacedTile().potentialOccupants();
    }


    /**
     * This method returns the game state with the starting tile placed
     *
     * @return the game state with the starting tile placed
     */
    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction == Action.START_GAME);
        Board newBoard = board.withNewTile(new PlacedTile(tileDecks.topTile(Tile.Kind.START), null, Rotation.NONE, new Pos(0, 0)));
        return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL).withTopTileDrawn(Tile.Kind.START), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, messageBoard);
    }

    /**
     * This method returns the game state after the PLACE_TILE action has been taken
     *
     * @param tile : the tile to place
     * @return the game state after the PLACE_TILE action has been taken
     */
    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tile.occupant() == null);

        Board newBoard = board.withNewTile(tile);
        switch (tile.kind()) {
            case NORMAL:
                if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.SHAMAN) {
                    return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.RETAKE_PAWN, messageBoard);
                } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.LOGBOAT) {
                    MessageBoard newMessageBoard = messageBoard.withScoredLogboat(players.get(0), board.riverSystemArea((Zone.Water) tile.specialPowerZone())); // typecast forcé???
                    return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, newMessageBoard).withTurnFinished();
                } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.HUNTING_TRAP) {
                    MessageBoard newMessageBoard = messageBoard.withScoredHuntingTrap(players.get(0), board.adjacentMeadow(tile.pos(), (Zone.Meadow) tile.specialPowerZone())); // typecast forcé???
                    return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, newMessageBoard).withTurnFinished();
                } else {
                    return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, messageBoard).withTurnFinished();
                }

            case MENHIR:
                if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.SHAMAN) {
                    return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.MENHIR), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.RETAKE_PAWN, messageBoard);
                } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.LOGBOAT) {
                    MessageBoard newMessageBoard = messageBoard.withScoredLogboat(players.get(0), board.riverSystemArea((Zone.Water) tile.specialPowerZone())); // typecast forcé???
                    return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.MENHIR), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, newMessageBoard).withTurnFinished();
                } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.HUNTING_TRAP) {
                    MessageBoard newMessageBoard = messageBoard.withScoredHuntingTrap(players.get(0), board.adjacentMeadow(tile.pos(), (Zone.Meadow) tile.specialPowerZone())); // typecast forcé???
                    return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.MENHIR), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, newMessageBoard).withTurnFinished();
                } else {
                    return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.MENHIR), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, messageBoard).withTurnFinished();
                }

            default:
                throw new IllegalStateException();
        }
    }

    /**
     * This method returns the game state after the RETAKE_PAWN action has been taken
     *
     * @param occupant: the occupant to retake
     * @return the game state after the RETAKE_PAWN action has been taken
     */
    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.RETAKE_PAWN);
        Preconditions.checkArgument(occupant.kind() == Occupant.Kind.PAWN || occupant == null);
        if (occupant == null) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.PLACE_TILE, messageBoard).withTurnFinished(); //check ou occupy tile
        } else {
            return new GameState(players, tileDecks, tileToPlace, board.withOccupant(occupant), Action.OCCUPY_TILE, messageBoard);
        }
    }

    /**
     * This method returns the game state after the OCCUPY_TILE action has been taken
     *
     * @param occupant: the occupant to occupy the tile
     * @return the game state after the OCCUPY_TILE action has been taken
     */
    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);
        if (occupant == null) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.PLACE_TILE, messageBoard).withTurnFinished();
        }
        else {
            Board newBoard = board.withOccupant(occupant); //parait trop simple
            return new GameState(players, tileDecks, tileToPlace, newBoard, Action.PLACE_TILE, messageBoard).withTurnFinished();
        }
    }


    private GameState withFinalPointsCounted() {
        Set<Animal> deers = new HashSet<>();
        Set<Animal> tigers = new HashSet<>();
        for (Area<Zone.Meadow> meadowArea : board.meadowAreas()) {
            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE) == null) {
                for (Animal animal : Area.animals(meadowArea, Set.of())) { //cancelled animals set of suffit ?
                    if (animal.kind() == Animal.Kind.DEER) deers.add(animal);
                    if (animal.kind() == Animal.Kind.TIGER) tigers.add(animal);
                }
                if (tigers.size() >= deers.size()) {
                    board.cancelledAnimals().addAll(deers);
                } else {
                    int eatenDeers = 0;
                    for (Animal deer : deers) {
                        if (eatenDeers < tigers.size()) {
                            board.cancelledAnimals().add(deer);
                            eatenDeers++;
                        } else break;
                    }
                }
            } else {
                for (Animal animal : Area.animals(meadowArea, Set.of())) {
                    if (animal.kind() == Animal.Kind.TIGER) board.cancelledAnimals().add(animal);
                }
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

    private List<PlayerColor> nextPlayerList() {
       List<PlayerColor> list = players.subList(1, players.size());
       list.add(players.get(0));
       return list;
    }

    private GameState withTurnFinished() {
        PlacedTile lastPlacedTile = board.lastPlacedTile();
        List<PlayerColor> newPlayers = players;
        Action newAction = Action.PLACE_TILE;
        TileDecks newTileDecks = tileDecks;
        MessageBoard newMessageBoard = messageBoard;

        boolean canPlayAgain = false;
        Preconditions.checkArgument(lastPlacedTile != null); //vérfifier si on a le droit de faire ça

        for (Area<Zone.Forest> forest : board.forestsClosedByLastTile()) {
            newMessageBoard = newMessageBoard.withScoredForest(forest); //est ce que ça suffit ??
            if (hasMenhir(forest) && lastPlacedTile.kind() == Tile.Kind.NORMAL) {
                canPlayAgain = true;
            }
        }

        for (Area<Zone.River> river : board.riversClosedByLastTile()) {
            newMessageBoard = newMessageBoard.withScoredRiver(river);
        }

        if (canPlayAgain) {
            newTileDecks = newTileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, board::couldPlaceTile);
        } else {
            newTileDecks = newTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, board::couldPlaceTile);
        }

        if (!canPlayAgain) {
            newPlayers = nextPlayerList();
        }

        if (tileDecks.normalTiles().isEmpty()) {
            newAction = Action.END_GAME;
        }

        return new GameState(
                newPlayers,
                newTileDecks,
                canPlayAgain ? newTileDecks.topTile(Tile.Kind.MENHIR) : newTileDecks.topTile(Tile.Kind.NORMAL),
                board, //meme board ??
                newAction,
                newMessageBoard);
    }
}
