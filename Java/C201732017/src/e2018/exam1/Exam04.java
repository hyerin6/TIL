package e2018.exam1;

public class Exam04 {

	public static void doSomething(String s) {
		for(int i = 0; i < s.length(); i++)
			System.out.printf("(%c)", s.charAt(i));
		System.out.println();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = { "hello", "world", "hello world" };

		for (String s : a) 
			doSomething(s);
	}
}
