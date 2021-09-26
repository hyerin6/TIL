package net.skhu.list;

import java.util.ArrayList; 
import java.util.List;

public class Exam05 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();

		list.add("zero"); 
		list.add("one");
		list.add("two"); 
		list.add("three");

		list.remove("two"); // collection의 remove
		
		/* 위 코드는 list.remove(2); 와 동일하다. - list의 remove
		   
		   collection의 remove는 data로 삭제하는 것이므로 equals 재정의가 중요하다.
		   list의 remove는 인덱스 위치로 삭제하는 것이기 때문에 equals가 중요하진 않다. 
		 */


		for (String s : list) 
			System.out.printf("%s ", s);
	}

}
