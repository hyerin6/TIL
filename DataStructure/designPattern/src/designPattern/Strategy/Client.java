package designPattern.Strategy;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/* 전략 패턴 "클라이언트가 전략을 생성해 전략을 실행할 컨텍스트에 주입하는 패턴"
		 * >> 개방 폐쇄 원칙(OCP)과 의존 역전 원칙(DIP)이 적용된 것을 짐작할 수 있다. 
		 */
		
		Strategy strategy = null;
		Soldier rambo = new Soldier();
		
		// 총을 람보에게 전달해서 전투를 수행하게 한다. 
		strategy = new StrategyGun();
		rambo.runContext(strategy);
		
		System.out.println();
		
		// 검을 람보에게 전달해서 전투를 수행하게 한다. 
		strategy = new StrategySword();
		rambo.runContext(strategy);

	}

}
