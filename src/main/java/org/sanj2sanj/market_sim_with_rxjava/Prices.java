package org.sanj2sanj.market_sim_with_rxjava;

import org.sanj2sanj.market_sim_with_rxjava.domain.IMarketValue;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 *  Represents a list of prices that has been created from Reuters and Bloomberg.
 */
public class Prices {

    private static final Logger LOGGER = Logger.getLogger(Prices.class.getName());
    private final static String FALLBACK_FILE_POST_FIX = "_fallback";
	private final Map<String, IMarketValue> prices;
	private final String template = "%s,%s%n";

    public Prices(Map<String, IMarketValue> latestReutersPrices, Map<String, IMarketValue> latestBloombergPrices) {

        prices = Stream.of(latestReutersPrices, latestBloombergPrices).map(Map::entrySet).flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, IMarketValue::avg));
	}

    static public String getFallbackFileName(String fileName) {
        return fileName + FALLBACK_FILE_POST_FIX;
    }

	public Map<String, IMarketValue> asMap() {
		return prices;
	}

	/**
	 * Every time the file writer receives a new set of values it should create
	 * a new version of the file. Should the file writer not be able to write to
	 * the output file, then an alternate filename {fallBackFileName} should be
	 * used, but once the file is once more accessible it should return to the
	 * original filename. It should also not create many files and only a new
	 * file when a file is not accessible.
	 *
	 * @param fileName
	 * @throws CantWritePricesRuntimeException
	 */
	public void writeToFile(String fileName) throws CantWritePricesRuntimeException {
		try {
			writeToPrimaryFile(fileName);
            LOGGER.log(Level.INFO, "{0} prices saved to {1}", new Object[]{prices.keySet().size(), fileName});
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            final String fallBackFileName = getFallbackFileName(fileName);
			try {
				writeToPrimaryFile(fallBackFileName);
                LOGGER.log(Level.WARNING, "Could not write to {0}, have written to {1}", new Object[]{fileName,
                        fallBackFileName });
			} catch (IOException e1) {
				throw new CantWritePricesRuntimeException(e1);
			}
		}
	}

	private void writeToPrimaryFile(String fileName) throws IOException {
        File f = new File(fileName);
        if (f.exists() && !f.canWrite()) {
            throw new IOException("Cannot write to " + f);
        }
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName, false)))) {
            Set<String> sortedSymbols = new TreeSet<String>(prices.keySet());
			sortedSymbols.forEach(s -> pw.write(String.format(template, prices.get(s).getName(), +prices.get(s)
					.getValue())));
		}
	}

}
