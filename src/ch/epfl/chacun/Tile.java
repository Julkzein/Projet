package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a tile in the game.
 *
 * @param id The id of the tile
 * @param kind The kind of the tile
 * @param n The north side of the tile
 * @param e The east side of the tile
 * @param s The south side of the tile
 * @param w The west side of the tile *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public record Tile(int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {

    /**
     * The three possible kinds of a tile.
     */
    public enum Kind {
        START,
        NORMAL,
        MENHIR
    }

    /**
     * This method the four sides of the tile.
     *
     * @return the four sides of the tile.
     */
    public List<TileSide> sides() {
        return List.of(n, e, s, w);
    }

    /**
     * This method returns zones of the sides of the tile.
     *
     * @return the zones of the sides of the tile.
     */
    public Set<Zone> sideZones() {
        Set<Zone> sideZonesSet = new HashSet<>();
        sides().forEach(side -> sideZonesSet.addAll(side.zones()));
        return sideZonesSet;
    }

    /**
     * This method returns the zones of the tile.
     * 
     * @return the zones of the tile.
     */
    public Set<Zone> zones() {
        Set<Zone> sideZonesSet = sideZones();
        Set<Zone> zonesSet = new HashSet<>(sideZonesSet);
        for (Zone zone : sideZonesSet) {
            if (zone instanceof Zone.River river && river.hasLake()) {
                zonesSet.add(river.lake());
            }
        }
        return zonesSet;
    }
}
