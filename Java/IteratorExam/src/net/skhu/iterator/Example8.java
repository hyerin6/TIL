package net.skhu.iterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List; 
import java.util.ListIterator;
public class Example8 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> list = new ArrayList<Integer>();

		for (int i = 0; i < 10; ++i) 
			list.add(i);

		for (int i : list) 
			System.out.print(i + " ");
		System.out.println();

		Iterator<Integer> iterator = list.iterator();

		while (iterator.hasNext()) {
			int i = iterator.next();
			if (i % 2 == 0) 
				iterator.remove(); 
		}

		for (int i : list) 
			System.out.print(i + " ");
		System.out.println();

		// ListIterator은 (이전 데이터 탐색)역방향 탐색이 가능하다. 
		ListIterator<Integer> iter = list.listIterator(list.size());

		while(iter.hasPrevious()) {
			int i = iter.previous();
			if (i % 2 == 1) 
				iter.add(i - 1);
		}

		for (int i : list) 
			System.out.print(i + " ");
	}

}
