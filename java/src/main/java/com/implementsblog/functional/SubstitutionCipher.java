package com.implementsblog.functional;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collector;
import java.util.stream.IntStream;

/**
 *
 */
final class SubstitutionCipher
{
    /**
     * Applies a substitution cipher to the given {@code string} and {@code
     * codePointMapper}. The domain and range of {@code codePointMapper} must be
     * [a-z] and [A-Z], all other characters should remain the same.
     * <p>
     * A string encrypted with key {@code k} can be decrypted by applying
     * this method using {@code -k}.
     *
     * @param string Any string to encrypt
     * @param codePointMapper the amount to shift by. Positive numbers shift "up"
     *     the alphabet, e.g., a shift value of 2 turns 'a' into 'c'; negative
     *     numbers shift "down" the alphabet, e.g., -2 turns 'A' into 'Y'. If
     *     {@code abs(shiftAmount) >= 26}, the shift will "wrap around", e.g.,
     *     if {@code shiftAmount == 27}, 'a' turns into 'b'.
     *
     * @return an encrypted string.
     */
    public static String encrypt(
            final String string,
            final CodePointMapper codePointMapper)
    {
        return encrypt(
                string,
                codePointMapper,
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
     * <p>
     * A string encrypted with key {@code k} and code point ranges {@code r} can
     * be decrypted by applying this method using {@code -k} and {@code r}.
     *
     * @param string Any string to encrypt
     * @param codePointMapper the amount to shift by. Positive numbers shift "up"
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
            final CodePointMapper codePointMapper,
            final SortedSet<Integer> codePointRanges)
    {
        if (codePointRanges.size() % 2 != 0)
        {
            throw new IllegalArgumentException();
        }

        return mapAll(
                string.chars(),
                codePointRanges
                        .stream()
                        .collect(asMappers(codePointMapper, toList())))
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append).toString();
    }
    /**
     * A collector that creates a collection of {@code substitute} functions.
     *
     * @param downstream the collector type to add {@code substitutes} to.
     * @param codePointMapper a function that performs character substitution
     * @param <A> Intermediate type (of type Pair, but doesn't really matter)
     * @param <R> Return type of the {@code downstream} collector
     *
     * @return never {@code null}.
     */
    private static <A, R> Collector<Integer, ?, R> asMappers(
            final CodePointMapper codePointMapper,
            final Collector<IntUnaryOperator, A, R> downstream)
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
                            .accept(intermediate, codePointMapper.apply(lower, i));

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
     * Applies each {@code functions} with the {@link IntStream#map map
     * function} of the {@code stream}.
     *
     * @param stream the stream to apply the {@code functions} to
     * @param functions the shifting functions to apply to the {@code stream}
     *
     * @return the result of applying each of the {@code functions} with the
     *     {@link IntStream#map map function} of the {@code stream}.
     */
    static IntStream mapAll(
            final IntStream stream, final List<IntUnaryOperator> functions)
    {
        return !functions.isEmpty()
                ? mapAll(
                        stream.map(c -> functions.get(0).applyAsInt(c)),
                        functions.subList(1, functions.size()))
                : stream;
    }
}
