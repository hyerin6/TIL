package e2018.exam1;

public class Exam09 {

	static String reverse(String path) {
		StringBuilder builder = new StringBuilder(path);
		builder.reverse();
		return builder.toString();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = { "hello", "world", "hello world" };

		for(String s : a)
			System.out.println(reverse(s));

	}

}
