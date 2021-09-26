package net.skhu.iterator;

import java.util.ArrayList; 
import java.util.Collection;
import java.util.Iterator;

public class Example3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Collection<Integer> c = new ArrayList<>(); 

		for (int i = 0; i < 10; ++i) 
			c.add(i); 

		System.out.print("원본 : ");
		for (int i : c)
			System.out.print(i + " ");
		System.out.println(); 


		/*
		 * for문에서 i를 0부터 시작했다면 c의 size() 리턴값이 변해서
		 * 값을 전부 탐색하지 못하고 for문이 종료되어 
		 * 짝수를 삭제하지 못했을 것이다.
		 */
		System.out.println("===== for문 사용 remove =====");
		for (int i = c.size(); i >= 0; --i) {
			if (i % 2 == 0) {
				c.remove(i); 
				System.out.println("짝수 삭제 완료 ! " + i);
			}
			else 
				System.out.println("홀수입니다~ " +  i);
		}

		for (int i : c)
			System.out.print(i + " ");
		System.out.println();

		System.out.println("===== Iterator 사용 remove =====");
		System.out.print("원본 : ");
		for (int i : c)
			System.out.print(i + " ");
		System.out.println(); 

		for (int i = 0; i < 10; i = i+2) 
			c.add(i); 

		Iterator<Integer> iterator = c.iterator();

		while (iterator.hasNext()) {
			int i = iterator.next();
			if (i % 2 == 0) iterator.remove(); 
		}

		for (int i : c)
			System.out.print(i + " ");

	}

}
