package net.skhu.exercise.list;

import java.util.ArrayList; 
import java.util.Iterator;
import java.util.List;

public class Example2 {
	static void add(List<Integer> list, int count) { 
		// list 목록에 0부터 count 개의 정수를 추가한다. 
		for(int i = 0;  i < count; i++) 
			list.add(i);
	}

	static void removeEven(List<Integer> list) { 
		// list 목록에서 짝수를 전부 제거한다. 
		Iterator<Integer>  it = list.iterator();

		while(it.hasNext()) {
			if(it.next() % 2 == 0)
				it.remove();
		}

		/*
		 * type 정보는 소스코드에만 의존하고 그 실제 객체의 타입은 검사하지 않는다. 

		 * Object i = 3;
  		  >> autoboxing & unboxing, up-casting & down-casting 
  		  	 위 코드는 에러가 발생하지 않는다.

		 * Object 타입에 대한 나머지 연산자가 정의되어있지 않다.
  		   Iterator 타입을 정의해야 한다. -> <Integer> 제네릭 문법
  		   혹은 i에 타입 캐스팅을 해주면됨
		 */

	}

	static void addEven(List<Integer> list) { 
		// list 목록의 홀수 값들 사이에 짝수 값을 삽입한다. 
		int count = list.size() * 2;
		for (int i = 0; i < count; i += 2)
			list.add(i, i);
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();

		add(list, 10); 
		System.out.println(list.toString());

		removeEven(list);
		System.out.println(list.toString()); 

		addEven(list); 
		System.out.println(list.toString());

	}

}
