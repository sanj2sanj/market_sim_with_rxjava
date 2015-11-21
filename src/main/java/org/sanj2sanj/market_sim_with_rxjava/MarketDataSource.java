package org.sanj2sanj.market_sim_with_rxjava;

import org.sanj2sanj.market_sim_with_rxjava.domain.IMarketValue;
import rx.Observable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simulates market data ticks from Reuters and Bloomberg. Keeps a map of name to market data object received for each
 * market data source. Clients of this class can ask for these and aggregate (@see org.sanj2sanj.Prices) as appropriate
 * 
 * This class is implemented using @see <a href="https://github.com/ReactiveX/RxJava"> RxJava </a> and <a href="http://reactivex.io/"> reactivex.io </a> 
 *
 */
public class MarketDataSource {

	final static String[] reutersInstrumentList = ("AAA\n" + "BBB\n" + "CCC\n" + "DDD\n" + "EEE\n" + "FFF").split("\n");
	final static String[] bloombergInstrumentList = ("AAA\n" + "BBB\n" + "CCC\n" + "GGG\n" + "HHH\n" + "III\n" + "JJJ")
			.split("\n");
	private static final int TIME_BETWEEN_PRICE_TICKS_MS = 150;
	private static final Logger log = Logger.getLogger(MarketDataSource.class.getName());
	private final Map<String, IMarketValue> latestReutersPrices = new ConcurrentHashMap<>();
	private final Map<String, IMarketValue> latestBloombergPrices = new ConcurrentHashMap<>();

	public MarketDataSource() {
		setup("reuters", latestReutersPrices, reutersInstrumentList);
		setup("bloomberg", latestBloombergPrices, bloombergInstrumentList);
	}

	private void setup(String name, Map<String, IMarketValue> targetMap, String[] instruments) {
		Observable<Long> marketDataTicker = Observable.interval(TIME_BETWEEN_PRICE_TICKS_MS, TimeUnit.MILLISECONDS);
		Observable<IMarketValue> marketDataTicks = marketDataTicker.map(new MarketDataProviderFunc(name, instruments));
		marketDataTicks.map(m -> targetMap.put(m.getName(), m))
		.subscribe(s -> sleepForOneSecondDuringMessageProcessing());	
	}

	public Map<String, IMarketValue> getLatestReutersPrices() {
		return latestReutersPrices;
	}

	public Map<String, IMarketValue> getLatestBloombergPrices() {
		return latestBloombergPrices;
	}

	private Object sleepForOneSecondDuringMessageProcessing() {
		try {
			// Simulate a one second delay during message processing loop
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.log(Level.INFO, "Something interrupted the main thread", e);
		}
		return null;
	}

}
