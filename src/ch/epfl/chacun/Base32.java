package ch.epfl.chacun;

public class Base32 {
    //private constructor to prevent instantiation
    private Base32() {}

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    public static boolean isValid(String s) {
        if (s == null) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!ALPHABET.contains(String.valueOf(s.charAt(i))))
                return false;
        }
        return true;
    }

    public static String encodeBits5(int i) {
        if (! (0 <= i && i < 32)) throw new IllegalArgumentException();
        return String.valueOf(ALPHABET.charAt(i & 0x1f));
    }

    public static String encodeBits10(int i) {
<<<<<<< Updated upstream
        return encodeBits5(i >>> 5) + encodeBits5(i & 0x1f);
=======
        if (! (0 <= i && i < 1024)) throw new IllegalArgumentException();
        return encodeBits5(i >>> 5) + encodeBits5(i);
>>>>>>> Stashed changes
    }

    public static int decode(String s) {
        if (s == "") throw new IllegalArgumentException();
        if (! isValid(s)) throw new IllegalArgumentException();
        return ((s.length() == 1) ?
                ALPHABET.indexOf(s.charAt(0)) :
                32 * ALPHABET.indexOf(s.charAt(0)) + ALPHABET.indexOf(s.charAt(1)));
    }

}
