package org.sanj2sanj.market_sim_with_rxjava;

import org.junit.Test;
import org.sanj2sanj.market_sim_with_rxjava.domain.IMarketValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class MarketDataProviderFuncTest {

	@Test
	public void test_market_data_is_returned_with_3_instruments() {

		final String providerId = "Bloomberg";
		final String[] instrumentArray = { "VOD.L", "BT.G", "AAT.N" };
		final Set<String> instrumentSet = new HashSet<>(Arrays.asList(instrumentArray));

		MarketDataProviderFunc mdf = new MarketDataProviderFunc(providerId, instrumentArray);

		for (long i = 0; i < 1000; i++) {
			IMarketValue mv = mdf.call(1L);
			assertTrue(mv.getProviderId().equals(providerId));
			assertTrue(instrumentSet.contains(mv.getName()));

			// You should generate random numbers (from 5.5 to 10.5) for the
			// values that the providers publish.
			assertTrue(mv.getValue() >= 5.5 && mv.getValue() <= 10.5);
		}

	}

	@Test
	public void test_market_data_is_returned_with_1_instrument() {

		final String providerId = "Bloomberg";
		final String[] instruments = { "VOD.L" };

		MarketDataProviderFunc mdf = new MarketDataProviderFunc(providerId, instruments);

		for (long i = 0; i < 1000; i++) {
			IMarketValue mv = mdf.call(1L);
			assertTrue(mv.getProviderId().equals(providerId));
			assertTrue(mv.getName().equals(instruments[0]));

			// You should generate random numbers (from 5.5 to 10.5) for the
			// values that the providers publish.
			assertTrue(mv.getValue() >= 5.5 && mv.getValue() <= 10.5);
		}
	}

}
