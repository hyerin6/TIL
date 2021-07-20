package hw8_2;
import java.util.Scanner;

// int형 큐를 단순연결리스트로 구현하고 사용하는 프로그램을 작성하시오.

public class MyLinkedQueueTest {
	public static void main(String[] args) {
		System.out.println("hw8_2 : 박혜린 \n");
		
		Scanner scan = new Scanner(System.in);
	
		int menu =  0;
		MyLinkedQueue q = new MyLinkedQueue();

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
