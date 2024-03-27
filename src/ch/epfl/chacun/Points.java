package ch.epfl.chacun;

import static ch.epfl.chacun.Preconditions.checkArgument;


/**
 * Represents the points that can be scored in the game.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public final class Points {
    private Points() {}

    /**
     * Returns the number of points scored by the given occupant(s) of the closed forest.
     *
     * @param tileCount the number of tiles in the closed forest.
     * @param mushroomGroupCount the number of mushroom groups in the closed forest.
     * @return the number of points scored by the given occupant(s).
     * @throws NullPointerException if the occupant is null.
     */
    public static int forClosedForest(int tileCount, int mushroomGroupCount) {
        checkArgument(tileCount > 1 && mushroomGroupCount >= 0);
        return 2 * tileCount + 3 * mushroomGroupCount;
    }


    /**
     * Returns the number of points scored by the given occupant(s) of the closed river.
     *
     * @param tileCount the number of tiles in the closed river.
     * @param fishCount the number of fish in the closed river.
     * @return the number of points scored by the given occupant(s).
     * @throws NullPointerException if the occupant is null.
     */
    public static int forClosedRiver(int tileCount, int fishCount) {
        checkArgument(tileCount > 1 && fishCount >= 0);
        return tileCount + fishCount;
    }


    /**
     * Returns the number of points scored by the given occupant(s) of the Meadow.
     *
     * @param mammothCount the number of mammoth in the meadow.
     * @param aurochsCount the number of aurochs in the meadow.
     * @param deerCount the number of deer in the meadow.
     * @return the number of points scored by the given occupant(s).
     * @throws NullPointerException if the occupant is null.
     */
    public static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        checkArgument(mammothCount >= 0 && aurochsCount >= 0 && deerCount >= 0);
        return 3 * mammothCount + 2 * aurochsCount + deerCount;
    }

    /**
     * Returns the number of points scored by the given occupant(s) of the RiverSystem.
     *
     * @param fishCount the number of fish in the lake.
     * @return the number of points scored by the given occupant(s).
     * @throws NullPointerException if the occupant is null.
     */
    public static int forRiverSystem(int fishCount) {
        checkArgument(fishCount >= 0);
        return fishCount;
    }

    /**
     * Returns the number of points scored by the player who placed the Logboat.
     *
     * @param lakeCount the number of lake.
     * @return the number of points scored by the given occupant(s).
     * @throws NullPointerException if the occupant is null.
     */
    public static int forLogboat(int lakeCount) {
        checkArgument(lakeCount > 0);
        return 2 * lakeCount;
    }


    /**
     * Returns the number of points scored by the player who possesses the most
     * fisherman on the river system where the raft is placed.
     *
     * @param lakeCount the number of lake.
     * @return the number of points scored by the given occupant(s).
     * @throws NullPointerException if the occupant is null.
     */
    public static int forRaft(int lakeCount) {
        checkArgument(lakeCount > 0);
        return lakeCount;
    }
}