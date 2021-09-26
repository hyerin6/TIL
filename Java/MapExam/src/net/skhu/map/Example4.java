package net.skhu.map;

import java.util.HashMap;
import java.util.Map;

public class Example4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<Person, Integer> map = new HashMap<Person, Integer>();

		map.put(new Person("홍길동", 18), 101); 
		map.put(new Person("임꺽정", 19), 102);

		// Person 클래스에 equals, hashCode 메소드가 재정의 되어 있지 않기 때문에
		// key를 찾지 못하고 null을 출력한다.
		System.out.println(map.get(new Person("홍길동", 18))); 
		System.out.println(map.get(new Person("임꺽정", 19)));
	}

}
