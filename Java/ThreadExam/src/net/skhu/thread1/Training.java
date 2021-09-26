package net.skhu.thread1;

class SharedObject{
	int sum = 0;
	synchronized void add(int i) {
		sum = sum + i;
	}
	synchronized void sub(int i) {
		sum = sum - i;
	}
}

class ThreadADD extends Thread{
	String name; 
	int count; 
	SharedObject sharedObj;

	public ThreadADD(String name, int count, SharedObject sharedObj) {
		this.sharedObj = sharedObj;
		this.name = name;
		this.count = count; 
	}

	@Override
	public void run() {
		System.out.printf("%s: started\n", name);
		for (int i = 1; i <= count; ++i) 
			sharedObj.add(i);
	}
}

class ThreadSUB extends Thread{
	String name; 
	int count; 
	SharedObject sharedObj;

	public ThreadSUB(String name, int count, SharedObject sharedObj) {
		this.sharedObj = sharedObj;
		this.name = name;
		this.count = count; 
	}

	@Override
	public void run() {
		System.out.printf("%s: started\n", name);
		for (int i = 1; i <= count; ++i) 
			sharedObj.sub(i); 
	}
}

public class Training {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SharedObject sharedobj = new SharedObject();

		ThreadADD t1 = new ThreadADD("Thread1", 100, sharedobj);
		ThreadSUB t2 = new ThreadSUB("Thread2", 100, sharedobj);

		t1.start();
		t2.start();

		try { 
			t1.join(); 
			t2.join(); 
		} catch (InterruptedException e) { }

		System.out.println("sum = " + sharedobj.sum);
	}

}
