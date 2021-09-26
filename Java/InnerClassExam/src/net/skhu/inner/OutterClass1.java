package net.skhu.inner;

public class OutterClass1 {
	int a;

	class InnerClass {
		int b;

		void method2() {
			b = 5; // (1)
			this.b = 6; // (2)
			// (1), (2) - 같은 의미

			a = 7; 
			OutterClass1.this.a = 8; // (3)
			instanceMethod();
			OutterClass1.this.instanceMethod(); 
			// this.instanceMethod(); -> 컴파일 에러 

			// innerClass의 method에서 outterClass의 static method 호출 가능
			main(null); 
		}
	}

	public void instanceMethod() {
		// instance method는 인스턴스 메소드이다.
		// this 사용이 가능하고 innerClass 객체도 생성할 수 있다.
		InnerClass obj = new InnerClass(); 
		a = 1; // (4)
		// (3), (4) - 같은 의미

		this.a = 2;
		obj.method2();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		OutterClass1 o = new OutterClass1();
		o.instanceMethod();

		// main은 static 메소드이다. 
		// this를 사용할 수 없고 innerClass 객체를 생성할 수 없다.
		// InnerClass obj = new InnerClass(); 컴파일 에러 
		// a = 3; 컴파일 에러 
		// this.a = 4; 컴파일 에러

	}

}