package ch.epfl.chacun;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
        switch(gameState.nextAction()) {
            case PLACE_TILE:
                int index = decode(str);
                int rotation = index % 4;
                int posIndex = index / 4;
                Pos pos = gameState.board().insertionPositions()
                        .stream()
                        .sorted(Comparator.comparingInt(p -> p.x() * (REACH * 2 + 1) + p.y()))
                        .toList()
                        .get(posIndex);
                return withPlacedTile(gameState, new PlacedTile(gameState.board().tileAt(pos).tile(), gameState.currentPlayer(), Rotation.values()[rotation], pos));
        }
    }

    public record StateAction(GameState gameState, String action) {}
}
