package net.skhu.exercise.list;

public class Example7 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Product product1 = new Product(1, "볼펜", "학용품"); 
		Product product2 = new Product(2, "연필", "학용품"); 
		Product product3 = new Product(1, "볼펜", "사무용품");
		Product product4 = new Product(1, null, "학용품"); 
		Product product5 = new Product(1, "볼펜", null);
		Product product6 = new Product(1, "볼펜", "학용품");

		System.out.println(product1.equals(null)); 
		System.out.println(product1.equals(product2)); 
		System.out.println(product1.equals(product3));
		System.out.println(product1.equals(product4));
		System.out.println(product1.equals(product5)); 
		System.out.println(product1.equals(product6)); 
		System.out.println(product1.hashCode() == product6.hashCode());
	}

}
