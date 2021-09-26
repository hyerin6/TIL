package net.skhu.list;

import java.util.ArrayList; 
import java.util.List;

public class Exam07 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();

		list.add("zero"); 
		list.add("one"); 
		list.add("two"); 
		list.add("three"); 
		list.add("four");

		// list 인덱스 1, 2, 3의 값들이 list2에 리턴
		List<String> list2 = list.subList(1, 4);

		for (String s : list2)
			System.out.printf("%s ", s);

	}

}
