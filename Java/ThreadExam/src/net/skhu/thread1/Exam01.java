package net.skhu.thread1;

import java.util.Random;

// Thread 클래스를 상속하여 자식클래스에서 run메소드를 재정의 한다.
class ThreadA extends Thread{
	String name; 
	Random random;

	public ThreadA(String name) {
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
				sleep(mili_second); 
			} 
		} catch (InterruptedException e) {
			System.out.printf("%s: interrupted\n", name); 
		}
		System.out.printf("%s: stoped\n", name); 
	} 
}

public class Exam01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread threadOne = new ThreadA("one");
		Thread threadTwo = new ThreadA("two");
		Thread threadThree = new ThreadA("three"); 

		threadOne.start(); // ThreadA의 run 메소드 실행
		threadTwo.start(); 
		threadThree.start();
	}

}
