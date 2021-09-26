package e2018.exam1;

public class Exam03 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Person[] a = new Person[2];
		Person[] b = a;
		a[0] = new Person("홍길동", "201132050", 2, 4.1);
		a[1] = a[0];
		
		System.out.println(a == b); 
		System.out.println(a[0] == b[1]); 
		System.out.println(a[0].equals(b[1]));
	}
}
