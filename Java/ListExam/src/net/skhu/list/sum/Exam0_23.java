package net.skhu.list.sum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/*
int getSum(List<Integer> list)
메소드만 구현하는 것이 정답이다.
이 메소드의 파라미터 변수 타입이 List<Integer> 이기 때문에,
ArrayList<Integer>, LinkedList<Integer>, Vector<Integer> 객체들을 사용할 수 있다.
*/

public class Exam0_23 {
	static int getSum(List<Integer> list) {
		int sum = 0; 

		for (Integer i : list) 
			sum = sum + i; return sum; 
	} 

	static void addRandomValue(List<Integer> list, int count) { 
		Random random = new Random();

		for (int i = 0; i < count; ++i)
			list.add(random.nextInt(20)); 
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Integer> list1 = new ArrayList<>(); 
		LinkedList<Integer> list2 = new LinkedList<>();
		Vector<Integer> list3 = new Vector<>();

		addRandomValue(list1, 10); 
		addRandomValue(list2, 10); 
		addRandomValue(list3, 10); 

		System.out.printf("%s 합계: %d\n", list1.toString(), getSum(list1));
		System.out.printf("%s 합계: %d\n", list2.toString(), getSum(list2));
		System.out.printf("%s 합계: %d\n", list3.toString(), getSum(list3));
	}

}
