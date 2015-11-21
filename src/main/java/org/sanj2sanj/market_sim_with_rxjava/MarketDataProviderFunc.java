package org.sanj2sanj.market_sim_with_rxjava;

import org.sanj2sanj.market_sim_with_rxjava.domain.IMarketValue;
import org.sanj2sanj.market_sim_with_rxjava.domain.MarketValue;
import rx.functions.Func1;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simulates a marker data publisher that will iterate a list of given
 * instruments and publish random prices between 5.5 and 10.5.
 *
 * @author sanj@email.com
 *
 */
public class MarketDataProviderFunc implements Func1<Long, IMarketValue> {

	private static final Logger log = Logger.getLogger(MarketDataProviderFunc.class.getName());
	final DecimalFormat twoDp = new DecimalFormat("#.##");

	private final String name;
	private final List<String> instruments;
	private AtomicInteger lastInstrumentPublishedIndex = new AtomicInteger(0);

	public MarketDataProviderFunc(String name, String[] instrumentList) {
		this.name = name;
		instruments = Arrays.asList(instrumentList);
		log.log(Level.INFO, "Created {0} for instruments {1}", new Object[] { name, instruments });
	}

	@Override
	public IMarketValue call(Long t) {

		double price = Double.valueOf(twoDp.format(ThreadLocalRandom.current().nextDouble(5.5, 10.5)));

		// Get the next instrument to publish, if we are at the end of the list, start from the beginning
		String instrument = instruments
				.get(lastInstrumentPublishedIndex.updateAndGet(i -> (i + 1 == instruments.size()) ? 0 : i + 1));

		IMarketValue mv = MarketValue.from(name, instrument, price);
		log.log(Level.FINER, "Published {0} ", new Object[] { mv });
		return mv;

	}

}