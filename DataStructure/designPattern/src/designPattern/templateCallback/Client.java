package designPattern.templateCallback;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Soldier rambo = new Soldier();

		/*
		rambo.runContext(new Strategy() {
			@Override
			public void runStrategy() {
				System.out.println("탕, 타당, 타다당");
			}
		});
		System.out.println();

		rambo.runContext(new Strategy() {
			@Override
			public void runStrategy() {
				System.out.println("챙.. 채채챙 챙챙");
			}
		});
		 */ 
		// >> 중복된 코드가 많아 보인다. 수정해보자. 

		rambo.runContext("탕, 타당, 타다당");
		System.out.println();

		rambo.runContext("챙.. 채채챙 챙챙");

		/* 템플릿 콜백 패턴 "전략을 익명 내부 클래스로 구현한 전략 패턴"  
		 * 스프링은 이런 형식으로 리택터링된 템플릿 콜백 패턴을 DI에 적극 활용하고 있다. 
		 * >> 개발 폐쇄 원칙(OCP)과 의존 역전 원칙(DIP)이 적용된 설계 패턴이다. 
		 */

	}

}
