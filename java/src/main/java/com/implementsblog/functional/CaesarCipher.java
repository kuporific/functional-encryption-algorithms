package com.implementsblog.functional;

import java.util.SortedSet;
import java.util.function.IntUnaryOperator;

/**
 * Contains functions that implement the Caesar Cipher.
 * <p>
 * The Caesar cipher encrypts text by shifting alphabetic characters up or down
 * the alphabetical order. For example, the word "Palindrome" shift by 2
 * characters down the alphabet (lexicographically) becomes "Rcnkpftqog".
 */
public final class CaesarCipher
{
    /**
     * Applies a Caesar cipher to the given {@code string}, shifting by the
     * given {@code shiftAmount}. Only the characters [a-z] and [A-Z] will be
     * shifted, all other characters will remain the same.
     * <p>
     * A string encrypted with key {@code k} can be decrypted by applying
     * this method using {@code -k}.
     *
     * @param string Any string to encrypt
     * @param shiftAmount the amount to shift by. Positive numbers shift "up"
     *     the alphabet, e.g., a shift value of 2 turns 'a' into 'c'; negative
     *     numbers shift "down" the alphabet, e.g., -2 turns 'A' into 'Y'. If
     *     {@code abs(shiftAmount) >= 26}, the shift will "wrap around", e.g.,
     *     if {@code shiftAmount == 27}, 'a' turns into 'b'.
     *
     * @return an encrypted string.
     */
    public static String encrypt(final String string, final int shiftAmount)
    {
        return SubstitutionCipher.encrypt(
                string,
                (lowerCodePoint, upperCodePoint)
                        -> shift(lowerCodePoint, upperCodePoint, shiftAmount));
    }

    /**
     * Applies a Caesar cipher to the given {@code string}, shifting by the
     * given {@code shiftAmount}, for the characters within the given {@code
     * characterRanges}.
     * <p>
     * A string encrypted with key {@code k} and code point ranges {@code r} can
     * be decrypted by applying this method using {@code -k} and {@code r}.
     *
     * @param string Any string to encrypt
     * @param shiftAmount the amount to shift by. Positive numbers shift "up"
     *     the alphabet, e.g., a shift value of 2 turns 'a' into 'c'; negative
     *     numbers shift "down" the alphabet, e.g., -2 turns 'A' into 'Y'. If
     *     {@code abs(shiftAmount) >= 26}, the shift will "wrap around", e.g.,
     *     if {@code shiftAmount == 27}, 'a' turns into 'b' (assuming
     *     characterRanges contains e.g., 'a', 'z', 'A', 'Z').
     * @param codePointRanges defines the range of code points to which the
     *     shift is applied, where even indices designate the beginning
     *     (inclusive) of the range, and the odd indices designate the ending
     *     (inclusive) of the range.
     *
     * @return an encrypted string.
     *
     * @throws IllegalArgumentException if {@code characterRanges} contains an
     *     odd number of elements.
     */
    public static String encrypt(
            final String string,
            final int shiftAmount,
            final SortedSet<Integer> codePointRanges)
    {
        return SubstitutionCipher.encrypt(
                string,
                (lowerCodePoint, upperCodePoint) ->
                        shift(lowerCodePoint, upperCodePoint, shiftAmount),
                codePointRanges);
    }

    /**
     * Curries a character Range of code points and a shift amount, returning a
     * function that shifts a given character by that amount if it falls within
     * the provided range.
     *
     * @param lowest Lowest character in the range, inclusive
     * @param highest Highest character in the range, inclusive
     * @param shiftAmount The amount the character will be shifted
     *
     * @return a function that shifts a given character by that amount if it
     *     falls within the provided range.
     */
    private static IntUnaryOperator shift(
            final int lowest,
            final int highest,
            final int shiftAmount)
    {
        final int distance = highest - lowest + 1;
        return (codePoint)
                -> lowest <= codePoint && codePoint <= highest
                ? ((codePoint - lowest + shiftAmount) % distance
                        + distance) % distance + lowest
                : codePoint;
    }

    private CaesarCipher()
    {
        throw new UnsupportedOperationException(
                "Don't instantiate a utility class.");
    }
}
