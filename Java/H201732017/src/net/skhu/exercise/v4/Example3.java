package net.skhu.exercise.v4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Example3 { 

	static void solution(int[] a) {
		// 배열 a에서 2의 배수와 3의 배수를 모두 제거하고
		// 제거된 항목의 뒷 항목들을 앞으로 당겨야 한다. 
		// 제거된 항목 때문에 생간 배열 뒤의 공간에 -1을 채워라

		List<Integer> list = new ArrayList<>();

		for(int i = 0; i < a.length; ++i)
			if(a[i] % 2 != 0 && a[i] % 3 != 0)
				list.add(a[i]);

		for(int i = 0; i < a.length; ++i) 
			a[i] = (i < list.size() ? list.get(i) : -1);

	}

	static void removeAt(int[] a, int index) {
		for (int i = index; i < a.length - 1; ++i)
			a[i] = a[i + 1];
	}

	static void insertAt(int[] a, int index, int value) {
		for (int i = a.length - 1; i > index; --i)
			a[i] = a[i - 1];
		a[index] = value;
	}

	static void solution2(int[] a) { // 버그가 있음.
		for (int i = 0; i < a.length; ++i)
			if ((a[i] % 2) == 0 || (a[i] % 3) == 0) {
				removeAt(a, i);
				a[a.length - 1] = -1;
			}
	}

	static void solution2a(int[] a) {
		for (int i = 0; i < a.length; ++i)
			if ((a[i] % 2) == 0 || (a[i] % 3) == 0) {
				removeAt(a, i);
				a[a.length - 1] = -1;
				--i;
			}
	}

	static void solution2b(int[] a) {
		for (int i = a.length - 1; i >= 0; --i)
			if ((a[i] % 2) == 0 || (a[i] % 3) == 0) {
				removeAt(a, i);
				a[a.length - 1] = -1;
			}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a1 = new int[] {1, 6, 1, 8, 9, 2, 2, 3, 5, 3, 1, 3, 7}; 
		System.out.println(Arrays.toString(a1)); 
		solution(a1);
		System.out.println(Arrays.toString(a1));
		System.out.println("------------------------------------------------");

		int[] a2 = new int[] {1, 6, 1, 8, 9, 2, 2, 3, 5, 3, 1, 3, 7}; 
		System.out.println(Arrays.toString(a2)); 
		solution2(a2);
		System.out.println(Arrays.toString(a2));
		System.out.println("------------------------------------------------");

		int[] a3 = new int[] {1, 6, 1, 8, 9, 2, 2, 3, 5, 3, 1, 3, 7};
		System.out.println(Arrays.toString(a3)); 
		solution2a(a3);
		System.out.println(Arrays.toString(a3));
		System.out.println("------------------------------------------------");

		int[] a4 = new int[] {1, 6, 1, 8, 9, 2, 2, 3, 5, 3, 1, 3, 7};
		System.out.println(Arrays.toString(a4)); 
		solution2b(a4);
		System.out.println(Arrays.toString(a4));

	}

}
