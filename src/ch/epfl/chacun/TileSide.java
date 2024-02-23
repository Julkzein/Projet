package ch.epfl.chacun;

import java.util.List;

public sealed interface TileSide {
    public abstract List<Zone> zones();
    public abstract boolean isSameKindAs(TileSide that);

    record Forest(Forest forest) implements TileSide {
        public List<Zone> zones() {

        }

        public boolean isSameKindAs(TileSide that){
            return that instanceof Forest;
        }
    }

    record Meadow(Meadow meadow) implements TileSide {
        public List<Zone> zones() {

        }

        public boolean isSameKindAs(TileSide that) {
            return that instanceof Meadow
        }
    }

    record River(River river) implements TilesSide {
        public List<Zone> zones() {

        }

        public boolean isSameKindAs(TileSide)
    }
}
