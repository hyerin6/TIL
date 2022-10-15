/* 
 * 파일명: SortTest.java
 * 작성자: 박혜린 
 * 작성일: 2018-12-09
 * 목적: 병합 정렬 알고리즘과 병합 알고리즘을 이해한다.
 */
package hw11_1;
import java.util.Scanner;

public class SortTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("hw11_1: 박혜린\n");

		Scanner scan = new Scanner(System.in);

		System.out.print("정수 개수 입력: ");
		int n = scan.nextInt();
		int[] arr = new int[n];
		
		System.out.print(n + "개의 정수 입력: ");
		for(int i = 0; i < n; ++i) 
			arr[i] = scan.nextInt();

		System.out.print("\n정렬 전 배열: ");
		for(int i = 0; i < n; ++i)
			System.out.print(arr[i] + " ");

		MyIntegerSort.mergeSort(arr);

		System.out.print("\n정렬 후 배열: ");
		for(int i = 0; i < n; ++i)
			System.out.print(arr[i] + " ");
	}


}
