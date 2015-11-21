package org.sanj2sanj.market_sim_with_rxjava;

import org.junit.Test;
import org.sanj2sanj.market_sim_with_rxjava.domain.IMarketValue;
import org.sanj2sanj.market_sim_with_rxjava.domain.MarketValue;

import static org.junit.Assert.*;

public class MarketValueTest {

	@Test
	public void test_average_of_two_market_values() {
		IMarketValue mv = MarketValue.from("TEST Provider 2", "TEST Stock", 1.0);
		IMarketValue mv2 = MarketValue.from("TEST Provider", "TEST Stock", 1.0);
		assertTrue(mv.avg(mv2).getValue() == 1.0);
	}
	
	@Test
	public void test_average_of_two_market_values_part_2() {
		IMarketValue mv = MarketValue.from("TEST Provider 2", "TEST Stock", 5.0);
		IMarketValue mv2 = MarketValue.from("TEST Provider", "TEST Stock", 10.0);
		assertTrue(mv.avg(mv2).getValue() == 7.5);
	}

	@Test
	public void test_create_object() {
		IMarketValue mv = MarketValue.from("TEST Provider", "TEST Stock", 1.0);
		assertEquals(mv.getProviderId(), "TEST Provider");
		assertEquals(mv.getName(), "TEST Stock");
		assertEquals(mv.getValue(), 1.0, 0);
		assertTrue(mv.equals(mv));
	}

	@Test
	public void test_equals_with_different_objects() {
		IMarketValue mv = MarketValue.from("TEST Provider 2", "TEST Stock", 1.0);
		Object object = new Object();
		assertFalse(mv.equals(object));
		assertFalse(mv.hashCode() == object.hashCode());
		assertFalse(mv.equals(null));
	}

	@Test
	public void test_equals_with_different_prices() {
		IMarketValue mv = MarketValue.from("TEST Provider", "TEST Stock", 2.0);
		IMarketValue mv2 = MarketValue.from("TEST Provider", "TEST Stock", 1.0);
		assertFalse("Prices are different", mv.equals(mv2));
		assertFalse("Prices are different", mv.hashCode() == mv2.hashCode());
	}

	@Test
	public void test_equals_with_different_provider_ids() {
		IMarketValue mv = MarketValue.from("TEST Provider 2", "TEST Stock", 1.0);
		IMarketValue mv2 = MarketValue.from("TEST Provider", "TEST Stock", 1.0);
		assertFalse("provider ids are different", mv.equals(mv2));
		assertFalse("provider are different", mv.hashCode() == mv2.hashCode());
	}

	@Test
	public void test_equals_with_different_stocks() {
		IMarketValue mv = MarketValue.from("TEST Provider", "TEST Stock 2", 1.0);
		IMarketValue mv2 = MarketValue.from("TEST Provider", "TEST Stock", 1.0);
		assertFalse("Stocks are different", mv.equals(mv2));
		assertFalse("Stocks are different", mv.hashCode() == mv2.hashCode());
	}

	@Test
	public void test_equals_with_nulls() {
		IMarketValue mv = MarketValue.from("TEST Provider 2", "TEST Stock", 1.0);

		IMarketValue mv2 = MarketValue.from("TEST Provider 2", null, 1.0);
		assertFalse(mv.equals(mv2));
		assertFalse(mv2.equals(mv));
		assertFalse(mv.hashCode() == mv2.hashCode());

		IMarketValue mv3 = MarketValue.from(null, "TEST Stock", 1.0);
		assertFalse(mv.equals(mv3));
		assertFalse(mv3.equals(mv));
		assertFalse(mv.hashCode() == mv3.hashCode());
	}

	@Test
	public void test_toString() {
		IMarketValue mv = MarketValue.from("TEST Provider 2", "TEST Stock", 1.0);
		assertEquals("MarketValue [providerId=TEST Provider 2, name=TEST Stock, value=1.0]", mv.toString());
	}

	@Test
	public void test_two_objects_are_equal() {
		IMarketValue mv = MarketValue.from("TEST Provider", "TEST Stock", 1.0);
		IMarketValue mv2 = MarketValue.from("TEST Provider", "TEST Stock", 1.0);
		assertTrue(mv.equals(mv2));
		assertTrue(mv.hashCode() == mv2.hashCode());
	}

}
