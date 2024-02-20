package ch.epfl.chacun;

import static ch.epfl.chacun.Preconditions.checkArgument;

final public class Points {
    private Points() {}

    static int forClosedForest(int tileCount, int mushroomGroupCount) {
        checkArgument(tileCount > 1 && mushroomGroupCount >= 0);
        return (2 * tileCount + 3 * mushroomGroupCount);
    }

    static int forClosedRiver(int tileCount, int fishCount) {
        checkArgument(tileCount > 1 && fishCount >= 0);
        return (tileCount + fishCount);
    }

    static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        checkArgument(mammothCount >= 0 && aurochsCount >= 0 && deerCount >= 0);
        return (3 * mammothCount + 2 * aurochsCount + deerCount);
    }

    static int forRiverSystem(int fishCount) {
        checkArgument(fishCount >= 0);
        return fishCount;
    }

    static int forLogboat(int lakeCount) {
        checkArgument(lakeCount > 0);
        return lakeCount;
    }

    static int forRaft(int lakeCount) {
        checkArgument(lakeCount > 0);
        return lakeCount;
    }

}
