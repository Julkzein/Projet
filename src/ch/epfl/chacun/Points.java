package ch.epfl.chacun;

final public class Points {
    private Points() {}

    static int forClosedForest(int tileCount, int mushroomGroupCount) {
        return (2 * tileCount + 3 * mushroomGroupCount);
    }

    static int forClosedRiver(int tileCount, int fishCount) {
        return ( tileCount + fishCount);
    }

    static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        return (3 * mammothCount + 2 * aurochsCount + deerCount);
    }

}
