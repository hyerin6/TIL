package merge;

class sort{
	public void mergeSort(int[] a, int lb, int ub) {
		if(lb < ub) {
			int mid = (lb+ub)/2;
			mergeSort(a, lb, mid);
			mergeSort(a, mid+1, ub);
			merge(a, lb, mid, ub);
		}
	}

	public void merge(int[] arr, int m, int middle, int n) {
		int i, j, k, t;
		i = m;
		j = middle + 1;
		k = m;

		int[] sorted = new int[arr.length];

		while (i <= middle && j <= n) {
			if (arr[i] <= arr[j])
				sorted[k] = arr[i++];
			else
				sorted[k] = arr[j++];
			k++;
		}

		if (i > middle) {
			for (t = j; t <= n; t++, k++)
				sorted[k] = arr[t];
		} else {
			for (t = i; t <= middle; t++, k++)
				sorted[k] = arr[t];
		}

		for (t = m; t <= n; t++)
			arr[t] = sorted[t];
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

		s.mergeSort(a, 0, a.length-1);
		System.out.print("정렬 후 : ");
		for(int i : a)
			System.out.print(i + " ");
		System.out.println();

	}

}
