package e2017.exam2;

public class Example4 {

	int a = 3, b = 4;

	void doSomethoing() {
		// swap에 멤버변수가 존재하기 때문에 람다식으로 바꿀 수 없다. 
		new InnerClass().swap();
		System.out.printf("%d %d\n", a, b);
	}

	class InnerClass {
		public void swap() {
			int temp =  a; // 멤버변수
			a = b;
			b = temp;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Example4().doSomethoing();
	}

}
