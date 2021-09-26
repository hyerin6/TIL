package net.skhu.thread1;
import java.util.Random;
class ThreadTH extends Thread{
	String name; 
	Random random;
	int count; 
	int sum;

	public ThreadTH(String name, int count) {
		this.name = name;
		this.count = count; 
		this.sum = 0;
	}

	public void run() {
		System.out.printf("%s: started\n", name);

		// sum 계산이 다 끝난 후 sum을 출력하기 때문에 올바른 코드입니다.
		/*
		try {
			for(int i = 0; i <= count; i++) {
				sum = sum+i;
				int milli_second = random.nextInt(10);
				sleep(milli_second);
			}
		} catch(InterruptedException e) {
			System.out.printf("%s : stoped\n", name);
		}
		System.out.printf("%s : %d\n", name, sum);
		System.out.printf("%s : stopedn", name);
		 */

		for(int i=0; i<=count; i++) {
			sum = sum+i;
		}
		System.out.printf("%s: stoped\n", name);
	}
}

public class Exam0_345 {
	/*
	main은 스레드의 계산이 시작되기 전에 종료될 수 있다. 
	합계를 구하고 싶으면 스레드의 계산의 종료를 기다려야 한다. 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("main started");
		ThreadTH threadOne = new ThreadTH("one", 100);
		ThreadTH threadTwo = new ThreadTH("two", 100); 
		ThreadTH threadThree = new ThreadTH("three", 100);

		threadOne.start(); 
		threadTwo.start(); 
		threadThree.start();

		try { 
			// join()을 호출한 스레드는 실행이 종료될때까지 sleep 
			threadOne.join();
			threadTwo.join(); 
			threadThree.join();
		} catch (InterruptedException e) {
			System.out.println("interrupted"); 
		}

		// sum을 각 스레드의 계산이 다 끝난 후에 출력한다.
		System.out.printf("one: %d\n", threadOne.sum);
		System.out.printf("two: %d\n", threadTwo.sum); 
		System.out.printf("three: %d\n", threadThree.sum); 
		System.out.println("main stoped");
	}
}
