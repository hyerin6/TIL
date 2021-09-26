package e2018.exam1;

public class Exam10 {

	static String removeSubstring(String s, int fromIndex, int toIndex) {
		StringBuilder builder = new StringBuilder(s);
		builder.delete(fromIndex, toIndex);
		return builder.toString();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = { "hello", "world", "hello hello" };

		System.out.println(removeSubstring(a[0], 1, 4)); 
		System.out.println(removeSubstring(a[1], 2, 4)); 
		System.out.println(removeSubstring(a[2], 2, 5));
	}

}
