package net.skhu.sort;

/*
ArrayList<Integer> 타입의 목록 객체를 하나 생성하고 정수 난수를 200 개 추가하라.

(1 ~ 100 사이의 random integer 200 개)

ArrayList<Integer> 타입의 목록 객체를 정렬하는 메소드를 구현하라.

size, get, set 메소드 사용 해서 구현.
*/

import java.util.ArrayList;
import java.util.Random;

public class Exam01 {
	static void swap(ArrayList<Integer> list, int i, int j) {
		int temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
	}

	static void bubbleSort(ArrayList<Integer> list) {
		for(int i = list.size()-1; i>=1; --i) {
			for(int j = 0; j<i; ++j) {
				if(list.get(j)>list.get(j+1))
					swap(list, j, j+1);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random random = new Random();
		ArrayList<Integer> list = new ArrayList<Integer>();

		for(int i=0; i<200; i++) 
			list.add(random.nextInt(100)+1); // 1~100

		System.out.println(list.toString());
		bubbleSort(list);
		System.out.println(list.toString());
	}
}