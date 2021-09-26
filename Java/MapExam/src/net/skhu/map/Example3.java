package net.skhu.map;

import java.util.HashMap;
import java.util.Map;

public class Example3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<Integer, Person> map = new HashMap<Integer, Person>(); 

		/* Map 의 key 는 hashCode 메소드를 재정의한 클래스만 등록될 수 있다. 
		   value는 상관없다. */ 
		map.put(101, new Person("홍길동", 18)); 
		map.put(102, new Person("임꺽정", 19)); 
		System.out.printf("%s %s %s\n", map.get(101), map.get(102), map.get(103));

		map.remove(102); 
		System.out.printf("%s %s %s\n", map.get(101), map.get(102), map.get(103));
	}

}
