package net.skhu.iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
public class Example5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<Integer> list = new ArrayList<Integer>(); 

		for (int i = 0; i < 10; ++i) 
			list.add(i); 

		// for문 사용
		for (int i = list.size() - 1; i >= 0; --i)
			System.out.print(list.get(i) + " "); 
		System.out.println(); 

		// Iterator 객체 사용
		ListIterator<Integer> iterator = list.listIterator(list.size());

		while (iterator.hasPrevious()) { 
			Integer i = iterator.previous(); 
			System.out.print(i + " "); 
		}

	}

}
