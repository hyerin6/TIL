package course7;

public class MyArrayQueueTest {
	public static void main(String[] args) {
		System.out.println("course7: 박혜린");

		// MyArrayQueue 객체를 생성하고, 삽입, 삭제 연산을 수행
		MyArrayQueue queue = new MyArrayQueue(5);
 
		queue.enQueue(1);
		queue.enQueue(2);

		System.out.println(queue.deQueue()); // 출력: 1
		System.out.println(queue.deQueue()); // 출력: 2

		queue.enQueue(3);
		queue.enQueue(4);

		System.out.println(queue.deQueue()); // 출력: 3
		System.out.println(queue.deQueue()); // 출력: 4

		queue.enQueue(5);
		queue.enQueue(6);
		queue.enQueue(7);
		queue.enQueue(8);
		queue.enQueue(9); // 삽입 실패하여 오류 메시지 출력할 것임
		queue.enQueue(10); // 삽입 실패하여 오류 메시지 출력할 것임

		while(!queue.isEmpty())
			System.out.print(queue.deQueue() + " "); // 출력: 5 6 7 8
		System.out.println();

		System.out.println(queue.deQueue()); // 삭제 실패하여 예외발생할 것임

		System.out.println("실행하지 않을 문장임");
	}
}