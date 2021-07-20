package lab9_1;

public class Main {

	public static void main(String[] args) {
		System.out.println("lab9_1: 박혜린 \n");

		// 연결자료구조로 구현한 노드 7개 예제 트리를 만들고 메소드 호출
		System.out.println(" 연결자료구조 구현 트리1");
		MyLinkedTree tree7 = new  MyLinkedTree(7, null, null);
		MyLinkedTree tree6 = new  MyLinkedTree(6, null, null);
		MyLinkedTree tree5 = new  MyLinkedTree(5, null, null);
		MyLinkedTree tree4 = new  MyLinkedTree(4, null, null);
		MyLinkedTree tree3 = new  MyLinkedTree(3, tree6, tree7);
		MyLinkedTree tree2 = new  MyLinkedTree(2, tree4, tree5);
		MyLinkedTree linkedTree1 = new  MyLinkedTree(1, tree2, tree3);

		System.out.print("preorder:");
		linkedTree1.printPreorder(); // 전위순회하며 출력
		System.out.print("inorder:");
		linkedTree1.printInorder();  // 중위순회하며 출력
		System.out.print("left:");
		linkedTree1.printLeft(); // 왼쪽 자식들을 출력

		// 연결자료구조로 구현한 공백 예제 트리를 만들고 메소드 호출
		System.out.println(" 연결자료구조 구현 트리2");
		MyLinkedTree linkedTree2 = new  MyLinkedTree();

		System.out.print("preorder:");
		linkedTree2.printPreorder(); // 전위순회하며 출력
		System.out.print("inorder:");
		linkedTree2.printInorder();  // 중위순회하며 출력
		System.out.print("left:");
		linkedTree2.printLeft(); // 왼쪽 자식들을 출력

		// 배열로 구현한 노드 8개 예제 트리를 만들고 메소드 호출
		System.out.println(" 배열 구현 트리1");
		MyArrayTree arrayTree1 = new MyArrayTree(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
		System.out.print("preorder:");
		arrayTree1.printPreorder(); // 전위순회하며 출력
		System.out.print("inorder:");
		arrayTree1.printInorder(); // 중위순회하며 출력
		System.out.print("left:");
		arrayTree1.printLeft();  // 왼쪽 자식들을 출력

		// 배열로 구현한 공백 예제 트리를 만들고 메소드 호출
		System.out.println(" 배열 구현 트리2");
		MyArrayTree arrayTree2 = new MyArrayTree();
		System.out.print("preorder:");
		arrayTree2.printPreorder(); // 전위순회하며 출력
		System.out.print("inorder:");
		arrayTree2.printInorder(); // 중위순회하며 출력
		System.out.print("left:");
		arrayTree2.printLeft();  // 왼쪽 자식들을 출력
	}
}

