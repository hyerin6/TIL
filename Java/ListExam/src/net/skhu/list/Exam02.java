package net.skhu.list;

import java.util.ArrayList; 
import java.util.LinkedList;
import java.util.List;

public class Exam02 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new LinkedList<String>();

		list1.add("one");
		list1.add("two");
		list2.add("zero"); 
		list2.add("three");

		// addAll도 원하는 인덱스 위치에 목록 객체를 추가할 수 있다.
		list2.addAll(1, list1);

		for (String s : list2) 
			System.out.printf("%s ", s);
	}

}
