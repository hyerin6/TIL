package net.skhu.exercise.list;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Example4 {

	public static void main(String[] args) {
		String[] a1 = { "d", "a", "b", "a", "c", "a" };
		String[] a2 = { "b", "a", "f", "e", "b", "b" };
		// 구현1
		/*
        List<String> list2 = Arrays.asList(a2);
        List<String> output = new ArrayList<>();
        for (String s : a1)
            if (list2.contains(s))
                if (!output.contains(s))
                    output.add(s);
        System.out.println(output.toString());
		 */

		// 구현2
		Set<String> output = new HashSet<>();
		output.addAll(Arrays.asList(a1));
		output.retainAll(Arrays.asList(a2));
		System.out.println(output.toString());
	}
}
