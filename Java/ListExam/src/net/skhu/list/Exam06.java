package net.skhu.list;

import java.util.ArrayList; 
import java.util.List;

public class Exam06 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		
		list.add("zero"); 
		list.add("one"); 
		list.add("two"); 
		list.add("three");
		
		// add와 달리 set은 인덱스에 존재하던 기존의 값을 덮어쓴다.
		list.set(2, "TWO");

		for (String s : list)
			System.out.printf("%s ", s);

	}

}
