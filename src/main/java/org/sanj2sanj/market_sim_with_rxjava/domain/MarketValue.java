package org.sanj2sanj.market_sim_with_rxjava.domain;

import java.text.DecimalFormat;

/*
 * Represents a price for a instrument
 */
public class MarketValue implements IMarketValue {

	private final DecimalFormat twoDp = new DecimalFormat("#.##");
	private String providerId;
	private String name;
	private double value;

	private MarketValue(String providerId, String name, double value) {
		super();
		this.providerId = providerId;
		this.name = name;
		this.value = value;
	}

	static public IMarketValue from(String providerId, String name, double value) {
		return new MarketValue(providerId, name, value);
	}

	@Override
	public IMarketValue avg(IMarketValue that) {
		double avgPrice = Double.valueOf(twoDp.format((value + that.getValue()) / 2));
		return new MarketValue(new StringBuilder(that.getProviderId()).append("/").append(getProviderId()).toString(), name, avgPrice);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarketValue other = (MarketValue) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (providerId == null) {
			if (other.providerId != null)
				return false;
		} else if (!providerId.equals(other.providerId))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getProviderId() {
		return providerId;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((providerId == null) ? 0 : providerId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "MarketValue [providerId=" + providerId + ", name=" + name + ", value=" + value + "]";
	}

}
