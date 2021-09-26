/*
 * 06 자바 배열 정렬 기능 
 * 4. 연습문제 
 * 2) 내림차순 정렬 
 * 
 * Arrays 클래스의 sort 메소드에는 내림차순 정렬 기능이 없다. 
 * 
 * 객체를 정렬하는 경우, 
 * Comparator 클래스의 compare을 수정하여 내림차순 정렬이 가능하다.
 * 결과에 -1 곱해주면됨 
 */
import java.util.Comparator;
import java.util.Arrays; 
public class sortExam {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Integer[] arr = {5, 2, 3, 7};

		Arrays.sort(arr, new IntegerComperator());
		System.out.println(Arrays.toString(arr));
	}

}
/*
static void sort(T[] a)

배열의 원소를 정렬한다. 배열의 원소의 타입 T는 기본 자료형이거나,
Comparable interface를 implements한 클래스 타입이어야 한다.
예를 들어 Java의 String 클래스나 Date 클래스는 Comparable interface를 implements 하였다.
값의 크기를 비교할 수 있는, Java 표준 라이브러리의 클래스들은 대부분 Comparable interface를 implements 하였다.
 */

class IntegerComperator implements Comparator<Integer>{
	@Override
	public int compare(Integer o1, Integer o2) {
		// TODO Auto-generated method stub
		int r = o1.intValue() - o2.intValue();
		if(r != 0) return r * -1;
		return 0;
	}
}