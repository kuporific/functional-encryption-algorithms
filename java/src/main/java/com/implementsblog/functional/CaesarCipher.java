package com.implementsblog.functional;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.IntStream;

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
     *     numbers shift "down" the alphabet, e.g., -2 turns 'A' into 'Y'. If
     *     {@code abs(shiftAmount) >= 26}, the shift will "wrap around", e.g.,
     *     if {@code shiftAmount == 27}, 'a' turns into 'b'.
     *
     * @return an encrypted string.
     */
    public static String encrypt(final String string, final int shiftAmount)
    {
        return encrypt(
                string,
                shiftAmount,
                "azAZ".codePoints()
                        .collect(
                                TreeSet::new,
                                TreeSet::add,
                                TreeSet::addAll));
    }

    /**
     * Applies a Caesar cipher to the given {@code string}, shifting by the
     * given {@code shiftAmount}, for the characters within the given {@code
     * characterRanges}.
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
        if (codePointRanges.size() % 2 != 0)
        {
            throw new IllegalArgumentException();
        }

        return mapAll(
                string.chars(),
                shiftAmount,
                codePointRanges.stream().collect(asShifters(toList())))
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append).toString();
    }

    /**
     * Applies each {@code functions} with the {@link IntStream#map map
     * function} of the {@code stream}.
     *
     * @param stream the stream to apply the {@code functions} to
     * @param shiftAmount amount the characters will be shifted
     * @param functions the shifting functions to apply to the {@code stream}
     *
     * @return the result of applying each of the {@code functions} with the
     *     {@link IntStream#map map function} of the {@code stream}.
     */
    private static IntStream mapAll(
            final IntStream stream,
            final int shiftAmount,
            final List<BinaryOperator<Integer>> functions)
    {
        if (!functions.isEmpty())
        {
            return mapAll(
                    stream.map(t -> functions.get(0).apply(t, shiftAmount)),
                    shiftAmount,
                    functions.subList(1, functions.size()));
        }
        else
        {
            return stream;
        }
    }

    /**
     * A collector that creates a collection of {@link #shift shifters}.
     *
     * @param downstream the collector type to add {@link #shift shifters} to.
     * @param <A> Intermediate type (of type Pair, but doesn't really matter)
     * @param <R> Return type of the {@code downstream} collector
     *
     * @return A Collector that creates a collection of {@link #shift shifters}.
     */
    private static <A, R> Collector<Integer, ?, R> asShifters(
            final Collector<BinaryOperator<Integer>, A, R> downstream)
    {
        class Pair
        {
            Integer lower;
            A intermediate = downstream.supplier().get();

            private void add(Integer i)
            {
                if (lower == null)
                {
                    lower = i;
                }
                else
                {
                    downstream
                            .accumulator()
                            .accept(intermediate, shift(lower, i));

                    lower = null;
                }
            }

            private Pair combine(Pair other)
            {
                throw new UnsupportedOperationException(
                        "Should not have been called because this Collector "
                                + "does not specify the "
                                + Collector.Characteristics.CONCURRENT
                                + " characteristic.");
            }

            private R finish()
            {
                return downstream.finisher().apply(intermediate);
            }
        }

        return Collector.of(Pair::new, Pair::add, Pair::combine, Pair::finish);
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
    private static BinaryOperator<Integer> shift(
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

    public static void main(String[] args)
    {
        System.out.println(0x10FFFF / 8);
    }
}
