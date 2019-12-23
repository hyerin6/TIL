package stack;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		 * JVM은 객체 멤버 메소드를 스태틱 영역에 단 하나만 보유한다. 
		 * 그리고 눈에 보이지는 않지만 메서드를 호출할 때 
		 * 객체 자신을 나타내는 this 객체 참조 변수를 넘긴다.
		 */

		펭귄 펭귄 = new 펭귄();
		펭귄.method();


		// (1) 상위 클래스 타입의 객체 참조 변수를 사용하더라도 하위 클래스에서 오버라이딩한 메서드가 호출된다.   
		// (2) 객체를 저장하고 있는 객체 참조 변수를 복사하는 경우 -> Call By Reference(참조에 의한 호출)
		//     아래 코드의 결과는 age가 펭수, 뽀로로 둘 다 2살로 출력된다. 
		동물 펭수 = new 펭귄();
		동물 뽀로로 = 펭수;

		펭수.age = 1;
		뽀로로.age = 2;

		펭수.method();
		뽀로로.method();

	}

}
