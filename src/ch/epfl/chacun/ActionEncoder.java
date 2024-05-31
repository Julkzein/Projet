package ch.epfl.chacun;

import java.util.Comparator;
import java.util.List;

import static ch.epfl.chacun.Base32.*;

/**
 * This class encodes and decodes the actions of the players.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class ActionEncoder {
    //Private constructor to prevent instantiation
    private ActionEncoder() {}

    private static int nullOccupant = 0x1f;

    /**
     * This method encodes the action of placing a tile.
     *
     * @param gameState the current state of the game
     * @param placedTile the placed tile
     * @return the encoded action
     */
    public static StateAction withPlacedTile(GameState gameState, PlacedTile placedTile) {
        List<Pos> list = getPosList(gameState);
        int index = (list.indexOf(placedTile.pos()) * 4) + placedTile.rotation().ordinal();
        return new StateAction(gameState.withPlacedTile(placedTile), encodeBits10(index));
    }

    /**
     * This method encodes the action of placing a pawn or hut.
     *
     * @param gameState the current state of the game
     * @param occupant the occupant to place
     * @return the encoded action
     */
    public static StateAction withNewOccupant(GameState gameState, Occupant occupant) {
        int index = (occupant == null) ? nullOccupant : occupant.kind().ordinal() * 16 + Zone.localId(occupant.zoneId());
        return new StateAction(gameState.withNewOccupant(occupant), encodeBits5(index));
    }

    /**
     * This method encodes the action of removing a pawn or hut.
     *
     * @param gameState the current state of the game
     * @param occupant the occupant to remove
     * @return the encoded action
     */
    public static StateAction withOccupantRemoved(GameState gameState, Occupant occupant) {
        int index = (occupant == null) ? nullOccupant : getOccupantList(gameState).indexOf(occupant);
        return new StateAction(gameState.withOccupantRemoved(occupant), encodeBits5(index));
    }

    /**
     * This method decodes and applies the action of the player.
     *
     * @param gameState the current state of the game
     * @param str the encoded action
     * @return the decoded and applied action or null if the code isn't valid
     */
    public static StateAction decodeAndApply(GameState gameState, String str) {
        try {
            return decodeOrThrow(gameState, str);
        } catch (DecoderException e) {
            return null;
        }
    }

    /**
     * This method decodes the action of the player or throws an error if the code isn't valid.
     *
     * @param gameState the current state of the game
     * @param str the encoded action
     * @throws DecoderException if the code isn't valid
     * @return the decoded action
     */
    private static StateAction decodeOrThrow(GameState gameState, String str) throws DecoderException {
        if (!isValid(str) || (str.length() != 1 && str.length() != 2) || gameState.nextAction() == null) {
            throw new DecoderException();
        }

        int index = decode(str);
        return switch(gameState.nextAction()) {
            case PLACE_TILE -> {
                if (str.length() != 2) throw new DecoderException();
                int rotation = index % 4; //the last 2 bits
                int posIndex = index >> 2; //the first 8 bits
                if (posIndex >= gameState.board().insertionPositions().size()) throw new DecoderException();

                Pos pos = getPosList(gameState).get(posIndex);
                PlacedTile pT = new PlacedTile(
                        gameState.tileToPlace(),
                        gameState.currentPlayer(),
                        Rotation.values()[rotation],
                        pos
                );
                if (!gameState.board().canAddTile(pT)) throw new DecoderException();

                yield withPlacedTile(gameState, pT);
            }

            case OCCUPY_TILE -> {
                if (index == nullOccupant) yield withNewOccupant(gameState, null);
                int kind = (index >> 4) & 1; //
                int localId = index & 0xf;
                if (str.length() != 1 || localId >= 10) throw new DecoderException();
                for (Occupant o : gameState.lastTilePotentialOccupants()) {
                    if (Zone.localId(o.zoneId()) == localId && o.kind().ordinal() == kind)
                        yield withNewOccupant(gameState, o);
                }
                throw new DecoderException();
            }

            case RETAKE_PAWN -> {
                if (index == nullOccupant) yield withOccupantRemoved(gameState, null);
                if (str.length() != 1 || index >= 25) throw new DecoderException();
                Occupant occupant2 = getOccupantList(gameState).get(index);
                yield withOccupantRemoved(gameState, occupant2);
            }

            default -> throw new DecoderException();
        };
    }

    /**
     * Returns the list of insertion positions sorted by x and y coordinates.
     * @param gameState the current state of the game
     * @return the list of insertion positions sorted by x and y coordinates
     */
    private static List<Pos> getPosList(GameState gameState) {
        return gameState.board().insertionPositions()
                .stream()
                .sorted(Comparator.comparing(Pos::x).thenComparing(Pos::y))
                .toList();
    }

    /**
     * Returns the list of pawn occupants sorted by zone id.
     * @param gameState the current state of the game
     * @return the list of pawn occupants sorted by zone id
     */
    private static List<Occupant> getOccupantList(GameState gameState) {
        return gameState.board().occupants().stream()
                .filter(o -> o.kind() == Occupant.Kind.PAWN)
                .sorted(Comparator.comparingInt(Occupant::zoneId))
                .toList();
    }

    /**
     * This exception is thrown when the code isn't valid.
     */
    private static class DecoderException extends Exception{}

    /**
     * This class represents a state action, which is a pair of a game state and an action.
     */
    public record StateAction(GameState gameState, String action) {}
}