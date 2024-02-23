package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Tile(int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {

    enum Kind {
        START,
        NORMAL,
        MENHIR;
    }

    public List<TileSide> sides() {
        return List.of(n, e, s, w);
    }

    public Set<Zone> sideZones() {
        Set sideZonesSet = new HashSet<Zone>();
        for (TileSide side : sides()) {
            sideZonesSet.addAll(side.zones());
        }
        return sideZonesSet;
    }

    public Set<Zone> zones() {
        Set zonesSet = new HashSet<Zone>();
        zonesSet.addAll(sideZones());
        for(TileSide zone : sides()) {
            if (zone instanceof TileSide.River) {
                if ((TileSide.River)zone.hasLake( )) {
                    zonesSet.add(Zone.Lake);
                }
            }
        }
    }
}
