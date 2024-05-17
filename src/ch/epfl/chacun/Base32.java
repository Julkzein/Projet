package ch.epfl.chacun;

public class Base32 {
    //private constructor to prevent instantiation
    private Base32() {}

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    public static boolean isValid(String s) { //TODO : check if null
        if (s.isEmpty())
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (!ALPHABET.contains(String.valueOf(s.charAt(i))))
                return false;
        }
        return true;
    }

    public static String encodeBits5(int i) {
        return String.valueOf(ALPHABET.charAt(i & 0x1f));
    }

    public static String encodeBits10(int i) {
        return encodeBits5(i >>> 5) + encodeBits5(i);
    }

    public static int decode(String s) {
        return ((s.length() == 1) ?
                ALPHABET.indexOf(s.charAt(0)) :
                32 * ALPHABET.indexOf(s.charAt(0)) + ALPHABET.indexOf(s.charAt(1)));
    }

}
