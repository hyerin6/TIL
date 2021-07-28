package com.java.oop1;

import java.util.ArrayList;
import java.util.List;

public class BreadBox {
	private List<Bread> breadBox = new ArrayList();

	public BreadBox() {
	}

	public List<Bread> getBreadBox() {
		return this.breadBox;
	}

	public void addBread(Bread bread) {
		this.breadBox.add(bread);
	}
}