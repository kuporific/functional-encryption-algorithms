package com.implementsblog.functional;

import java.util.SortedSet;
import java.util.function.IntUnaryOperator;

/**
 * Contains functions that implement the Atbash Cipher.
 * <p>
 * This cipher encrypts text by reversing a sequence of characters; for example,
 * given the sequence [a-z], 'a' is switched for 'z', 'b' is switched for 'y',
 * etc.
 * <p>
 * Cipher text can be decrypted by encrypting over the same sequence of
 * characters.
 */
public final class AtbashCipher
{
    /**
     * Applies the atbash cipher to the given {@code string}, switching
     * code points within the [a-z] and [A-Z] ranges, all other code points
     * remain unchanged.
     *
     * @param string the string to encrypt
     *
     * @return never {@code null}.
     */
    public static String encrypt(final String string)
    {
        return SubstitutionCipher.encrypt(string, AtbashCipher::swap);
    }

    /**
     * Applies the atbash cipher to the given {@code string} with the given
     * {@code codePointRanges}, all code points outside of the ranges remain
     * unchanged.
     *
     * @param string the string to encrypt
     *
     * @return never {@code null}.
     */
    public static String encrypt(
            final String string,
            final SortedSet<Integer> codePointRanges)
    {
        return SubstitutionCipher.encrypt(
                string,
                AtbashCipher::swap,
                codePointRanges);
    }

    /**
     * Returns a function that reverses a given code point over the range from
     * [lowest-highest] (inclusive).
     *
     * @param lowest the lowest code point in the range
     * @param highest the highest code point in the range
     *
     * @return never {@code null}.
     */
    private static IntUnaryOperator swap(final int lowest, final int highest)
    {
        return c -> lowest <= c && c <= highest
                ? highest - (c - lowest)
                : c;
    }

    private AtbashCipher()
    {
        throw new UnsupportedOperationException(
                "Don't instantiate a utility class.");
    }
}
