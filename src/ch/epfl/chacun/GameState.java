package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

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
     * This constructor creates a game state with the given players, tile decks, tile to place, board, next action and
     * message board. It also checks if the number of players is at least 2, if the tile to place is null or (exclusive)
     * the next action is place tile, and if the tile decks, board, next action or message board are not null.
     *
     * @throws IllegalArgumentException if one of the conditions above is not met.
     */
    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(tileToPlace == null ^ nextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tileDecks != null && board != null && nextAction != null && messageBoard != null);
        players = List.copyOf(players);
    }


    /**
     * This method creates the initial game state with the given players, tile decks and text maker and instantiates the
     * message board with an empty list of messages.
     *
     * @param players : the list of players
     * @param tileDecks : the tile decks
     * @param textMaker : the text maker
     * @return the initial game state.
     */
    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker) {
        return new GameState(players, tileDecks, null, Board.EMPTY, Action.START_GAME, new MessageBoard(textMaker, List.of()));
    }

    /**
     *This method returns the player who has the turn or null if there is none.
     *
     * @return the player who has the turn.
     */
    public PlayerColor currentPlayer() {
        if (nextAction == Action.START_GAME || nextAction == Action.END_GAME) {
            return null;
        } else {
            return players.getFirst();
        }
    }

    /**
     * This method returns number of free occupants (that are not currently on a tile) of the given color and type.
     *
     * @param color : the color of the occupants
     * @param kind : the kind of the occupants
     * @return the number of free occupants of the given color and type.
     */
    public int freeOccupantsCount(PlayerColor color, Occupant.Kind kind) {
        return occupantsCount(kind) - board.occupantCount(color, kind);
    }

    /**
     * This method returns the set of potential occupants of the last placed tile or throws IllegalArgumentException
     * if the board is empty or the last placed tile is null. It loops over each zone of the last placed tile to
     * determine the potential occupants.
     *
     * @throws IllegalArgumentException if the board is empty or the last placed tile is null.
     * @return the set of potential occupants of the last placed tile.
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
     * This method returns the game state with the starting tile placed and the tile decks updated.
     *
     * @throws IllegalArgumentException if the next action is not start game.
     * @return the new game state.
     */
    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction == Action.START_GAME);
        Board newBoard = board.withNewTile(new PlacedTile(tileDecks.topTile(Tile.Kind.START), null, Rotation.NONE, new Pos(0, 0)));
        TileDecks tileDecks1 = tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, newBoard::couldPlaceTile);
        return new GameState(players, tileDecks1.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL), tileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, messageBoard);
    }

    /**
     * This method returns the game state after the PLACE_TILE action has been taken. If the tile has a special power, a
     * new GameState is returned with the corresponding action or a message is added to the message board.
     *
     * @param tile : the tile to place
     * @throws IllegalArgumentException if the next action is not place tile or the tile has an occupant.
     * @return the game state after the PLACE_TILE action has been taken.
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
                    break;
                case LOGBOAT:
                    newMessageBoard = messageBoard.withScoredLogboat(currentPlayer(), newBoard.riverSystemArea((Zone.Water) tile.specialPowerZone()));
                    break;
                case HUNTING_TRAP:
                    newMessageBoard = messageBoard.withScoredHuntingTrap(currentPlayer(), newBoard.adjacentMeadow(tile.pos(), (Zone.Meadow) tile.specialPowerZone()));
                    break;
                default:
            }
        }

        return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, newMessageBoard).withTurnFinishedIfOccupationImpossible();
    }

    /**
     * This method returns the game state with the pawn retaken and the next action set to OCCUPY_TILE.
     *
     * @param occupant: the occupant to retake
     * @throws IllegalArgumentException if the next action is not retake pawn or if the occupant is not null and not a pawn
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
     * This method returns the game state with the tile occupied by the given occupant.
     *
     * @param occupant: the occupant to occupy the tile
     * @throws IllegalArgumentException if the next action is not occupy tile.
     * @return the game state after the OCCUPY_TILE action has been taken.
     */
    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);
        if (occupant == null) {
            return withTurnFinished(board, messageBoard);
        } else {
            Board newBoard = board.withOccupant(occupant);
            return withTurnFinished(newBoard, messageBoard);
        }
    }

    /**
     * This method returns the game state with the next action set to occupy tile if the player has the possibility to
     * do so. Otherwise, it returns the game state with the turn finished.
     *
     * @throws IllegalArgumentException if the next action is not occupy tile or if the last placed tile is null.
     * @return the game state after the turn has been finished if the occupation is impossible.
     */
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
                tileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, board :: couldPlaceTile)
                        .withTopTileDrawnUntil(Tile.Kind.NORMAL, board :: couldPlaceTile);

        PlacedTile lastPlacedTile = board.lastPlacedTile();
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard;
        List<PlayerColor> newPlayers = new ArrayList<>(players);
        boolean canPlayAgain = false;
        Preconditions.checkArgument(lastPlacedTile != null);

        for (Area<Zone.Forest> forest : board.forestsClosedByLastTile()) {
            newMessageBoard = newMessageBoard.withScoredForest(forest);
            if (hasMenhir(forest)) {
                newMessageBoard = newMessageBoard.withClosedForestWithMenhir(currentPlayer(), forest);
                if (lastPlacedTile.kind() == Tile.Kind.NORMAL) {
                    canPlayAgain = true;
                }
            }
        }

        for (Area<Zone.River> river : board.riversClosedByLastTile()) {
            newMessageBoard = newMessageBoard.withScoredRiver(river);
        }


        newBoard = board.withoutGatherersOrFishersIn(board.forestsClosedByLastTile(), board.riversClosedByLastTile());

        if (!canPlayAgain) {
            newPlayers = nextPlayerList();
        }

        if (tileDecks.normalTiles().isEmpty() && (!canPlayAgain || tileDecks.menhirTiles().isEmpty())) {
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
     * It adds to the cancelled animals set the deer that have been eaten by the tigers, and the tigers
     * It counts the final points and determines the winners
     *
     * @return the game state after the game has ended
     */
    private GameState withFinalPointsCounted() {
        MessageBoard newMessageBoard = messageBoard;
        Set<Animal> cancelledAnimals = cancelledAnimals(board.meadowAreas());
        Board newBoard = board.withMoreCancelledAnimals(cancelledAnimals);

        for (Area<Zone.Meadow> meadowArea : newBoard.meadowAreas()) {
            newMessageBoard = newMessageBoard.withScoredMeadow(meadowArea, cancelledAnimals);
            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP) != null) {
                Zone.Meadow meadow = (Zone.Meadow) meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP);
                newMessageBoard = newMessageBoard.withScoredPitTrap(newBoard.adjacentMeadow(newBoard.tileWithId(meadow.tileId()).pos(), meadow), cancelledAnimals);
            }
        }
        for (Area<Zone.Water> waterArea : newBoard.riverSystemAreas()) {
            newMessageBoard = newMessageBoard.withScoredRiverSystem(waterArea);
            if (waterArea.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null) {
                newMessageBoard = newMessageBoard.withScoredRaft(waterArea);
            }
        }
        int maxPoints = 0 ;
        Set<PlayerColor> winners = new HashSet<>();
        for (Integer i : newMessageBoard.points().values()) {
            if (i > maxPoints) maxPoints = i;
        }
        for (Map.Entry<PlayerColor, Integer> entry : newMessageBoard.points().entrySet()) {
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


    private Set<Animal> cancelledAnimals(Set<Area<Zone.Meadow>> meadowAreas) {
        Set<Animal> cancelledAnimals = new HashSet<>();

        for (Area<Zone.Meadow> meadowArea : meadowAreas) {
            Set<Animal> deer = new HashSet<>();
            Set<Animal> tigers = new HashSet<>();

            for (Animal animal : Area.animals(meadowArea, Set.of())) {
                if (animal.kind() == Animal.Kind.DEER) deer.add(animal);
                if (animal.kind() == Animal.Kind.TIGER) tigers.add(animal);
            }
            cancelledAnimals.addAll(tigers);

            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE) == null) { //If there isn't any fire, we deal with the deer to cancel
                if (deer.size() <= tigers.size()) {
                    cancelledAnimals.addAll(deer);
                } else {
                    Set<Zone.Meadow> priorityMeadows = new HashSet<>();

                    if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP) != null) {
                        priorityMeadows = meadowZonesNotAdjacentInSameArea((Zone.Meadow) meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP), meadowArea);
                    }

                    //Use of the flat map to convert each individual element of the stream into a stream itself, and avoiding nested streams into a stream
                    //Filters the animals by kind and keeps only the deer until reaching the size of the tigers
                    Set<Animal> cancelledAnimals2 = priorityMeadows.stream()
                            .flatMap(m -> m.animals().stream())
                            .filter(d -> d.kind() == Animal.Kind.DEER)
                            .limit(tigers.size())
                            .collect(Collectors.toSet());

                    cancelledAnimals.addAll(cancelledAnimals2);
                    int eatenDeer = cancelledAnimals2.size();

                    //Filters the deer to check if they aren't already cancelled, and adds them until reaching the limit (number of tigers - eaten deer)
                    Set<Animal> cancelledAnimals3 = deer.stream()
                            .filter(d -> !cancelledAnimals.contains(d))
                            .limit(tigers.size() - eatenDeer)
                            .collect(Collectors.toSet());

                    cancelledAnimals.addAll(cancelledAnimals3);
                }
            }
        }
        return cancelledAnimals;
    }
}
