package ch.epfl.chacun;

import java.util.List;

public sealed interface TileSide {
    public abstract List<Zone> zones();
    public abstract boolean isSameKindAs(TileSide that);

    record Forest(Zone.Forest forest) implements TileSide {
        public List<Zone> zones() {
            return List.of(forest);
        }

        public boolean isSameKindAs(TileSide that){
            return that instanceof Forest;
        }
    }

    record Meadow(Zone.Meadow meadow) implements TileSide {
        public List<Zone> zones() {
            return List.of(meadow);
        }

        public boolean isSameKindAs(TileSide that) {
            return that instanceof Meadow;
        }
    }

    record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide {
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        public boolean isSameKindAs(TileSide that) {
            return that instanceof River;
        }
    }
}
