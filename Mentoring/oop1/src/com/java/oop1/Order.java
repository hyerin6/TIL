package com.java.oop1;

import java.util.HashMap;
import java.util.Map;

public class Order {
	private Map<Flavor, Integer> order = new HashMap();

	public Order() {
	}

	public Map<Flavor, Integer> getOrder() {
		return this.order;
	}

	public void addOrder(Flavor flavor, int n) {
		this.order.put(flavor, n);
	}

	public long getAllAmount() {
		return this.order.entrySet().stream().mapToLong((x) -> {
			return (long)(((Flavor)x.getKey()).getAmount() * (Integer)x.getValue());
		}).sum();
	}
}

