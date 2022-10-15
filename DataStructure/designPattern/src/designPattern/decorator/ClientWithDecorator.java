package designPattern.decorator;

public class ClientWithDecorator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		IService decorator = new Decorator();
		System.out.println(decorator.runSomething());
		

	}

}
