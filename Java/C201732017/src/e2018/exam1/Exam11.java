package e2018.exam1;

import java.util.Arrays;

public class Exam11 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Product[] a = new Product[] { 
				new Product(1, "맥주", 3000), 
				new Product(2, "맥주", 2000), 
				new Product(3, "맥주", 3000), 
				new Product(4, "막걸리", 1000), 
				new Product(5, "소주", 2000) 
		};

		Arrays.sort(a, new ProductComparator()); 
		for (Product p : a) System.out.println(p);
	} 
}