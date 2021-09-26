package net.skhu.map;

import java.util.HashMap; 
import java.util.Map;

public class Example5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Map<Person2, Integer> map = new HashMap<>(); 

		map.put(new Person2("홍길동", 18), 101);
		map.put(new Person2("임꺽정", 19), 102); 

		// Person2 클래스에 equals, hashCode 메소드가 재정의되어 있기 때문에 key값을 출력한다.
		System.out.println(map.get(new Person2("홍길동", 18)));
		System.out.println(map.get(new Person2("임꺽정", 19)));

	}

}
