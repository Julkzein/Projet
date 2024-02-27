package ch.epfl.chacun;

import java.util.Set;

public record ZonePartition<Z extends Zone>(Set<Area<Z>> areas) {
    public ZonePartition {
        areas = Set.copyOf(areas);
    }*
}
