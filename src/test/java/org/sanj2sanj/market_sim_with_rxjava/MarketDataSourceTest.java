package org.sanj2sanj.market_sim_with_rxjava;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MarketDataSourceTest extends MarketDataSource {

	@Test
	public void test_we_get_prices_for_instruments() throws InterruptedException {
		MarketDataSource mds = new MarketDataSource();
		Thread.sleep(500);
		assertTrue(mds.getLatestReutersPrices().size() > 0);
		assertTrue(mds.getLatestBloombergPrices().size() > 0);
	}

}
