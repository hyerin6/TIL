package net.skhu.inner;

public class OutterClass2 {
	int a;

	static class InnerClass {
		int b;

		void method2() {
			b = 5; 
			this.b = 6; 

			// a = 7; 
			// OutterClass2.this.a = 8;
			// instanceMethod();
			// OutterClass2.this.instanceMethod(); 
			// this.instanceMethod(); 
			// -> 컴파일 에러

			main(null); 
		}
	}

	public void instanceMethod() {
		InnerClass obj = new InnerClass(); 
		a = 1;
		this.a = 2;
		obj.method2();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// static innerClass - outterClass의 this가 없어도 객체 생성 가능
		InnerClass obj = new InnerClass();
		obj.method2();

		// outterclass의 this 사용 불가
		// a = 3;
		// this.a = 4;
		// a = 7;  
		// OutterClass2.this.a = 8; 
		// instanceMethod(); 
		// this.instanceMethod(); 
		// OutterClass2.this.instanceMethod(); 
	}

}