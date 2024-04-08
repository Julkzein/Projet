package ch.epfl.chacun;

import java.util.*;

import static ch.epfl.chacun.Area.hasMenhir;
import static ch.epfl.chacun.Occupant.occupantsCount;

/**
 * This record represents the state of the game
 *
 * @param players the players of the current game
 * @param tileDecks the three decks of tiles that will be drawn during the game 
 * @param tileToPlace the next tile that will be placed
 * @param board the board of the current game
 * @param nextAction the next action to be taken
 * @param messageBoard the message board associated to the current game  
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace, Board board, Action nextAction, MessageBoard messageBoard) {
    /**
     * This constructor creates a game state with the given players, tile decks, tile to place, board, next action and message board. It also checks if
     * the number of players is at least 2, if the tile to place is null and the next action is place tile, if the tile decks, board, next action or
     *message board is not null
     * @throws IllegalArgumentException if the number of players is less than 2, if the tile to place is not null and the next action is not place tile, if the tile decks, board, next action or message board is null
     */
    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(tileToPlace == null ^ nextAction == Action.PLACE_TILE);
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
            return players.getFirst();
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

        if (lastPlacedTile.occupant() == null) {
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
        TileDecks tileDecks1 = tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, newBoard::couldPlaceTile);
        return new GameState(players, tileDecks1.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, messageBoard);
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

        if (tile.specialPowerZone() != null) {
            switch (tile.specialPowerZone().specialPower()) {
                case SHAMAN:
                    if (board.occupantCount(tile.placer(), Occupant.Kind.PAWN) != 0) {
                        return new GameState(players, tileDecks, null, newBoard, Action.RETAKE_PAWN, messageBoard);
                    }
                case LOGBOAT:
                    newMessageBoard = messageBoard.withScoredLogboat(players.getFirst(), board.riverSystemArea((Zone.Water) tile.specialPowerZone()));
                case HUNTING_TRAP:
                    newMessageBoard = messageBoard.withScoredHuntingTrap(players.getFirst(), board.adjacentMeadow(tile.pos(), (Zone.Meadow) tile.specialPowerZone()));
                default:
            }
        }

        return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, newMessageBoard).withTurnFinishedIfOccupationImpossible();

        /**
        switch (tile.kind()) {
            case NORMAL:
                return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, messageBoard).withTurnFinishedIfOccupationImpossible();

            case MENHIR:
                if (tile.specialPowerZone() != null) {
                    if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.SHAMAN) {
                        if (board.occupantCount(tile.placer(), Occupant.Kind.PAWN) != 0) {
                            return new GameState(players, tileDecks, null, newBoard, Action.RETAKE_PAWN, messageBoard);
                        } else {
                            return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, messageBoard).withTurnFinishedIfOccupationImpossible();
                        }
                    } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.LOGBOAT) {
                        newMessageBoard = messageBoard.withScoredLogboat(players.getFirst(), board.riverSystemArea((Zone.Water) tile.specialPowerZone()));
                        return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, newMessageBoard).withTurnFinishedIfOccupationImpossible();
                    } else if (tile.specialPowerZone().specialPower() == Zone.SpecialPower.HUNTING_TRAP) {
                        newMessageBoard = messageBoard.withScoredHuntingTrap(players.getFirst(), board.adjacentMeadow(tile.pos(), (Zone.Meadow) tile.specialPowerZone()));
                        return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, newMessageBoard).withTurnFinishedIfOccupationImpossible();
                    }
                }
                return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, messageBoard).withTurnFinishedIfOccupationImpossible();

            default:
                throw new IllegalArgumentException();
        }
         */
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
            return new GameState(players, tileDecks, null, board, Action.OCCUPY_TILE, messageBoard).withTurnFinishedIfOccupationImpossible();
        } else {
            return new GameState(players, tileDecks, null, board.withoutOccupant(occupant), Action.OCCUPY_TILE, messageBoard).withTurnFinishedIfOccupationImpossible();
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
            return withTurnFinished(board, messageBoard);
                    //new GameState(players, tileDecks, null, board, nextAction, messageBoard).withTurnFinished();
        } else {
            Board newBoard = board.withOccupant(occupant);
            return withTurnFinished(newBoard, messageBoard);
        }

    }

    private GameState withTurnFinishedIfOccupationImpossible() {
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);
        Preconditions.checkArgument(board.lastPlacedTile() != null);
        boolean occupationPossible = false;

        for (Zone zone : board.lastPlacedTile().tile().zones()) {
            if (Objects.requireNonNull(zone) instanceof Zone.Water) {
                if (!lastTilePotentialOccupants().isEmpty() &&
                    ((freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN) > 0 && setHasOccupantOfKind(lastTilePotentialOccupants(), Occupant.Kind.PAWN)) ||
                     (freeOccupantsCount(currentPlayer(), Occupant.Kind.HUT) > 0 && setHasOccupantOfKind(lastTilePotentialOccupants(), Occupant.Kind.HUT)))) {
                    occupationPossible = true;
                }
            } else {
                if (!lastTilePotentialOccupants().isEmpty() && freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN) > 0 && setHasOccupantOfKind(lastTilePotentialOccupants(), Occupant.Kind.PAWN)) {
                    occupationPossible = true;
                }
            }
        }

        if (occupationPossible) { return new GameState(players, tileDecks, null, board, Action.OCCUPY_TILE, messageBoard); }
        else { return withTurnFinished(board, messageBoard); }
    }

    /**
     * This method returns the game state after the turn has been finished
     * It checks forests and rivers closed by the last placed tile and updates the message board
     * It updates the tile decks and the players list
     * It also checks if the player can play again and if the tile decks are empty, in what case it calls the withFinalPointsCounted method
     *
     * @return the game state after the turn has been finished
     */
    private GameState withTurnFinished(Board board, MessageBoard messageBoard) {
        TileDecks newTileDecks =
                tileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, board::couldPlaceTile)
                        .withTopTileDrawnUntil(Tile.Kind.NORMAL, board::couldPlaceTile);

        PlacedTile lastPlacedTile = board.lastPlacedTile();
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard;
        List<PlayerColor> newPlayers = new ArrayList<>(players);
        boolean canPlayAgain = false;
        Preconditions.checkArgument(lastPlacedTile != null);

        for (Area<Zone.Forest> forest : board.forestsClosedByLastTile()) {
            if (hasMenhir(forest)) {
                newMessageBoard = newMessageBoard.withClosedForestWithMenhir(currentPlayer(), forest);
                if (lastPlacedTile.kind() == Tile.Kind.NORMAL) {
                    canPlayAgain = true;
                }
            } else {
                newMessageBoard = newMessageBoard.withScoredForest(forest);
            }
        }

        for (Area<Zone.River> river : board.riversClosedByLastTile()) {
            newMessageBoard = newMessageBoard.withScoredRiver(river);
        }

        System.out.println("forests closed : " + board.forestsClosedByLastTile());
        System.out.println("rivers closed : " + board.riversClosedByLastTile());
        newBoard = board.withoutGatherersOrFishersIn(board.forestsClosedByLastTile(), board.riversClosedByLastTile());

        if (canPlayAgain) {
            //newTileDecks = newTileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, board::couldPlaceTile);
        } else {
            //newTileDecks = newTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, board::couldPlaceTile);
            newPlayers = nextPlayerList();
        }

        if (tileDecks.normalTiles().isEmpty()) {
            return new GameState(newPlayers, newTileDecks, null, newBoard, Action.END_GAME, newMessageBoard).withFinalPointsCounted();
        } else {
            return new GameState(
                    newPlayers,
                    canPlayAgain ? newTileDecks.withTopTileDrawn(Tile.Kind.MENHIR) : newTileDecks.withTopTileDrawn(Tile.Kind.NORMAL),
                    canPlayAgain ? newTileDecks.topTile(Tile.Kind.MENHIR) : newTileDecks.topTile(Tile.Kind.NORMAL),
                    newBoard,
                    Action.PLACE_TILE,
                    newMessageBoard);
        }
    }

    /**
     * This method returns the game state after the game has ended
     * It adds to the cancelled animals set the deers that have been eaten by the tigers, and the tigers
     * It counts the final points and determines the winners
     *
     * @return the game state after the game has ended
     */
    private GameState withFinalPointsCounted() {
        System.out.println("final points counted");
        Set<Animal> deers = new HashSet<>();
        Set<Animal> tigers = new HashSet<>();
        Set<Animal> cancelledAnimals = new HashSet<>();
        MessageBoard newMessageBoard = messageBoard;

        Board newBoard = board.withMoreCancelledAnimals(cancelledAnimals);

        for (Area<Zone.Meadow> meadowArea : newBoard.meadowAreas()) {
            newMessageBoard = newMessageBoard.withScoredMeadow(meadowArea, cancelledAnimals);
            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP) != null) {
                newMessageBoard = newMessageBoard.withScoredPitTrap(meadowArea, cancelledAnimals);
             }
        }
        for (Area<Zone.Water> waterArea : board.riverSystemAreas()) {
            newMessageBoard = newMessageBoard.withScoredRiverSystem(waterArea);
            if (waterArea.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null) {
                newMessageBoard = newMessageBoard.withScoredRaft(waterArea);
            }
        }
        int maxPoints = 0 ;
        Set<PlayerColor> winners = new HashSet<>();
        for (Integer i : messageBoard.points().values()) {
            if (i > maxPoints) maxPoints = i;
        }
        for (Map.Entry<PlayerColor, Integer> entry : messageBoard.points().entrySet()) {
            if (entry.getValue() == maxPoints) winners.add(entry.getKey());
        }
        newMessageBoard = newMessageBoard.withWinners(winners, maxPoints);

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
        List<PlayerColor> sublist = players.subList(1, players.size());
        List<PlayerColor> list = new ArrayList<>(sublist);
        list.add(players.getFirst());
        return list;
    }

    /**
     * This method returns true if the given set of occupants contains an occupant of the given kind
     *
     * @param s : the set of occupants
     * @param k : the kind of occupant
     * @return true if the given set of occupants contains an occupant of the given kind
     */
    private boolean setHasOccupantOfKind(Set<Occupant> s, Occupant.Kind k) {
        for (Occupant o : s) {
            if (o.kind() == k)  {
                return true;
            }
        }
        return false;
    }

    private Set<Animal> cancelledAnimals()
}
