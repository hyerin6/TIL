package com.java.oop1;

public class Bag {

	private Long amount;
	private BreadBox breadBox;

	public Bag(long amount) {
		this.amount = amount;
	}

	public Long getAmount() {
		return amount;
	}

	public BreadBox getBreadBox() {
		return breadBox;
	}

	public void minAmount(long amount) {
		this.amount -= amount;
	}

	public void hold(BreadBox breadBox) {
		this.breadBox = breadBox;
	}

}
