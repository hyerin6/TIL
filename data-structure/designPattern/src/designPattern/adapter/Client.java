package designPattern.adapter;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ServiceA a = new ServiceA();
		ServiceB b = new ServiceB();
		
		a.runServiceA();
		b.runServiceB();
	
	}

}
