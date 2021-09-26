package net.skhu.list;

import java.util.ArrayList; 
import java.util.List;

public class Exam04 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();

		list.add("zero"); 
		list.add("one"); 
		list.add("two");
		list.add("three"); 

		// indexOf() - equals를 반드시 재정의해야 한다. 
		// data를 찾는 메소드는 전부 equals 재정의가 중요 ! 
		System.out.println(list.indexOf("two"));
		System.out.println(list.indexOf("three")); 
		System.out.println(list.indexOf("four"));
	}

}
