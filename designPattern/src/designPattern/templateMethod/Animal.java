package designPattern.templateMethod;

public abstract class Animal {
	
	// 템플릿 메서드  
	public void playWithOwner() {
		System.out.println("디디야~");
		play();
		runSomething();
		System.out.println("잘했어~");
	}
	
	// 추상 메서드 
	abstract void play();
	
	// Hook(갈고리) 메서드 
	void runSomething() {
		System.out.println("~");
	}

}
