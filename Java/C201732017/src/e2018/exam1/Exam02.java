package e2018.exam1;

public class Exam02 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Person[] a = new Person[2]; 
		a[0] = new Person("홍길동", "201132050", 2, 4.1);
		a[1] = new Person("홍길동", "201132050", 2, 4.1);

		System.out.println(a[0] == a[1]);
		System.out.println(a[0].equals(a[1]));
	}
}