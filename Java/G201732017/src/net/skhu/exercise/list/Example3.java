package net.skhu.exercise.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Example3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = { "d", "a", "b", "a", "c", "d", "e", "f", "e" };


		// 구현1 - list는 중복 허용하기 때문에 if문을 사용해야함
		List<String> list = new ArrayList<>();
		for (String s : a)
			if (!list.contains(s))
				list.add(s);
		System.out.println(list.toString());

		// 구현2 - set은 중복을 허용하지 않는다.
		Set<String> set = new HashSet<>();
		for (String s : a)
			set.add(s);
		System.out.println(set.toString());

	}

}
