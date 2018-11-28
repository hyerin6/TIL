package lab8_1;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedQueue {
	private final Lock lock = new ReentrantLock();
	private final Condition notFull  = lock.newCondition();
	private final Condition notEmpty = lock.newCondition();
	private int[] array;
	private int arraySize;
	private int front = 0;
	private int rear = 0;

	public SynchronizedQueue(int arraySize) {
		this.arraySize = arraySize;
		array = new int[arraySize];
	}

	public boolean isEmpty() {
		return front == rear;
	}

	public boolean isFull() {
		return (rear + 1) % arraySize == front;
	}

	public void enQueue(int item) throws InterruptedException {
		lock.lock();
		try {
			while (isFull()) {
				System.out.println(Thread.currentThread().getName() + ": 큐가 가득차서 기다립니다.");
				notFull.await();    
			}
			rear = (rear + 1) % arraySize;
			array[rear] = item;
			System.out.println(Thread.currentThread().getName() + ": 큐 삽입 " + item);
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}

	public int deQueue() throws InterruptedException {
		lock.lock();
		try {
			while (isEmpty()) {
				System.out.println(" " + Thread.currentThread().getName() + ": 큐가 비어서 기다립니다.");
				notEmpty.await();
			}
			front = (front + 1) % arraySize;
			int item = array[front];
			System.out.println(" " + Thread.currentThread().getName() + ": 큐 삭제 " + item);
			notFull.signal();
			return item;
		} finally {
			lock.unlock();
		}
	}
}
