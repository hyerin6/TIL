package net.skhu.exercise.v4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Example2 {

	static void solution(List<Integer> a) { 
		// 목록 a에서 2의 배수와 3의 배수를 모두 제거하라
		Iterator<Integer> i = a.iterator();

		// for each 문으로 탐색하면서 add, remove하면 에러가 발생한다.
		while(i.hasNext()) {
			Integer it = i.next();
			if(it % 2 == 0 || it % 3 == 0) 
				i.remove();
		}

		/*
		int index = 0;
		while (index < a.size()) {
			int value = a.get(index);
			if ((value % 2) == 0 || (value % 3) == 0)
				a.remove(index);
			else
				++index;
		}*/

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> a = new ArrayList<>();
		a.addAll(Arrays.asList(new Integer[] {1, 6, 1, 8, 9, 2, 2, 3, 5, 3, 1, 3, 7}));
		System.out.println(a.toString());
		solution(a); 
		System.out.println(a.toString());
	}

}
