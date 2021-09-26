package net.skhu.thread1;

import java.util.Random;

// Runnable 인터페이스를 구현한 ChildRunnable에서 run메소드를 재정의
class RunnableA implements Runnable {
	String name; 
	Random random;

	public RunnableA(String name) {
		this.name = name;
		this.random = new Random();
	}

	@Override
	public void run() {
		System.out.printf("%s: started\n", name);
		try { 
			for (int i = 0; i < 100; ++i) {
				System.out.printf("%s: %d\n", name, i);
				int mili_second = random.nextInt(100);
				Thread.sleep(mili_second); 
			} 
		} catch (InterruptedException e) {
			System.out.printf("%s: interrupted\n", name); 
		}
		System.out.printf("%s: stoped\n", name); 
	}
}
public class Exam02 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// ChildRunnable 객체를 파라미터로 Thread 객체 생성
		Thread threadOne = new Thread(new RunnableA("one")); 
		Thread threadTwo = new Thread(new RunnableA("two")); 
		Thread threadThree = new Thread(new RunnableA("three"));

		threadOne.start(); 
		threadTwo.start(); 
		threadThree.start();
	}

}
