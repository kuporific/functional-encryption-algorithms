package com.implementsblog.functional;

import java.util.function.BiFunction;

/**
 * Contains functions that implement the Caesar Cipher.
 * <p>
 * The Caesar cipher encrypts text by shifting alphabetic characters up or down
 * the alphabetical order. For example, the word "Palindrome" shift by 2
 * characters down the alphabet (lexicographically) becomes "Rcnkpftqog".
 * <p>
 * This particular implementation only shifts alphabetic ASCII characters and
 * leaves all other types of characters unmodified in the cipher text.
 */
public final class CaesarCipher
{
    /**
     * Applies a Caesar cipher to the given {@code string}, shifting by the
     * given {@code shiftAmount}. Only the characters [a-z] and [A-Z] will be
     * shifted, all other characters will remain the same.
     *
     * @param string Any string to encrypt
     * @param shiftAmount the amount to shift by. Positive numbers shift "up"
     *     the alphabet, e.g., a shift value of 2 turns 'a' into 'c'; negative
     *     numbers shift "down" the alphabet, e.g., -2 turns 'a' into 'y'. If
     *     {@code abs(shiftAmount) >= 26}, the shift will "wrap around", e.g.,
     *     if {@code shiftAmount == 27}, 'a' turns into 'b'.
     *
     * @return an encrypted string.
     */
    public static String encrypt(final String string, final int shiftAmount)
    {
        return string
                .chars()
                .map(c -> shift('a', 'z').apply(c, shiftAmount))
                .map(c -> shift('A', 'Z').apply(c, shiftAmount))
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    /**
     * Curries a character Range (int code points), returning a function that
     *    shifts a given character by a given amount if it falls within the
     *    provided range.
     *
     * @param lowest Lowest character in the range, inclusive
     * @param highest Highest character in the range, inclusive
     *
     * @return a function that shifts a given character by a given amount if
     *     it's within the provided range.
     */
    private static BiFunction<Integer, Integer, Integer> shift(
            final int lowest,
            final int highest)
    {
        final int distance = highest - lowest + 1;
        return (codePoint, shift)
                -> lowest <= codePoint && codePoint <= highest
                ? ((codePoint - lowest + shift) % distance
                        + distance) % distance + lowest
                : codePoint;
    }

    private CaesarCipher()
    {
        throw new UnsupportedOperationException(
                "Don't instantiate a utility class.");
    }
}
