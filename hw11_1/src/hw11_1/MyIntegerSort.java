/* 
 * 파일명: MyIntegerSort.java
 * 작성자: 박혜린 
 * 작성일: 2018-12-09
 * 목적: 병합 정렬 알고리즘과 병합 알고리즘을 이해한다.
 */
package hw11_1;

class MyIntegerSort{

	// 정수 배열을 매개변수로 받아 병합 정렬 
	static void mergeSort(int[] arr) {
		mergeSort(arr, 0, arr.length-1);
	}

	// 정수 배열(array)과 인덱스 범위 하한(lb), 상한(ub)을 매개변수로 받아
	// array[lb..ub]를 병합 정렬 
	public static void mergeSort(int[] arr, int m, int n) {
		int middle;
		if (m < n) {
			middle = (m + n) / 2;
			mergeSort(arr, m, middle);
			mergeSort(arr, middle + 1, n);
			merge(arr, m, middle, n);
		}
	}


	// 정수 배열(array)과 인덱스 하한(lb), 중간(middle), 상한(ub)을 매개변수로 받음
	public static void merge(int[] arr, int m, int middle, int n) {
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
