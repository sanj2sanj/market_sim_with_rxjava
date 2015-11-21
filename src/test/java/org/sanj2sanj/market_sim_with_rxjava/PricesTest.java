package org.sanj2sanj.market_sim_with_rxjava;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sanj2sanj.market_sim_with_rxjava.domain.IMarketValue;
import org.sanj2sanj.market_sim_with_rxjava.domain.MarketValue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PricesTest {

    public static final String REUTERS = "Reuters";
    public static final String BLOOMBERG = "Bloomberg";
    private static final String TEST_PRICES_FILE = File.separator + "prices.txt";
    private static final String tempDir = System.getProperty("java.io.tmpdir") + "PricesTest";
    private Map<String, IMarketValue> reutersMarketData;
    private Map<String, IMarketValue> bloombergMarketData;

    private Map<String, IMarketValue> expected;

    private List<String> expectedFileOutput = Arrays.asList("AAA,6.65", "BBB,8.0", "FFF,7.0");

    static private void createReadOnlyFile(String fileName) throws IOException {
        File f = new File(fileName);
        try (FileWriter fw = new FileWriter(f)) {
            fw.append("blocked");
            fw.flush();
        }
        f.setReadOnly();
        f.deleteOnExit();

        Logger.getAnonymousLogger().info("Created read only file " + fileName);
    }

    @After
    public void after() {
        File dir = new File(tempDir);
        File[] fileList = dir.listFiles();
        if (fileList != null && fileList.length > 0) {
            for (File file : fileList) {
                file.delete();
            }
        }
        dir.delete();
    }

    @Before
    public void before() throws IOException {
        Files.createDirectories(Paths.get(tempDir));
    }

    @Before
    public void beforeTest() {
        reutersMarketData = new HashMap<>();
        bloombergMarketData = new HashMap<>();
        expected = new HashMap<>();

        reutersMarketData.put("AAA", MarketValue.from(REUTERS, "AAA", 6.3));
        reutersMarketData.put("FFF", MarketValue.from(REUTERS, "FFF", 7.0));

        bloombergMarketData.put("AAA", MarketValue.from(BLOOMBERG, "AAA", 7.0));
        bloombergMarketData.put("BBB", MarketValue.from(BLOOMBERG, "BBB", 8.0));

        expected.put("AAA", MarketValue.from("Bloomberg/Reuters", "AAA", 6.65));
        expected.put("FFF", MarketValue.from(REUTERS, "FFF", 7.0));
        expected.put("BBB", MarketValue.from(BLOOMBERG, "BBB", 8.0));
    }

    @Test(expected = CantWritePricesRuntimeException.class)
    public void test_exception_when_cant_write_to_files() throws IOException {
        String fileName = tempDir + TEST_PRICES_FILE;
        Prices prices = new Prices(reutersMarketData, bloombergMarketData);

        createReadOnlyFile(fileName);
        createReadOnlyFile(Prices.getFallbackFileName(fileName));
        prices.writeToFile(fileName);

    }

    @Test
    // The file writer should output in the following format and the entries
    // should be in alphabetical order.
    // Each new set of values should create a new file and there should be only
    // one entry for each instrument.
    public void test_prices_are_aggregated() {

        Prices prices = new Prices(reutersMarketData, bloombergMarketData);
        assertEquals(expected, prices.asMap());

        // However, if Reuters then publishes: Reuters – BBB – 7.0
        reutersMarketData.put("BBB", MarketValue.from(REUTERS, "BBB", 7.0));

        // the next aggregator output should contain: BBB – 7.5
        expected.put("BBB", MarketValue.from("Bloomberg/Reuters", "BBB", 7.5));
        prices = new Prices(reutersMarketData, bloombergMarketData);
        assertEquals(expected, prices.asMap());

    }

    @Test
    public void test_prices_are_written_to_fallback_file() throws IOException {
        String fileName = tempDir + TEST_PRICES_FILE + "_fallback";
        Prices prices = new Prices(reutersMarketData, bloombergMarketData);

        createReadOnlyFile(fileName);

        prices.writeToFile(fileName);

        String fallbackFileName = Prices.getFallbackFileName(fileName);
        assertTrue(fallbackFileName + " should exist", new File(fallbackFileName).exists());

        List<String> actual = Files.readAllLines(Paths.get(fallbackFileName));
        assertEquals(expectedFileOutput, actual);

    }

    @Test
    public void test_prices_are_written_to_file() throws IOException {
        String fileName = tempDir + TEST_PRICES_FILE + "2";
        Prices prices = new Prices(reutersMarketData, bloombergMarketData);

        prices.writeToFile(fileName);

        List<String> actual = Files.readAllLines(Paths.get(fileName));
        assertEquals(expectedFileOutput, actual);
    }


}
