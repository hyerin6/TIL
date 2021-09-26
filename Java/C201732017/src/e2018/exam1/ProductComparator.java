package e2018.exam1;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product>{
	@Override 
	public int compare(Product p1, Product p2) {
		int r = p1.name.compareTo(p2.name);
		if (r != 0) return r; 
		r = p1.price - p2.price; 
		if (r != 0) return r;
		return p1.id - p2.id;
	}
}
