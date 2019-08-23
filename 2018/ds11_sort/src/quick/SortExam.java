package quick;

class sort{
	int p;
	public void quickSort(int[] a, int b, int e) {
		if(b < e) {
			p = partition(a, b, e);
			quickSort(a, b, p-1);
			quickSort(a, p+1, e);
		}
	}

	public int partition(int[] a, int b, int e) {
		int pivot = a[b];
		int i = b;
		int j = e+1;

		do {
			do {i=i+1;} while(i<=e && a[i]<pivot);
			do {j=j-1;} while(a[j]>pivot);
			if(i<j) swap(a, i, j);
		}while(i<j);

		swap(a, b, j);

		return j;
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
		int[] a = {6, 3, 5, 2, 0, 1, 2, 5, 2};
		sort s = new sort();

		System.out.print("정렬 전 : ");
		for(int i : a)
			System.out.print(i + " ");
		System.out.println();

		s.quickSort(a, 0, a.length-1);
		System.out.print("정렬 후 : ");
		for(int i : a)
			System.out.print(i + " ");
		System.out.println();

	}

}
