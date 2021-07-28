package com.java.oop1;

public enum Flavor {
	CREAM(1000),
	RED_BEAN(1000),
	SPICY(1000);

	private int amount;

	private Flavor(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return this.amount;
	}
}

