package ch.epfl.chacun;

public class Base32 {
    //Private constructor to prevent instantiation
    private Base32() {}
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    /**
     * This method checks if a string is a valid base 32 string.
     *
     * @param s the string to check
     * @return true if the string is a valid base 32 string, false otherwise
     */
    public static boolean isValid(String s) {
        if (s == null) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!ALPHABET.contains(String.valueOf(s.charAt(i))))
                return false;
        }
        return true;
    }

    /**
     * This method encodes a 5-bit integer into a base 32 string.
     *
     * @param i the integer to encode
     * @return the string of the encoded integer
     */
    public static String encodeBits5(int i) {
        if (! (0 <= i && i < 32)) throw new IllegalArgumentException();
        return String.valueOf(ALPHABET.charAt(i & 0x1f));
    }

    /**
     * This method encodes a 10-bit integer in base 32.
     *
     * @param i the integer to encode
     * @return the string of the encoded integer
     */
    public static String encodeBits10(int i) {
        if (! (0 <= i && i < 1024)) throw new IllegalArgumentException();
        return encodeBits5(i >>> 5) + encodeBits5(i & 0x1f);
    }

    /**
     * This method decodes a base 32 string into a 5-bit integer.
     *
     * @param s the string to decode
     * @return the decoded integer
     */
    public static int decode(String s) {
        if (s.isEmpty() || !isValid(s)) throw new IllegalArgumentException();
        return ((s.length() == 1) ?
                ALPHABET.indexOf(s.charAt(0)) :
                32 * ALPHABET.indexOf(s.charAt(0)) + ALPHABET.indexOf(s.charAt(1)));
    }

}
