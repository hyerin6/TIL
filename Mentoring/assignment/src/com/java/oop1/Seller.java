package com.java.oop1;

public class Seller {
	private Strongbox strongbox;

	public Seller(long amount) {
		this.strongbox = new Strongbox(amount);
	}

	public Strongbox getStrongbox() {
		return this.strongbox;
	}

	public void sellTo(Customer customer, Order order) {
		this.strongbox.plusAmount(customer.buy(order));
	}

	public BreadBox makeBread(Order order) {
		BreadBox breadBox = new BreadBox();
		order.getOrder().entrySet().stream().forEach((x) -> {
			breadBox.addBread(new Bread((Flavor)x.getKey()));
		});
		return breadBox;
	}
}
