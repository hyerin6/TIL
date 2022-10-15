package designPattern.templateMethod;

public class Cat extends Animal{

	@Override 
	// 추상 메서드 오버라이딩 
	void play() {
		System.out.println("야옹..");
	}
	
	// Hook(갈고리) 메서드 오버라이딩 
	@Override
	void runSomething() {
		System.out.println("야옹~");
	}

}
