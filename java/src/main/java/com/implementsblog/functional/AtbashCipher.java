package com.implementsblog.functional;

import java.util.SortedSet;
import java.util.function.IntUnaryOperator;

/**
 *
 */
public final class AtbashCipher
{
    public static String encrypt(final String string)
    {
        return SubstitutionCipher.encrypt(string, AtbashCipher::swap);
    }

    public static String encrypt(
            final String string,
            final SortedSet<Integer> codePointRanges)
    {
        return SubstitutionCipher.encrypt(
                string, AtbashCipher::swap,
                codePointRanges);
    }

    private static IntUnaryOperator swap(int lowest, int highest)
    {
        return c -> lowest <= c && c <= highest
                ? highest - (c - lowest)
                : c;
    }
}
