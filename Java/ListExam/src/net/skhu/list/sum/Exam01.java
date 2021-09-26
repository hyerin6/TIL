package net.skhu.list.sum;

import java.util.ArrayList;
import java.util.LinkedList; 
import java.util.Random;

public class Exam01 {

	static int getSum(ArrayList<Integer> list) {
		int sum = 0; 

		for (Integer i : list) 
			sum = sum + i; return sum; 
	} 

	public static void main(String[] args) {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		LinkedList<Integer> list2 = new LinkedList<Integer>();
		Random random = new Random(); 

		for (int i = 0; i < 10; ++i) {
			list1.add(random.nextInt(20));
			list2.add(random.nextInt(20)); 
		}

		System.out.printf("%s 합계: %d\n", list1.toString(), getSum(list1)); 
		// int sum = getSum(list2); 컴파일 에러 발생 
		/*
		 * getSum 메소드의 파라미터 변수는 ArrayList<Integer> 타입이고
		 * list2 변수는 LinkedList<Integer> 타입이기 때문에
		 * 서로 일치하지 않아서 에러가 발생한다. 
		 */
	}
}
