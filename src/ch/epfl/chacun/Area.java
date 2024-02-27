package ch.epfl.chacun;

import java.util.List;
import java.util.Set;

/**
 * Represent the area that are composed of several zones 
 *
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record Area<Z extends Zone>(Set<Z> zones, List<PlayerColor> occupants, int openConnections) {
    public Area {
        if (openConnections < 0) {
            throw new IllegalArgumentException();
        }
    }
}
