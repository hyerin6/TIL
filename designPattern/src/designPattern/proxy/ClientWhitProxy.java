package designPattern.proxy;

public class ClientWhitProxy {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 프록시를 이용한 호출
		IService proxy = (IService) new Proxy();
		System.out.println(proxy.runSomething());

		/* 프록시 패턴의 포인트 
		 * (1) 대리자는 실제 서비스와 같은 이름의 메서드를 구현한다. 이때 인터페이스를 이용한다. 
		 * (2) 대리자는 실제 서비스에 대한 참조 변수를 갖는다. (합성)
		 * (3) 대리자는 실제 서비스와 같은 이름을 가진 메서드를 호출하고 그 값을 클라이언트에게 돌려준다.   
		 * (4) 대이자는 실제 서비스의 메서드 호출 전후에 별도의 로직을 수행할 수 있다.   
		 */		

	}

}
