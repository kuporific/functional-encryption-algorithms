package com.implementsblog.functional;

import static com.implementsblog.functional.AtbashCipher.encrypt;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Tests the {@link AtbashCipher} class.
 */
public class AtbashCipherTest
{
    private final static Random RANDOM = new Random();

    @Test(dataProvider = "knownValuesProvider")
    public void testKnownValues(final String cipherText, final String text)
    {
        assertThat(cipherText).isEqualTo(encrypt(text));
    }

    @Test(dataProvider = "testInverseAsciiProvider")
    public void testInverseAscii(final String text)
    {
        assertThat(text).isEqualTo(encrypt(encrypt(text)));
    }

    @Test(dataProvider = "testInverseGeneralProvider")
    public void testInverseGeneral(
            final String text,
            final SortedSet<Integer> codePointRanges)
    {
        assertThat(text)
                .isEqualTo(
                        encrypt(
                                encrypt(text, codePointRanges),
                                codePointRanges));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGeneralOddCodePointRange()
    {
        encrypt("value", "azA".codePoints()
                .collect(TreeSet::new, TreeSet::add, TreeSet::addAll));
    }

    @DataProvider
    private static Object[][] knownValuesProvider()
    {
        return new Object[][] {
                {
                        "the quick red fox jumped over the lazy brown dog "
                                + "0123456789 !@#$%^&*() THE QUICK RED FOX "
                                + "JUMPED OVER THE LAZY BROWN DOG.",
                        "gsv jfrxp ivw ulc qfnkvw levi gsv ozab yildm wlt "
                                + "0123456789 !@#$%^&*() GSV JFRXP IVW ULC "
                                + "QFNKVW LEVI GSV OZAB YILDM WLT." } };
    }

    @DataProvider
    private static Object[][] testInverseAsciiProvider()
    {
        return Stream.<Function<Integer, String>>of(
                RandomStringUtils::random,
                RandomStringUtils::randomAscii,
                RandomStringUtils::randomAlphanumeric,
                RandomStringUtils::randomAlphabetic,
                RandomStringUtils::randomNumeric)
                // Create a list of random strings of each type
                .flatMap(s -> IntStream.range(0, 100).mapToObj(i -> s.apply(50)))
                .toArray(Object[][]::new);
    }

    @DataProvider
    private static Object[][] testInverseGeneralProvider()
    {
        return Arrays.stream(testInverseAsciiProvider())
                .map(array -> {
                    final Object[] objects = Arrays.copyOf(
                            array,
                            array.length + 1);
                    objects[array.length] = randomCodePointRanges();
                    return objects;
                })
                .toArray(Object[][]::new);
    }

    private static SortedSet<Integer> randomCodePointRanges()
    {
        final TreeSet<Integer> collect = IntStream
                .generate(() -> (RANDOM.nextBoolean() ? 'a' : 'A')
                        + RANDOM.nextInt('z' - 'a' + 1))
                .limit(10)
                .collect(
                        TreeSet::new,
                        TreeSet::add,
                        TreeSet::addAll);

        // Don't allow odd sized sets.
        if (collect.size() % 2 != 0)
        {
            collect.remove(collect.last());
        }

        return collect;
    }
}
