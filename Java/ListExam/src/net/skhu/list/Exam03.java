package net.skhu.list;

import java.util.ArrayList; 
import java.util.List;

public class Exam03 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();

		list.add("one");
		list.add("three"); 
		list.add(0, "zero");
		list.add(2, "two");

		for (String s : list) 
			System.out.printf("%s ", s); 

		// get() - 내부 목록에서 인덱스 위치의 객체를 리턴한다.
		for (int i = 0; i < list.size(); ++i) 
			System.out.printf("%s ", list.get(i));

	}

}
