package e2018.exer;

import java.util.Arrays;

class Data3 implements Comparable<Data3>{
	int a;

	public Data3(int a) {
		this.a = a;
	}

	@Override
	public int compareTo(Data3 data) {
		if(this.a > data.a)return 1;
		else if(this.a < data.a) return -1;
		else return 0;
	}
	
	@Override
	public String toString() {
		return String.format("%d", this.a);

	}
}

public class Example12 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Data3[] a = new Data3[] { new Data3(3), new Data3(5), new Data3(1) };
		
		// sort의 파라미터가 comparable을 구현한 클래스의 객체거나 기본자료형이어야된다.
		Arrays.sort(a); 
		System.out.println(Arrays.toString(a));
	}

}
