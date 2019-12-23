package stack;

public class 동물 {
	
	int age;
	
	void method(){
		System.out.println("동물 입니다 ~ ");
	}

}

class 펭귄 extends 동물{
	
	void method(){
		//super.method();
		
		System.out.println("펭귄 입니다 ~ " + age + "살 입니다 ! ");
	}
	
}
