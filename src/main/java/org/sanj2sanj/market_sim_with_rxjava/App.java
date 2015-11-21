package org.sanj2sanj.market_sim_with_rxjava;

import org.sanj2sanj.market_sim_with_rxjava.domain.IMarketValue;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

	private static final Logger log = Logger.getLogger(App.class.getName());

	private static final int TIMER_BETWEEN_FILE_SAVES_MS = 1000;
	private static final String DEFAULT_FILE_NAME = "prices.csv";
	private static final int MAX_NO_RETRIES = 25;

	
	public static void main(String[] args) {
		log.info(App.class.getSimpleName() + " starting up");
		MarketDataSource mds = new MarketDataSource();
		aggregate(mds.getLatestReutersPrices(), mds.getLatestBloombergPrices());
	}

	private static void aggregate(Map<String, IMarketValue> latestReutersPrices,
								  Map<String, IMarketValue> latestBloombergPrices) {
		while (true) {
			int retries = 0;

			while (retries < MAX_NO_RETRIES) {
				try {
					// Save the file every second
					Thread.sleep(TIMER_BETWEEN_FILE_SAVES_MS);
				} catch (InterruptedException e) {
					log.log(Level.WARNING, "Something interupted the main thread", e);
				}
				Prices prices = new Prices(latestReutersPrices, latestBloombergPrices);
				try {
					prices.writeToFile(DEFAULT_FILE_NAME);
				} catch (CantWritePricesRuntimeException e) {
					retries++;
					log.log(Level.INFO, "CantWritePricesException, already retried {0}/{1}", new Object[] { retries,
							MAX_NO_RETRIES });
					if (retries == MAX_NO_RETRIES) {
						log.log(Level.SEVERE, e.getMessage(), e);
						log.log(Level.SEVERE, "Could not write to {0} or {1}, prices are being dropped!!",
								new Object[] { DEFAULT_FILE_NAME, Prices.getFallbackFileName(DEFAULT_FILE_NAME) });
						System.exit(-1);
					}
				}
			}
		}
	}

}
