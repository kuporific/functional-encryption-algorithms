package com.implementsblog.functional;

import static com.implementsblog.functional.CaesarCipher.encrypt;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Tests the {@link CaesarCipher} class.
 */
public class CaesarCipherTest
{
    @Test(dataProvider = "knownValuesProvider")
    public void testKnownValues(String cipherText, String text, int shift)
    {
        assertThat(cipherText).isEqualTo(encrypt(text, shift));
    }

    @Test(dataProvider = "testInverseProvider")
    public void testInverse(String text, int shift)
    {
        assertThat(text).isEqualTo(encrypt(encrypt(text, shift), -shift));
    }

    @DataProvider
    private static Object[][] testInverseProvider()
    {
        final Random random = new Random();

        // Create a long list of different classes of random strings "mapped" to
        // a random shift amount, e.g., an array of
        //
        //     { "random string", 2282 }
        //
        return Stream.<Function<Integer, String>>of(
                RandomStringUtils::random,
                RandomStringUtils::randomAscii,
                RandomStringUtils::randomAlphanumeric,
                RandomStringUtils::randomAlphabetic,
                RandomStringUtils::randomNumeric)
                // Create a list of random strings of each type
                .flatMap(s -> IntStream.range(0, 100).mapToObj(i -> s.apply(50)))
                // Pair each random string with a random shift amount
                .map(string -> new Object[] { string, random.nextInt() })
                .toArray(Object[][]::new);
    }

    @DataProvider
    private static Object[][] knownValuesProvider()
    {
        final String text = "the quick red fox jumped over the lazy brown dog "
                + "0123456789 !@#$%^&*() "
                + "THE QUICK RED FOX JUMPED OVER THE LAZY BROWN DOG.";

        final List<String> cipherText = Arrays.asList(
                text,
                "uif rvjdl sfe gpy kvnqfe pwfs uif mbaz cspxo eph 0123456789 !@#$%^&*() UIF RVJDL SFE GPY KVNQFE PWFS UIF MBAZ CSPXO EPH.",
                "vjg swkem tgf hqz lworgf qxgt vjg ncba dtqyp fqi 0123456789 !@#$%^&*() VJG SWKEM TGF HQZ LWORGF QXGT VJG NCBA DTQYP FQI.",
                "wkh txlfn uhg ira mxpshg ryhu wkh odcb eurzq grj 0123456789 !@#$%^&*() WKH TXLFN UHG IRA MXPSHG RYHU WKH ODCB EURZQ GRJ.",
                "xli uymgo vih jsb nyqtih sziv xli pedc fvsar hsk 0123456789 !@#$%^&*() XLI UYMGO VIH JSB NYQTIH SZIV XLI PEDC FVSAR HSK.",
                "ymj vznhp wji ktc ozruji tajw ymj qfed gwtbs itl 0123456789 !@#$%^&*() YMJ VZNHP WJI KTC OZRUJI TAJW YMJ QFED GWTBS ITL.",
                "znk waoiq xkj lud pasvkj ubkx znk rgfe hxuct jum 0123456789 !@#$%^&*() ZNK WAOIQ XKJ LUD PASVKJ UBKX ZNK RGFE HXUCT JUM.",
                "aol xbpjr ylk mve qbtwlk vcly aol shgf iyvdu kvn 0123456789 !@#$%^&*() AOL XBPJR YLK MVE QBTWLK VCLY AOL SHGF IYVDU KVN.",
                "bpm ycqks zml nwf rcuxml wdmz bpm tihg jzwev lwo 0123456789 !@#$%^&*() BPM YCQKS ZML NWF RCUXML WDMZ BPM TIHG JZWEV LWO.",
                "cqn zdrlt anm oxg sdvynm xena cqn ujih kaxfw mxp 0123456789 !@#$%^&*() CQN ZDRLT ANM OXG SDVYNM XENA CQN UJIH KAXFW MXP.",
                "dro aesmu bon pyh tewzon yfob dro vkji lbygx nyq 0123456789 !@#$%^&*() DRO AESMU BON PYH TEWZON YFOB DRO VKJI LBYGX NYQ.",
                "esp bftnv cpo qzi ufxapo zgpc esp wlkj mczhy ozr 0123456789 !@#$%^&*() ESP BFTNV CPO QZI UFXAPO ZGPC ESP WLKJ MCZHY OZR.",
                "ftq cguow dqp raj vgybqp ahqd ftq xmlk ndaiz pas 0123456789 !@#$%^&*() FTQ CGUOW DQP RAJ VGYBQP AHQD FTQ XMLK NDAIZ PAS.",
                "gur dhvpx erq sbk whzcrq bire gur ynml oebja qbt 0123456789 !@#$%^&*() GUR DHVPX ERQ SBK WHZCRQ BIRE GUR YNML OEBJA QBT.",
                "hvs eiwqy fsr tcl xiadsr cjsf hvs zonm pfckb rcu 0123456789 !@#$%^&*() HVS EIWQY FSR TCL XIADSR CJSF HVS ZONM PFCKB RCU.",
                "iwt fjxrz gts udm yjbets dktg iwt apon qgdlc sdv 0123456789 !@#$%^&*() IWT FJXRZ GTS UDM YJBETS DKTG IWT APON QGDLC SDV.",
                "jxu gkysa hut ven zkcfut eluh jxu bqpo rhemd tew 0123456789 !@#$%^&*() JXU GKYSA HUT VEN ZKCFUT ELUH JXU BQPO RHEMD TEW.",
                "kyv hlztb ivu wfo aldgvu fmvi kyv crqp sifne ufx 0123456789 !@#$%^&*() KYV HLZTB IVU WFO ALDGVU FMVI KYV CRQP SIFNE UFX.",
                "lzw imauc jwv xgp bmehwv gnwj lzw dsrq tjgof vgy 0123456789 !@#$%^&*() LZW IMAUC JWV XGP BMEHWV GNWJ LZW DSRQ TJGOF VGY.",
                "max jnbvd kxw yhq cnfixw hoxk max etsr ukhpg whz 0123456789 !@#$%^&*() MAX JNBVD KXW YHQ CNFIXW HOXK MAX ETSR UKHPG WHZ.",
                "nby kocwe lyx zir dogjyx ipyl nby futs vliqh xia 0123456789 !@#$%^&*() NBY KOCWE LYX ZIR DOGJYX IPYL NBY FUTS VLIQH XIA.",
                "ocz lpdxf mzy ajs ephkzy jqzm ocz gvut wmjri yjb 0123456789 !@#$%^&*() OCZ LPDXF MZY AJS EPHKZY JQZM OCZ GVUT WMJRI YJB.",
                "pda mqeyg naz bkt fqilaz kran pda hwvu xnksj zkc 0123456789 !@#$%^&*() PDA MQEYG NAZ BKT FQILAZ KRAN PDA HWVU XNKSJ ZKC.",
                "qeb nrfzh oba clu grjmba lsbo qeb ixwv yoltk ald 0123456789 !@#$%^&*() QEB NRFZH OBA CLU GRJMBA LSBO QEB IXWV YOLTK ALD.",
                "rfc osgai pcb dmv hskncb mtcp rfc jyxw zpmul bme 0123456789 !@#$%^&*() RFC OSGAI PCB DMV HSKNCB MTCP RFC JYXW ZPMUL BME.",
                "sgd pthbj qdc enw itlodc nudq sgd kzyx aqnvm cnf 0123456789 !@#$%^&*() SGD PTHBJ QDC ENW ITLODC NUDQ SGD KZYX AQNVM CNF.");

        return IntStream
                .range(-cipherText.size() + 1, cipherText.size())
                .mapToObj(
                        shiftAmount -> new Object[] {
                                cipherText.get((shiftAmount + 26) % 26),
                                text,
                                shiftAmount })
                .toArray(Object[][]::new);
    }
}
