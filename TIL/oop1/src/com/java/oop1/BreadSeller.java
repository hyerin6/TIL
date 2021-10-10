package com.java.oop1;

import java.util.Map;

public class BreadSeller implements Seller{

	private Strongbox strongbox;

	public BreadSeller(long amount) {
		this.strongbox = new Strongbox(amount);
	}

	public Strongbox getStrongbox() {
		return this.strongbox;
	}

	@Override
	public void sellTo(Customer customer, Order order) {
		customer.buy(order);
		long allAmount = order.getAllAmount();
		this.strongbox.plusAmount(allAmount);
	}

	public BreadBox makeBread(Order order) {
		BreadBox breadBox = new BreadBox();
		for(Map.Entry<Flavor, Integer> o : order.getOrder().entrySet()) {
			for(long i = 0; i < o.getValue(); ++i) {
				breadBox.addBread(new Bread(o.getKey()));
			}
		}
		return breadBox;
	}
}
