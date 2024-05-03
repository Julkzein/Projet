package ch.epfl.chacun;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Base32.*;
import static ch.epfl.chacun.Board.REACH;

public class ActionEncoder {
    //private constructor to prevent instantiation
    private ActionEncoder() {}

    public StateAction withPlacedTile(GameState gameState, PlacedTile placedTile) {
        List<Pos> list = gameState.board().insertionPositions()
                .stream()
                .sorted(Comparator.comparingInt(p -> p.x() * (REACH * 2 + 1) + p.y()))
                .toList();
        int index = (list.indexOf(placedTile.pos()) * 4) + placedTile.rotation().ordinal();
        return new StateAction(gameState.withPlacedTile(placedTile), encodeBits10(index));
    }

    public StateAction withNewOccupant(GameState gameState, Occupant occupant) {
        int index = occupant.kind().ordinal() * 16 + occupant.zoneId(); //TODO : check si l'id c'est le bon où pas
        return new StateAction(gameState.withNewOccupant(occupant), encodeBits5(index));
    }

    public StateAction withOccupantRemoved(GameState gameState, Occupant occupant) {
        int index = occupant.zoneId(); //TODO : vérifier si c'est le bon id ou pas
        return new StateAction(gameState.withOccupantRemoved(occupant), encodeBits5(index));
    }

    public StateAction decodeAndApply(GameState gameState, String str) {
        try {
            return decodeOrThrow(gameState, str);
        } catch (Exception e) { //TODO : check si tous les cas d'erreurs sont bien gérés
            return null;
        }
    }

    private StateAction decodeOrThrow(GameState gameState, String str) {
        if (!isValid(str) || (str.length() != 1 && str.length() != 2) || gameState.nextAction() == null) {
            throw new IllegalArgumentException();
        }

        int index = decode(str);
        switch(gameState.nextAction()) {
            case PLACE_TILE:
                int rotation = index % 4;
                int posIndex = index / 4;
                Pos pos = gameState.board().insertionPositions()
                        .stream()
                        .sorted(Comparator.comparingInt(p -> p.x() * (REACH * 2 + 1) + p.y()))
                        .toList()
                        .get(posIndex);
                PlacedTile pT = new PlacedTile(gameState.tileToPlace(), gameState.currentPlayer(), Rotation.values()[rotation], pos);
                return withPlacedTile(gameState, pT);

            case OCCUPY_TILE:
                int kind = index / 16;
                int zoneId = index % 16;
                Occupant occupant1 = new Occupant(Occupant.Kind.values()[kind], zoneId);
                return withNewOccupant(gameState, occupant1);

            case RETAKE_PAWN:
                Occupant occupant2 = gameState.board().occupants().stream()
                        .sorted(Comparator.comparingInt(Occupant::zoneId))
                        .toList()
                        .get(index);
                return withOccupantRemoved(gameState, occupant2);

            default:
                throw new IllegalArgumentException();
        }
        return null;
    }

    public record StateAction(GameState gameState, String action) {}
}
