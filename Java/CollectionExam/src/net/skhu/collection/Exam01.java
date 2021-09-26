package net.skhu.collection;

import java.util.ArrayList;
import java.util.Collection;

public class Exam01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// ArrayList클래스는 Collection 인터페이스를 구현하였다.
		// 자식 타입의 객체에 대한 참조를 부모 타입의 변수에 대입하는 것은 Ok
		Collection<String> c = new ArrayList<String>();

		// 부모 타입의 변수로 메소드를 호출하여 다형성이다.
		c.add("one");
		c.add("two");
		c.add("three");

		// 배열뿐만아니라 Collection 구현 객체들도 for문으로 탐색이 가능하다.
		for (String s : c) 
			System.out.printf("%s ", s);

	}

}
