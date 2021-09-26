package e2017.exam3;
import java.util.Arrays;

public class Example5 {
	static String[] convertToArray(String s) {
		s = s.replaceAll("[^a-zA-Z]+", " ");
		s = s.trim();
		return s.split("[^a-zA-Z]+");
	}

	static String[] convertToArray2(String s) {
		// replaceAll 메소드가 리턴하는 객체에 trim() 메소드 호출
		s = s.replaceAll("[^a-zA-Z]+", " ").trim();
		return s.split("[^a-zA-Z]+");
	}

	static String[] convertToArray3(String s) {
		// trim 메소드가 리턴하는 객체에 split() 메소드 호출
		String[] a = s.replaceAll("[^a-zA-Z]+", " ").trim().split("[^a-zA-Z]+");
		return a;
	}

	static String[] convertToArray4(String s) {
		// method chaining
		return s.replaceAll("[^a-zA-Z]+", " ")
				.trim()
				.split("[^a-zA-Z]+");
	}

	public static void main(String[] args) {
		String[] a = { "one 2 two, - Three,\t four; @ \t\nfive..,SIX)",
		"1 one 2 two, - Three,\t four; @ \t\nfive..,SIX)" };
		for (String s : a) {
			String[] t = convertToArray(s);
			System.out.println(Arrays.toString(t));
		}
	}
}
