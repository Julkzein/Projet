package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    public int freeOccupantsCount(PlayerColor color, Occupant.Kind kind) {
        return occupantsCount(kind) - board.occupantCount(color, kind);
    }

    /**
     * This method returns the set of potential occupants of the last placed tile or throws
     * IllegalArgumentException if the board is empty
     *
     * @throws IllegalArgumentException if the board is empty
     * @return the set of potential occupants of the last placed tile
     */
    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(board != Board.EMPTY);
        PlacedTile lastPlacedTile = board.lastPlacedTile();
        Preconditions.checkArgument(lastPlacedTile != null);
        Set<Occupant> potentialOccupantsSet = new HashSet<>();

        for (Occupant occupant : lastPlacedTile.potentialOccupants()) {
            switch (lastPlacedTile.zoneWithId(occupant.zoneId())) {
                case Zone.Meadow meadow:
                    if (!board.meadowArea(meadow).isOccupied() && freeOccupantsCount(currentPlayer(), occupant.kind()) > 0) {
                        potentialOccupantsSet.add(occupant);
                    }
                    break;
                case Zone.Forest forest:
                    if (!board.forestArea(forest).isOccupied() && freeOccupantsCount(currentPlayer(), occupant.kind()) > 0) {
                        potentialOccupantsSet.add(occupant);
                    }
                    break;
                case Zone.River river:
                    if (!board.riverArea(river).isOccupied() && freeOccupantsCount(currentPlayer(), occupant.kind()) > 0) {
                        potentialOccupantsSet.add(occupant);
                    }
                    break;
                case Zone.Water water:
                    if (!board.riverSystemArea(water).isOccupied() && freeOccupantsCount(currentPlayer(), occupant.kind()) > 0) {
                        potentialOccupantsSet.add(occupant);
                    }
                    break;
                default:
            }
        }
        return potentialOccupantsSet;
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
        MessageBoard newMessageBoard = messageBoard;

        switch (tile.kind()) {
            case NORMAL:
                if (tile.specialPowerZone() != null) {
                    if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.SHAMAN) {
                        return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.RETAKE_PAWN, messageBoard);
                    } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.LOGBOAT) {
                        newMessageBoard = messageBoard.withScoredLogboat(players.get(0), board.riverSystemArea((Zone.Water) tile.specialPowerZone())); // typecast forcé???
                        return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.OCCUPY_TILE, newMessageBoard).withTurnFinishedIfOccupationImpossible();
                    } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.HUNTING_TRAP) {
                        newMessageBoard = messageBoard.withScoredHuntingTrap(players.get(0), board.adjacentMeadow(tile.pos(), (Zone.Meadow) tile.specialPowerZone())); // typecast forcé???
                        return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.OCCUPY_TILE, newMessageBoard).withTurnFinishedIfOccupationImpossible();
                    }
                }
                return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.OCCUPY_TILE, messageBoard).withTurnFinishedIfOccupationImpossible();


            case MENHIR:
                if (tile.specialPowerZone() != null) {
                    if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.SHAMAN) {
                        return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.MENHIR), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.RETAKE_PAWN, messageBoard);
                    } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.LOGBOAT) {
                        newMessageBoard = messageBoard.withScoredLogboat(players.get(0), board.riverSystemArea((Zone.Water) tile.specialPowerZone())); // typecast forcé???
                        return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.MENHIR), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.OCCUPY_TILE, newMessageBoard).withTurnFinishedIfOccupationImpossible();
                    } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.HUNTING_TRAP) {
                        newMessageBoard = messageBoard.withScoredHuntingTrap(players.get(0), board.adjacentMeadow(tile.pos(), (Zone.Meadow) tile.specialPowerZone())); // typecast forcé???
                        return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.MENHIR), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.OCCUPY_TILE, newMessageBoard).withTurnFinishedIfOccupationImpossible();
                    }
                }
                return new GameState(players, tileDecks.withTopTileDrawn(Tile.Kind.MENHIR), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.OCCUPY_TILE, messageBoard).withTurnFinishedIfOccupationImpossible();

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
        Preconditions.checkArgument(occupant == null || occupant.kind() == Occupant.Kind.PAWN);

        if (occupant == null) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.PLACE_TILE, messageBoard).withTurnFinished(); //check ou occupy tile
        } else {
            return new GameState(players, tileDecks, tileToPlace, board.withOccupant(occupant), Action.OCCUPY_TILE, messageBoard).withTurnFinishedIfOccupationImpossible();
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
        } else {
            Board newBoard = board.withOccupant(occupant); //parait trop simple
            return new GameState(players, tileDecks, tileToPlace, newBoard, Action.PLACE_TILE, messageBoard).withTurnFinished();
        }
    }

    private GameState withTurnFinishedIfOccupationImpossible() {
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);
        for (Zone zone : board.lastPlacedTile().tile().zones()) {
            if (zone instanceof Zone.Water water && !lastTilePotentialOccupants().isEmpty() && (freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN) > 0 || freeOccupantsCount(currentPlayer(), Occupant.Kind.HUT) > 0 )) {
                //gamestate place ok
            }
            if (!lastTilePotentialOccupants().isEmpty() && freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN) > 0) {
                //gamestate place ok
            }
        }
        //gamestate place pas ok
        return null;
    }

    /**
     * This method returns the game state after the turn has been finished
     * It checks forests and rivers closed by the last placed tile and updates the message board
     * It updates the tile decks and the players list
     * It also checks if the player can play again and if the tile decks are empty, in what case it calls the withFinalPointsCounted method
     *
     * @return the game state after the turn has been finished
     */
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

        GameState nextGameState = new GameState(
                newPlayers,
                newTileDecks,
                canPlayAgain ? newTileDecks.topTile(Tile.Kind.MENHIR) : newTileDecks.topTile(Tile.Kind.NORMAL),
                board, //meme board ??
                newAction,
                newMessageBoard);

        return (newAction == Action.END_GAME) ? nextGameState.withFinalPointsCounted() : nextGameState;
    }

    /**
     * This method returns the game state after the game has ended
     * It adds to the cancelled animals set the deers that have been eaten by the tigers, and the tigers
     * It counts the final points and determines the winners
     *
     * @return the game state after the game has ended
     */
    private GameState withFinalPointsCounted() {
        Set<Animal> deers = new HashSet<>();
        Set<Animal> tigers = new HashSet<>();
        Set<Animal> cancelledAnimals = new HashSet<>();
        MessageBoard newMessageBoard = messageBoard;

        for (Area<Zone.Meadow> meadowArea : board.meadowAreas()) {
            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE) == null) {
                for (Animal animal : Area.animals(meadowArea, Set.of())) { //cancelled animals set of suffit ?
                    if (animal.kind() == Animal.Kind.DEER) deers.add(animal);
                    if (animal.kind() == Animal.Kind.TIGER) tigers.add(animal);
                }
                if (tigers.size() >= deers.size()) {
                    cancelledAnimals.addAll(deers);
                } else {
                    int eatenDeers = 0;
                    Set<Zone.Meadow> meadows = new HashSet<>();
                    if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP) != null) 
                        meadows = meadowZonesNotAdjacentInSameArea((Zone.Meadow) meadowArea.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP), meadowArea);
                    if (!meadows.isEmpty()) {
                        for (Zone.Meadow meadow : meadows) {
                            for (Animal deer : meadow.animals()) {
                                if (eatenDeers < tigers.size()) {
                                    cancelledAnimals.add(deer);
                                    deers.remove(deer);
                                    eatenDeers++;
                                } else break;
                            }
                            meadows.remove(meadow);
                        }
                    } else {
                        for (Animal deer : deers) {
                            if (eatenDeers < tigers.size()) {
                                cancelledAnimals.add(deer);
                                deers.remove(deer);
                                eatenDeers++;
                            } else break;
                        }
                    }
                }
            } else {
                for (Animal animal : Area.animals(meadowArea, Set.of())) {
                    if (animal.kind() == Animal.Kind.TIGER) cancelledAnimals.add(animal);
                }
            }
        }

        Board newBoard = board.withMoreCancelledAnimals(cancelledAnimals);
        for (Area<Zone.Meadow> meadowArea : newBoard.meadowAreas()) {
            newMessageBoard.withScoredMeadow(meadowArea, cancelledAnimals);
        }
        for (Area<Zone.Water> waterArea : board.riverSystemAreas()) {
            newMessageBoard.withScoredRiverSystem(waterArea);
        }
        int maxPoints = 0 ;
        Set<PlayerColor> winners = new HashSet<>();
        for (Integer i : messageBoard.points().values()) {
            if (i > maxPoints) maxPoints = i;
        }
        for (Map.Entry<PlayerColor, Integer> entry : messageBoard.points().entrySet()) {
            if (entry.getValue() == maxPoints) winners.add(entry.getKey());
        }
        newMessageBoard.withWinners(winners, maxPoints);

        return new GameState(players, tileDecks, null, newBoard, Action.END_GAME, newMessageBoard);
    }

    /**
     * This method returns the set of meadow zones of the given area that are not adjacent to the given meadow zone
     *
     * @param z : the meadow zone
     * @param m : the area
     * @return the set of meadow zones of the given area that are not adjacent to the given meadow zone
     */
    private Set<Zone.Meadow> meadowZonesNotAdjacentInSameArea(Zone.Meadow z, Area<Zone.Meadow> m){
        Set<Zone.Meadow> meadows = new HashSet<>();
        for (Zone.Meadow zone : m.zones()) {
            if (!(board.adjacentMeadow(board.tileWithId(z.tileId()).pos(), z).zones().contains(zone))) {
                meadows.add(zone);
            }
        }
        return meadows;
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

    /**
     * This method returns the list of players with the next player at the beginning
     *
     * @return the list of players with the next player at the beginning
     */
    private List<PlayerColor> nextPlayerList() {
       List<PlayerColor> list = players.subList(1, players.size());
       list.add(players.get(0));
       return list;
    }
}
