/*
synchronized(classNAME.class)
- 파라미터: 객체 
- 이 (파라미터)객체를 사용하는 동안 다른 스레드는 실행불가
- 클래스 변수는 인스턴스 객체가 아니라 그 클래스 객체에 들어있다. 
  즉, ThreadF.class 객체에 들어있다고 보면 된다.
  그래서 ThreadF.class에 lock을 거는 것이다.
*/

package net.skhu.thread1;

import java.util.Random;


class ThreadF extends Thread {
	String name; 
	Random random;
	static int sum = 0; 
	int count; 

	public ThreadF(String name, int count) {
		this.name = name;
		this.count = count; 
		this.random = new Random();
	}

	@Override 
	public void run() {
		System.out.printf("%s: started\n", name);
		try { 
			for (int i = 1; i <= count; ++i) {
				synchronized(ThreadF.class){
					sum = sum + i; 
				}
				int mili_second = random.nextInt(10); 
				sleep(mili_second); 
			}
		} catch (InterruptedException e) { 
			System.out.printf("%s: interrupted\n", name);
		}
		System.out.printf("%s: stoped\n", name);
		System.out.printf("%s: %d\n", name, sum); 
	} 
}

public class Exam07 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadF threadOne = new ThreadF("one", 100);
		ThreadF threadTwo = new ThreadF("two", 100);
		ThreadF threadThree = new ThreadF("three", 100); 

		threadOne.start(); 
		threadTwo.start(); 
		threadThree.start();
	}
}


