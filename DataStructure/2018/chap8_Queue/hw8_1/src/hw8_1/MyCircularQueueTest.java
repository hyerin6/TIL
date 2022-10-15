/*
 * 파일명 : MyCircularQueueTest.java
 * 작성자 : 박혜린 
 * 작성일 : 11/1
 * 목적 : 배열을 이용한 원형 큐 구현을 연습한다.
 */
package hw8_1;
import java.util.Scanner;
public class MyCircularQueueTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hw8_1 : 박혜린 \n");
		Scanner scan = new Scanner(System.in);
		int menu =  0;

		MyCircularQueue q = new MyCircularQueue(5);

		while(menu != 6) {
			System.out.print("1:삽입 2:삭제 3:조회 4:크기 5:전체삭제 6:종료 --> ");
			menu = scan.nextInt();
			switch(menu) {
			case 1:
				System.out.print("정수 입력: ");
				int item = scan.nextInt();
				q.enQueue(item);
				break;
			case 2:
				System.out.println(q.deQueue());
				break;
			case 3:
				System.out.println(q.peek());
				break;
			case 4:
				System.out.println(q.size());
				break;
			case 5:
				while(!q.isEmpty()) 
					System.out.println(q.deQueue());
				break;
			case 6:
				System.out.println("종료합니다.");
				break;

			}	
		}
	}

}
