package e2018.exam1;

public class Exam05 {
	
	public static void doSomething(int i) {
		System.out.printf("%05d\n", i);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = {2, 34, 256, 1980};

		for(int i : a)
			doSomething(i);
	}

}
