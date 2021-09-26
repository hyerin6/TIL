package net.skhu.exercise.list;

import java.util.Objects;

public class Product {
	int id; 
	String name;
	String category;

	public Product(int id, String name, String category) {
		this.id = id; 
		this.name = name; 
		this.category = category;
	}

	@Override 
	public int hashCode() { 
		return Objects.hash(this.id, this.name, this.category);
	}

	@Override 
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj instanceof Product == false) return false;

		Product p = (Product)obj;
		return Objects.equals(this.name, p.name) &&
				Objects.equals(this.category, p.category) && 
				this.id == p.id;
	}
}
