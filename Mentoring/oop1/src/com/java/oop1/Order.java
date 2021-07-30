package com.java.oop1;

import java.util.HashMap;
import java.util.Map;

public class Order {
	private Map<Flavor, Integer> order = new HashMap();

	public Order() {
		this.order.put(Flavor.CREAM, 1);
		this.order.put(Flavor.RED_BEAN, 1);
		this.order.put(Flavor.SPICY, 1);
	}

	public Map<Flavor, Integer> getOrder() {
		return this.order;
	}

	public long getAllAmount() {
		return order.entrySet()
			.stream()
			.mapToLong(x -> x.getKey().getAmount() * x.getValue())
			.sum();
	}
}

