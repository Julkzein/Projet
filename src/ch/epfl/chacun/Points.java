package ch.epfl.chacun;



/**
 * Represents the points that can be scored in the game.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public final class Points {
    private Points() {}

    /**
     * Returns the number of points scored by closing the forest.
     *
     * @param tileCount the number of tiles in the closed forest.
     * @param mushroomGroupCount the number of mushroom groups in the closed forest.
     * @return the number of points scored by closing the forest.
     * @throws IllegalArgumentException if the tile count is less than 2 or if the mushroom group count is negative.
     */
    public static int forClosedForest(int tileCount, int mushroomGroupCount) {
        Preconditions.checkArgument(tileCount > 1 && mushroomGroupCount >= 0);
        return 2 * tileCount + 3 * mushroomGroupCount;
    }


    /**
     * Returns the number of points scored by closing the river.
     *
     * @param tileCount the number of tiles in the closed river.
     * @param fishCount the number of fish in the closed river.
     * @return the number of points scored by closing the river.
     * @throws IllegalArgumentException if the tile count is less than 2 or if the fish count is negative.
     */
    public static int forClosedRiver(int tileCount, int fishCount) {
        Preconditions.checkArgument(tileCount > 1 && fishCount >= 0);
        return tileCount + fishCount;
    }


    /**
     * Returns the number of points scored for a meadow.
     *
     * @param mammothCount the number of mammoth in the meadow.
     * @param aurochsCount the number of aurochs in the meadow.
     * @param deerCount the number of deer in the meadow.
     * @return the number of points scored for a meadow.
     * @throws IllegalArgumentException if the mammoth count, aurochs count or deer count is negative.
     */
    public static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        Preconditions.checkArgument(mammothCount >= 0 && aurochsCount >= 0 && deerCount >= 0);
        return 3 * mammothCount + 2 * aurochsCount + deerCount;
    }

    /**
     * Returns the number of points scored for the river system.
     *
     * @param fishCount the number of fish in the lake.
     * @return the number of points scored for the river system.
     * @throws IllegalArgumentException if the fish count is negative.
     */
    public static int forRiverSystem(int fishCount) {
        Preconditions.checkArgument(fishCount >= 0);
        return fishCount;
    }

    /**
     * Returns the number of points scored for the logboat.
     *
     * @param lakeCount the number of lake.
     * @return the number of points scored for the logboat.
     * @throws IllegalArgumentException if the lake count is negative.
     */
    public static int forLogboat(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return 2 * lakeCount;
    }


    /**
     * Returns the number of points scored for the raft.
     *
     * @param lakeCount the number of lake.
     * @return the number of points scored for the raft.
     * @throws IllegalArgumentException if the lake count is negative or equal to 0.
     */
    public static int forRaft(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount;
    }
}