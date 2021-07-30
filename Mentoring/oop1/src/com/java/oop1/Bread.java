package com.java.oop1;

public class Bread {
	private Flavor flavor;

	public Bread(Flavor flavor) {
		this.flavor = flavor;
	}

	public String toString() {
		return "flavor=" + this.flavor;
	}
}
