/*
공유되는 객체의 메소드에 synchronized를 붙여주면, 
이 메소드를 호출할 때 이 객체에 lock이 걸려 하나의 스레드만 
이 객체의 메소드를 호출할 수있어서 안전하다.
*/
package net.skhu.thread1;

import java.util.Random;

class SharedObjB {
	int sum = 0;
	synchronized void add(int i) {
		sum = sum + i; 
	}
}

class ThreadJ extends Thread {
	String name; 
	int count; 
	SharedObjB sharedObj;
	Random random;

	public ThreadJ(String name, int count, SharedObjB sharedObj) {
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


public class Exam09 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SharedObjB sharedObj = new SharedObjB(); 
		// sharedObjA 클래스 객체를 3개의 계산 스레드가 공유한다.
		// 공유되는 객체의 멤버변수(sum)을 동시에 읽고 쓰는 것은 문제 발생 !
		ThreadJ threadOne = new ThreadJ("one", 100, sharedObj); 
		ThreadJ threadTwo = new ThreadJ("two", 100, sharedObj);
		ThreadJ threadThree = new ThreadJ("three", 100, sharedObj);

		threadOne.start(); 
		threadTwo.start(); 
		threadThree.start();
	}

}
