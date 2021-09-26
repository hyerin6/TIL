package net.skhu.exercise.list;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Example6 {

	public static void main(String[] args) {
		Random random = new Random();
		int[] a = new int[20];
		for (int i = 0; i < a.length; ++i)
			a[i] = random.nextInt(5);

		// 구현
		Map<Integer, Integer> map = new HashMap<>();
		for (int key : a) {
			Integer count = map.get(key);
			if (count == null) count = 0;
			++count;
			map.put(key, count);
		}

		for (int key : map.keySet())
			System.out.printf("%d=%d ", key, map.get(key));
	}
}

