package com.java.oop1;

public class Strongbox {
	private long amount;

	public Strongbox(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return this.amount;
	}

	public void plusAmount(long amount) {
		this.amount += amount;
	}
}
