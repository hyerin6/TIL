package bubble;

class sort{
	public void bubbleSort(int[] a) {
		int i, j, temp, size;
		size = a.length;

		for(i = size-1; i > 0; i--) {
			for(j = 0; j < i; j++) {
				if(a[j] > a[j+1]) {
					temp = a[j];
					a[j] = a[j+1];
					a[j+1] = temp;
				}
			}
		}
	}
}

public class SortExam {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] a = {6, 3, 5, 2, 0, 1, 2, 5, 2};
		sort s = new sort();

		System.out.print("정렬 전 : ");
		for(int i : a)
			System.out.print(i + " ");
		System.out.println();

		s.bubbleSort(a);
		System.out.print("정렬 후 : ");
		for(int i : a)
			System.out.print(i + " ");
		System.out.println();

	}

}
