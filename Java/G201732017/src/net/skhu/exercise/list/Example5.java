package net.skhu.exercise.list;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Example5 {

	public static void main(String[] args) {
		String[] a1 = { "d", "a", "b", "a", "c", "a" };
		String[] a2 = { "b", "a", "f", "e", "b", "b" };

		// 구현2
		Set<String> output = new HashSet<>();
		output.addAll(Arrays.asList(a1));
		output.removeAll(Arrays.asList(a2));
		System.out.println(output.toString());
	}
}
