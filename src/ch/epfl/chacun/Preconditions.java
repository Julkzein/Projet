package ch.epfl.chacun;

/**
 * Utility class for checking preconditions.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public final class Preconditions {
    private Preconditions() {}

    /**
     * Throws an IllegalArgumentException if the given boolean is false.
     *
     * @param shouldBeTrue the boolean to check.
     * @throws IllegalArgumentException if the given boolean is false.
     */
    public static void checkArgument(boolean shouldBeTrue) throws IllegalArgumentException {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}
