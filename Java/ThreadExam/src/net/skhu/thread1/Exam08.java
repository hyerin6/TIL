package net.skhu.thread1;

import java.util.Random;

class SharedObjA {
	int sum = 0;
	void add(int i) {
		sum = sum + i; 
	}
}

class ThreadH extends Thread {
	String name; 
	int count; 
	SharedObjA sharedObj;
	Random random;

	public ThreadH(String name, int count, SharedObjA sharedObj) {
		this.sharedObj = sharedObj;
		this.name = name;
		this.count = count; 
		this.random = new Random();
	}

	@Override 
	public void run() {
		System.out.printf("%s: started\n", name);
		try { 
			for (int i = 1; i <= count; ++i) {
				sharedObj.add(i); //add 메소드에서 멤버변수 sum이 읽고 쓰인다.
				int mili_second = random.nextInt(10); 
				sleep(mili_second); 
			}
		} catch (InterruptedException e) { 
			System.out.printf("%s: interrupted\n", name);
		}
		System.out.printf("%s: stoped\n", name);
		System.out.printf("%s: %d\n", name, sharedObj.sum); 
	} 
}


public class Exam08 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SharedObjA sharedObj = new SharedObjA(); 
		// sharedObjA 클래스 객체를 3개의 계산 스레드가 공유한다.
		// 공유되는 객체의 멤버변수(sum)을 동시에 읽고 쓰는 것은 문제 발생 !
		ThreadH threadOne = new ThreadH("one", 100, sharedObj); 
		ThreadH threadTwo = new ThreadH("two", 100, sharedObj);
		ThreadH threadThree = new ThreadH("three", 100, sharedObj);

		threadOne.start(); 
		threadTwo.start(); 
		threadThree.start();
	}

}
