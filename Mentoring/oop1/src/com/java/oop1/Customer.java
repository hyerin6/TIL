package com.java.oop1;

public class Customer {
	private long amount;

	public Customer(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return this.amount;
	}

	private void minusAmount(long amount) {
		this.amount -= amount;
	}

	public long buy(Order order) {
		long sum = order.getAllAmount();
		this.minusAmount(sum);
		return sum;
	}
}
