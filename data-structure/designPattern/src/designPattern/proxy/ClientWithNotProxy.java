package designPattern.proxy;

public class ClientWithNotProxy {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// 프록시를 이용하지 않은 호출  
		Service s = new Service();
		System.out.println(s.runSomething());
	
	}

}
