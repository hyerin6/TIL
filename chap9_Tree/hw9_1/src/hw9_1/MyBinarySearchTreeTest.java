package hw9_1;


import java.util.NoSuchElementException;
import java.util.Scanner;

public class MyBinarySearchTreeTest {
	public static void main(String[] args) {
		System.out.println("hw9_1 : 박혜린 \n");

		// 공백 이진탐색트리 tree를 생성
		MyBinarySearchTree tree = new MyBinarySearchTree();

		Scanner scan = new Scanner(System.in);
		System.out.println("메뉴 번호를 입력하세요.");
		int menu = 0;
		int item = 0;

		// 종료를 선택할 때까지 반복하여 메뉴를 제공하고 그에 맞는 이진탐색트리 연산을 수행
		do {
			//System.out.print("1:재귀삽입  2:중위순회  3:최대값  4:검색  5:삽입  7:종료 --->");

			System.out.print("1:재귀삽입  2:중위순회  3:최대값  4:검색  5:삽입  6:삭제  7:종료 --->");  // 보너스 과제를 수행한 경우 이용하세요.

			menu = scan.nextInt();
			switch(menu) {
			case 1:
				System.out.println("재귀 알고리즘을 이용한 삽입을 수행합니다.");
				System.out.print("삽입할 정수 입력:");  
				item = scan.nextInt(); 
				tree.insert(item);
				break;    
			case 2:
				System.out.println("중위순회 출력을 수행합니다.");
				tree.printInorder();  
				break;
			case 3:
				System.out.println("최대값 조회를 수행합니다.");
				try {
					System.out.println(tree.max());   
				} catch(NoSuchElementException e) {
					System.out.println("최대값 조회 실패");
				}
				break;
			case 4:
				System.out.println("검색을 수행합니다.");
				System.out.print("검색할 정수 입력:");
				item = scan.nextInt();
				if(tree.contains(item))    
					System.out.println("검색 성공");
				else
					System.out.println("검색 실패");
				break;
			case 5:
				System.out.println("반복 알고리즘을 이용한 삽입을 수행합니다.");
				System.out.print("삽입할 정수 입력:");
				item = scan.nextInt();
				if(tree.add(item))    
					System.out.println("삽입 성공");  
				else
					System.out.println("삽입 실패");
				break;
			case 6:
				System.out.println("삭제를 수행합니다.");
				System.out.print("삭제할 정수 입력:");
				item = scan.nextInt();
				if(tree.remove(item))    
					System.out.println("삭제 성공");
				else
					System.out.println("삭제 실패");
				break;
			case 7:
				System.out.println("종료합니다.");
				break;
			default:
				System.out.println("메뉴 번호 오류: 메뉴를 다시 선택하세요.");
			}
		} while(menu != 7);
	}
}

