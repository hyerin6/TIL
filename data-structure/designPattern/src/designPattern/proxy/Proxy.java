package designPattern.proxy;

public class Proxy implements IService{

	IService service1;
	
	public String runSomething() {
		System.out.println("호출에 대한 흐름 제어가 주목적, 반환 결과를 그대로 전달"); // 별도의 로직 수행 가능
		
		service1 = new Service(); // 실제 서비스에 대한 참조 변수를 갖는다.  
		return service1.runSomething(); 

	}

}
