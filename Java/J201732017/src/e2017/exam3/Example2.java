package e2017.exam3;
import java.util.ArrayList; 
import java.util.Arrays;
import java.util.List;
public class Example2 {
	static List<String> solution(String[] a1, String[] a2, String[] a3) { 
		// (a1 ∪ a2) - a3

		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(a1));
		list.addAll(Arrays.asList(a2));
		list.removeAll(Arrays.asList(a3));
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a1 = { "A", "B", "D", "F", "G" };
		String[] a2 = { "B", "C", "F", "H", "I" };
		String[] a3 = { "B", "F", "G", "H", "I" };

		System.out.println(solution(a1, a2, a3));
	}

}
