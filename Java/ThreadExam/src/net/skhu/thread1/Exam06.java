/*
!! 클린코드
4. 공유변수
1) Exam06.java

static 변수 - 한 class에 하나밖에 만들지 못하고 멀티 스레드가 동시에 읽고 쓰면 충돌이 일어난다.

계산 스레드들이 sum 변수를 공유 
>> 동시에 읽고 쓰면 올바른 결과인 15150보다 적은 값이 결과로 나올 수 있다. 
*/

package net.skhu.thread1;
import java.util.Random;



class ThreadE extends Thread {
	String name; 
	Random random;
	static int sum = 0; 
	int count; 

	public ThreadE(String name, int count) {
		this.name = name;
		this.count = count; 
		this.random = new Random();
	}

	@Override 
	public void run() {
		System.out.printf("%s: started\n", name);
		try { 
			for (int i = 1; i <= count; ++i) {
				sum = sum + i; 
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

public class Exam06 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadE threadOne = new ThreadE("one", 100);
		ThreadE threadTwo = new ThreadE("two", 100);
		ThreadE threadThree = new ThreadE("three", 100); 

		threadOne.start(); 
		threadTwo.start(); 
		threadThree.start();
	}
}
