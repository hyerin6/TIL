package designPattern.factoryMethod;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 팩토리 메서드 패턴 >> 오버라이드된 메서드가 객체를 반환하는 패턴 
		// 의존 역전 원칙(DIP)을 활용하고 있다. 

		// 팩토리 메서드를 보유한 객체들 생성 
		Animal dog = new Dog();
		Animal cat = new Cat();

		// 팩토리 메서드가 반환하는 객체들  
		AnimalToy ball = dog.getToy();
		AnimalToy tower = cat.getToy();

		// 팩토리 메서드가 반환한 객체들을 사용  
		ball.identify();
		tower.identify();

	}

}
