package org.sanj2sanj.market_sim_with_rxjava.domain;

public interface IMarketValue {

	/*
	 * Returns a market value that is the average of this and that
	 * 
	 * @param that the other market value you want to average with
	 */
	IMarketValue avg(IMarketValue that);

	String getName();

	String getProviderId();

	double getValue();

}
