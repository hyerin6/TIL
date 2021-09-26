package net.skhu.list;

import java.util.ArrayList;
import java.util.List;

public class Exam01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>(); 

		// collection에서 상속받은 add는 맨뒤에 삽입이 되고
		// list에 선언된 add는 원하는 인덱스 위치에 삽입됩니다. 
		list.add("one");
		list.add("three"); 
		list.add(0, "zero"); 
		list.add(2, "two"); 

		for (String s : list) 
			System.out.printf("%s ", s);

	}

}
