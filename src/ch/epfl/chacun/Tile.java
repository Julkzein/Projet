package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a tile.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public record Tile(int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {

    /**
     * The three possible kinds of a tile.
     */
    enum Kind {
        START,
        NORMAL,
        MENHIR;
    }

    /**
     * The four sides of the tile.
     * @return the four sides of the tile.
     */
    public List<TileSide> sides() {
        return List.of(n, e, s, w);
    }

    /**
     * The zones of the sides of the tile.
     * @return the zones of the sides of the tile.
     */
    public Set<Zone> sideZones() {
        Set<Zone> sideZonesSet = new HashSet<>();
        for (TileSide side : sides()) {
            sideZonesSet.addAll(side.zones());
        }
        return sideZonesSet;
    }

    /**
     * The zones of the tile.
     * @return the zones of the tile.
     */
    public Set<Zone> zones() {
        Set<Zone> zonesSet = new HashSet<>(sideZones());
        for(Zone zone : sideZones()) {
            if (zone instanceof Zone.River river) {
                if (river.hasLake()) {
                    zonesSet.add(river.lake());
                }
            }
        }
        return zonesSet;
    }
}
