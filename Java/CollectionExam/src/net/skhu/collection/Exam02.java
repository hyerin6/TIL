package net.skhu.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class Exam02 { // + Exam03

	static void addData(Collection<String> c) {
		// 여기서 add 메소드 호출은 다형성 호출이다.
		c.add("one");
		c.add("two");
		c.add("three");
	}

	static void printCollection1(Collection<String> c) {
		for (String s : c)
			System.out.printf("%s ", s);
		System.out.println();
	}

	static void printCollection2(Collection<String> c) {
		// Collection의 toArray 메소드는 Object[] 타입의 배열을 리턴한다.
		// 리턴되는 a 배열에는 c 컬렉션의 객체에 들어 있던 문자열 참조값이 들어 있다.
		// 즉, a와 c에는 같은 문자열 참조값이 들어 있다.
		Object[] a = c.toArray(); 
		String s = Arrays.toString(a);
		// String s = c.toString(); 도 가능 !
		System.out.println(s);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Collection<String> c1 = new LinkedList<String>();
		Collection<String> c2 = new ArrayList<String>();

		addData(c1);
		addData(c2); 

		printCollection1(c1); 
		printCollection1(c2);

		printCollection2(c1); 
		printCollection2(c2);
	}
}