package designPattern.templateMethod;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/* 템플릿 메서드 패턴 
		 * "상위 클래스의 견본 메서드에서 하위 클래스가 오버라이딩한 메서드를 호출하는 패턴"
		 * >> 의존 역전 원칙(DIP)을 활용하고 있다. 
		 */

		Animal dog = new Dog();
		Animal cat = new Cat();

		dog.playWithOwner();

		System.out.println();

		cat.playWithOwner();


	}

}
