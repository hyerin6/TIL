package e2018.exam1;

public class Exam01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Person p1 = new Person("홍길동", "201132050", 2, 4.1); 
		Person p2 = new Person("홍길동", null, 2, 4.1); 
		Person p3 = new Person("홍길동", "201132050", 2, 4.1);

		System.out.println(p1 == p2);
		System.out.println(p1.equals(p2));
		System.out.println(p1.equals(p3));
	}

}
