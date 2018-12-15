package insert;

class sort{
	public void insertionSort(int a[], int size){
		int i, j, temp;

		for(i = 1; i < size; i++) {
			temp = a[i]; // temp에 하나씩 저장 

			// 맨앞에서 부터 비교하는데 temp가 삽입될 자리를 탐색한다. - j에 삽입
			for(j = i; j>0 && a[j-1]>temp; j--) 
				a[j] = a[j-1];
			a[j] = temp;
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

		s.insertionSort(a, a.length);
		System.out.print("정렬 후 : ");
		for(int i : a)
			System.out.print(i + " ");
		System.out.println();

	}

}
