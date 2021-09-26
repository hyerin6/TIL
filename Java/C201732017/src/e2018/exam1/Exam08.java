package e2018.exam1;

public class Exam08 {
	static int sum(int[][] a) {
		int sum = 0;
		for (int r = 0; r < a.length; ++r) 
			for (int c = 0; c < a[r].length; ++c)
				sum = sum + a[r][c]; 
		return sum; 
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] a1 = { {1, 3, 6}, {2, 6, 3}, {6, 9, 10} }; 
		int[][] a2 = { {5, 4, 1}, {10, 8, 3}, {7, 2, 6} };

		System.out.println(sum(a1));
		System.out.println(sum(a2));
	}
}