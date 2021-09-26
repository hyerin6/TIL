package equalsExam;

public class PersonTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Person[] a = new Person[] {
				new Person(null, 18),
				new Person("홍길동", 18),
				new Person("전우치", 23) 
		};

		System.out.println(a[0].equals(a[1]));
		System.out.println(a[0].equals(a[2]));
	}
}
