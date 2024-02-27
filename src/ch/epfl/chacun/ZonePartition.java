package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public record ZonePartition<Z extends Zone>(Set<Area<Z>> areas) {
    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    public ZonePartition() {
        this(new HashSet<>());
    }

    public Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas) {
            if (area.zones().contains(zone)) {
                return area;
            }
        }
        throw new IllegalArgumentException();
    }

    static class Builder<Z extends Zone>{
        private Set<Area<Z>> areas = new HashSet<>();

        public Builder(ZonePartition<Z> zonePartition) {
            areas = zonePartition.areas;
        }

        void addSingleton(Z zone, int openConnections) {
            areas.add(new Area<>(Set.of(zone), new ArrayList<>(), openConnections));
        }
    }
}
