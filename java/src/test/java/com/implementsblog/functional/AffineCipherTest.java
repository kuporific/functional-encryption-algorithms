package com.implementsblog.functional;

import static com.implementsblog.functional.AffineCipher.decrypt;
import static com.implementsblog.functional.AffineCipher.encrypt;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.implementsblog.functional.AffineCipher.Key1;
import com.implementsblog.functional.AffineCipher.Key2;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 */
public class AffineCipherTest
{
    @Test(dataProvider = "testEncryptDecryptAreInverseProvider")
    public void testEncryptDecryptAreInverse(String text, Key1 key1, Key2 key2)
    {
        assertThat(text)
                .isEqualTo(decrypt(encrypt(text, key1, key2), key1, key2));
    }


    @DataProvider
    private static Object[][] testEncryptDecryptAreInverseProvider()
    {
        // Create a long list of different classes of random strings "mapped" to
        // all possible key combinations, e.g,
        //
        //     { "random string", Key1._3, Key2._12 }
        //
        return Stream.<Function<Integer, String>>of(
//                RandomStringUtils::random,
                RandomStringUtils::randomAscii,
//                RandomStringUtils::randomAlphanumeric,
//                RandomStringUtils::randomAlphabetic,
                RandomStringUtils::randomNumeric)
                // Create a list of random strings of each type
                .flatMap(s -> IntStream.range(0, 10).mapToObj(i -> s.apply(5)))
                // Pair each random string with keys
                .flatMap(string
                        -> Arrays.stream(Key1.values())
                        .flatMap(k1
                                -> Arrays
                                .stream(Key2.values())
                                .map(k2 -> new Object[] { string, k1, k2 })))
                .toArray(Object[][]::new);
    }
}
