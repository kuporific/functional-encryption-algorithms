package com.implementsblog.functional;

import java.util.function.IntUnaryOperator;

/**
 * Contains functions that implement the Affine Cipher.
 * <p>
 * The Affine cipher encrypts text by mapping a character {@code c} to another
 * character {@code x} using two keys, {@code a} and {@code b}, in the following manner:
 * <pre>
 *     x = a * c + b % 26
 * </pre>
 * Note that this is a particularly simple implementation of the cipher in that
 * it only encodes upper and lower case ASCII characters (a-z and A-Z). What it
 * lacks in flexibility it gains in type-safety: all possible key values are
 * enumerated as {@link Key1} and {@link Key2}.
 */
public final class AffineCipher
{
    /**
     * All possible values for {@code key1}.
     */
    public enum Key1
    {
        _1(1, 27),
        _3(3, 9),
        _5(5, 21),
        _7(7, 15),
        _9(9, 3),
        _11(11, 19),
        _15(15, 7),
        _17(17, 23),
        _19(19, 11),
        _21(21, 5),
        _23(23, 17),
        _25(25, 51);

        private final int value;
        private final int modularMultiplicativeInverse;

        Key1(int value, int modularMultiplicativeInverse)
        {
            this.value = value;
            this.modularMultiplicativeInverse = modularMultiplicativeInverse;
        }

        private int getValue()
        {
            return value;
        }

        public int getModularMultiplicativeInverse()
        {
            return modularMultiplicativeInverse;
        }
    }

    /**
     * All possible values for {@code key2}.
     */
    public static enum Key2
    {
        _0(0), _1(1), _2(2), _3(3), _4(4), _5(5), _6(6), _7(7), _8(8), _9(9),
        _10(10), _11(11), _12(12), _13(13), _14(14), _15(15), _16(16), _17(17),
        _18(18), _19(19), _20(20), _21(21), _22(22), _23(23), _24(24), _25(25);

        private final int value;

        Key2(int value)
        {
            this.value = value;
        }

        private int getValue()
        {
            return value;
        }
    }

    /**
     * Encrypts the given {@code text}.
     *
     * @param text the text to encrypt
     * @param key1 the first key
     * @param key2 the second key
     *
     * @return never {@code null}.
     */
    public static String encrypt(String text, Key1 key1, Key2 key2)
    {
        return apply(text, key1.getValue(), key2.getValue());
    }

    /**
     * Decrypts the given {@code cipherText}.
     *
     * @param cipherText the cipher text to decrypt
     * @param key1 the {{@link Key1} instance used to encrypt the original text
     * @param key2 the {{@link Key2} instance used to encrypt the original text
     *
     * @return never {@code null}.
     */
    public static String decrypt(String cipherText, Key1 key1, Key2 key2)
    {
        final int mmi = key1.getModularMultiplicativeInverse();
        return apply(cipherText, mmi, - mmi * key2.getValue());
    }

    /**
     * Generic Affine function.
     *
     * @param text the text to manipulate
     * @param x key 1
     * @param y key 2
     *
     * @return never {@code null}.
     */
    private static String apply(String text, int x, int y)
    {
        return text
                .chars()
                .map(shift('a', 'z', x, y))
                .map(shift('A', 'Z', x, y))
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    /**
     * Curries a character Range (int code points), returning a function that
     * shifts a given character by a given amount if it falls within the
     * provided range.
     *
     * @param lowest Lowest character in the range, inclusive
     * @param highest Highest character in the range, inclusive
     *
     * @return a function that shifts a given character by a given amount if
     * it's within the provided range.
     */
    private static IntUnaryOperator shift(
            final int lowest, final int highest, final int key1, final int key2)
    {
        final int distance = highest - lowest + 1;
        return codePoint
                -> lowest <= codePoint && codePoint <= highest
                ? ((key1 * (codePoint - lowest) + key2) % distance + distance)
                        % distance + lowest
                : codePoint;
    }
}
