package ch.epfl.chacun;

import java.util.Comparator;
import java.util.List;

import static ch.epfl.chacun.Base32.*;
import static ch.epfl.chacun.Board.REACH;

/**
 * This class encodes and decodes the actions of the players.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public class ActionEncoder {
    //private constructor to prevent instantiation
    private ActionEncoder() {}

    /**
     * This method encodes the action of placing a tile.
     *
     * @param gameState the current state of the game
     * @param placedTile the placed tile
     * @return the encoded action
     */
    public static StateAction withPlacedTile(GameState gameState, PlacedTile placedTile) {
        List<Pos> list = gameState.board().insertionPositions()
                .stream()
                .sorted(Comparator.comparingInt(p -> p.x() * (REACH * 2 + 1) + p.y()))
                .toList();
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
        int index = (occupant == null) ? 0x1f : occupant.kind().ordinal() * 16 + Zone.localId(occupant.zoneId());
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
        int index = (occupant == null) ?
                0x1f :
                gameState.board().occupants().stream()
                    .sorted(Comparator.comparingInt(Occupant::zoneId))
                    .toList()
                    .indexOf(occupant); //TODO : vérifier si c'est le bon id ou pas
        return new StateAction(gameState.withOccupantRemoved(occupant), encodeBits5(index));
    }

    /**
     * This method decodes and applies the action of the player.
     *
     * @param gameState the current state of the game
     * @param str the encoded action
     * @return the decoded and applied action
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
                int rotation = index % 4;
                int posIndex = index / 4;
                Pos pos = gameState.board().insertionPositions()
                        .stream()
                        .sorted(Comparator.comparingInt(p -> p.x() * (REACH * 2 + 1) + p.y()))
                        .toList()
                        .get(posIndex);
                PlacedTile pT = new PlacedTile(gameState.tileToPlace(), gameState.currentPlayer(), Rotation.values()[rotation], pos);
                yield withPlacedTile(gameState, pT);
            }

            case OCCUPY_TILE -> {
                int kind = (index >> 4) & 1;
                int localId = index & 0xf;
                if (localId == 0xf) yield withNewOccupant(gameState, null);
                else {
                    for (Occupant o : gameState.lastTilePotentialOccupants()) {
                        if (Zone.localId(o.zoneId()) == localId && o.kind().ordinal() == kind)
                            yield withNewOccupant(gameState, o);
                    }
                }
                throw new DecoderException();
            }

            case RETAKE_PAWN -> {
                Occupant occupant2 = gameState.board().occupants().stream()
                        .sorted(Comparator.comparingInt(Occupant::zoneId))
                        .toList()
                        .get(index);
                yield withOccupantRemoved(gameState, occupant2);
            }

            default -> throw new DecoderException();
        };

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
//TODO : static