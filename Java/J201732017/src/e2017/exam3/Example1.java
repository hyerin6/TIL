package e2017.exam3;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Example1 {

	static List<String> solution(String[] a1, String[] a2, String[] a3) { 
		// a1 배열, a2 배열, a3 배열의 교집합을 계산해서 리턴
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(a1));
		list.retainAll(Arrays.asList(a2));
		list.retainAll(Arrays.asList(a3));
		return list;
	}

	static Collection<String> solution2(String[] a1, String[] a2, String[] a3) {
		Set<String> set = new HashSet<>();
		set.addAll(Arrays.asList(a1));
		set.retainAll(Arrays.asList(a2));
		set.retainAll(Arrays.asList(a3));
		return set;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a1 = { "A", "B", "D", "F", "G"}; 
		String[] a2 = { "B", "C", "F", "H", "I" };
		String[] a3 = { "B", "F", "G", "H", "I" };

		System.out.println(solution2(a1, a2, a3)); // 동일한 값도 삭제
		System.out.println(solution(a1, a2, a3)); // 동일한 값은 삭제하지 못함

	}

}
