package selection;

class sort{
	public void selectionSort(int a[]) {
		int i, j, min;

		for(i = 0; i < a.length-1; ++i) {
			min = i;

			for(j = i+1; j<a.length; ++j) {
				if(a[min] > a[j])
					min = j;
			} swap(a, min, i);
		}
	}


	public void swap(int[] a, int min, int i) {
		int temp = a[min];
		a[min] = a[i];
		a[i] = temp;
	}
}

public class SortExam {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = {1, 3, 5, 2, 1, 4, 6, 7, 0, 8, 9, 3};

		System.out.print("정렬 전 : ");
		for(int i : a)
			System.out.print(i + " ");
		System.out.println();

		sort s = new sort();
		s.selectionSort(a);

		System.out.print("정렬 후 : ");
		for(int i : a)
			System.out.print(i + " ");
		System.out.println();

	}

}
