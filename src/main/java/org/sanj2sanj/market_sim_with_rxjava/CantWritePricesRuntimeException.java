package org.sanj2sanj.market_sim_with_rxjava;

import java.io.IOException;

public class CantWritePricesRuntimeException extends IOException {

	private static final long serialVersionUID = -456334061510779666L;

	public CantWritePricesRuntimeException(IOException e1) {
		super(e1);
	}

}
