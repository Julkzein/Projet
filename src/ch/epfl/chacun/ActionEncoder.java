package ch.epfl.chacun;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Base32.encodeBits10;
import static ch.epfl.chacun.Board.REACH;
import static java.lang.Integer.compare;

public class ActionEncoder {
    //private constructor to prevent instantiation
    private ActionEncoder() {}

    public StateAction withPlacedTile(GameState gameState, PlacedTile placedTile) {
        List<Pos> list = gameState.board().insertionPositions()
                .stream()
                .sorted(Comparator.comparingInt(p -> p.x() * (REACH * 2 + 1) + p.y()))
                .collect(Collectors.toList());
        return new StateAction(gameState.withPlacedTile(placedTile), encodeBits10())
    }


    private record StateAction(GameState gameState, String action) {}
}
