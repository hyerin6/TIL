package designPattern.singleton;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// private 생성자이므로 new를 통해 인스턴스를 생성할 수 없다.  
		// Singleton s = new Singleton();

		Singleton s1 = Singleton.getInstance();
		Singleton s2 = Singleton.getInstance();
		Singleton s3 = Singleton.getInstance();

		System.out.println(s1);
		System.out.println(s2);
		System.out.println(s3);

		// 아래 출력 결과 : s1만 null이 출력된다. 
		s1 = null;
		System.out.println(s1);
		System.out.println(s2);
		System.out.println(s3);

	}

}
