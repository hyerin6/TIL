package net.skhu.exercise.v4;

import java.util.Arrays;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

public class Example1 {

	static int solution(List<Integer> list) {
		// list 목록에서 가장 여러 번 등장하는 정수 값을 리턴하라
		Map<Integer, Integer> map = new HashMap<>();
		int maxValue = 0, maxCount = 0;

		for (int value : list) {
			// map.get(value) - value로 등록했던 value를 리턴한다.
			Integer count = map.get(value);

			map.put(value, count == null ? 1 : count + 1);

			if (map.get(value) > maxCount) {
				maxCount = map.get(value);
				maxValue = value;
			}
		}
		return maxValue;
	}


	// 배열을 이용해서 구현. (단, value 값의 범위는 0 <= value < 10 으로 제한함.)
	static int solution2(List<Integer> list) {
		int[]count = new int[10];
		int maxValue = 0, maxCount = 0;

		for(int value : list) {
			count[value] = count[value] + 1;

			if(count[value] > maxCount) {
				maxCount = count[value];
				maxValue = value;
			}
		}
		return maxValue;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> a =
				Arrays.asList(new Integer[] {1, 7, 1, 8, 5, 2, 2, 3, 5, 3, 1, 3, 7});

		System.out.println(a.toString());

		int maxValue = solution(a);
		System.out.println(maxValue);

		maxValue = solution2(a);
		System.out.println(maxValue);

	}

}
