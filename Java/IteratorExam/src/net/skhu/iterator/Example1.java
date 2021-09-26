package net.skhu.iterator;

import java.util.ArrayList;
import java.util.Collection; 
import java.util.Iterator;

public class Example1 {
	public static void main(String[] args) {
		Collection<Integer> c = new ArrayList<Integer>();

		for (int i = 0; i < 10; ++i)
			c.add(i); 
		for (Integer i : c) 
			System.out.print(i + " ");
		System.out.println();

		// Collection 인터페이스를 구현한 클래스들은 모두 iterator 메소드를 구현했다.
		Iterator<Integer> iterator = c.iterator(); 

		while (iterator.hasNext()) {  
			Integer i = iterator.next(); 
			System.out.print(i + " ");
		}

	}

}
