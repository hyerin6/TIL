import java.util.Arrays;

public class binarySearchExam {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer[] a = new Integer[] {10, 3, 5, 2, 8};

		// 이진 탐색은 반드시 정렬된 배열로 해야한다.
		Arrays.sort(a);

		for(int i : a)
			System.out.println(a[Arrays.binarySearch(a, i)] + " ");
	}

}
