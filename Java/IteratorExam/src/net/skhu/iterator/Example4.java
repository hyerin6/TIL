package net.skhu.iterator;

import java.util.ArrayList; 
import java.util.Collection;

/* 
컬렉션 객체를 for 문으로 탐색하는 도중에, 
그 컬렉션 객체가 수정되는 메소드(add, remove, addAll, clear 등)가 호출되면 에러가 발생한다.
-> 기존 index 값과 remove 호출로 인해 줄어든 size 값이 맞지 않기 때문에 예외가 발생한다. 
*/

public class Example4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Collection<Integer> c = new ArrayList<Integer>();

		for (int i = 0; i < 10; ++i)
			c.add(i);

		for (int i : c)
			System.out.print(i + " ");
		System.out.println();

		for (int i : c) // ConcurrentModificationException
			if (i % 2 == 0)
				c.remove(i);

		for (int i : c) 
			System.out.print(i + " ");
	}

}
