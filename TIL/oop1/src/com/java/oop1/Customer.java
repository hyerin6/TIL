package com.java.oop1;

public class Customer {
	private Bag bag;

	public Customer(long amount) {
		this.bag = new Bag(amount);
	}

	public Bag getBag() {
		return bag;
	}

	public void buy(Order order) {
		long allAmount = order.getAllAmount();
		bag.minAmount(allAmount);
	}

	public void receive(BreadBox breadBox) {
		bag.hold(breadBox);
	}


}
