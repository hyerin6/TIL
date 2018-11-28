package course9;

public class MyTreeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("course9: 박혜린 \n");

		// 노드가 7개인 예제 트리를 만들고 메소드 호출
		System.out.println(" 트리1");
		MyTree tree7 = new MyTree(70, null, null);
		MyTree tree6 = new MyTree(60, null, null);
		MyTree tree5 = new MyTree(50, null, null);
		MyTree tree4 = new MyTree(40, null, null);
		MyTree tree3 = new MyTree(30, tree6, tree7);
		MyTree tree2 = new MyTree(20, tree4, tree5);
		MyTree myTree1 = new MyTree(10, tree2, tree3);

		System.out.println("노드 갯수 = " + myTree1.size());  // 출력: 7
		System.out.println("오른쪽 최하위  = " + myTree1.getRight()); // 출력: 70

		// 노드가 하나인 예제 트리를 만들고 메소드 호출
		System.out.println(" 트리2");
		MyTree myTree2 = new MyTree(99, null, null);

		System.out.println("노드 갯수 = " + myTree2.size());  // 출력: 1
		System.out.println("오른쪽 최하위  = " + myTree2.getRight()); // 출력: 99

		// 공백 예제 트리를 만들고 메소드 호출
		System.out.println(" 트리3");
		MyTree myTree3 = new MyTree();

		System.out.println("노드 갯수 = " + myTree3.size());  // 출력: 0
		System.out.println("오른쪽 최하위  = " + myTree3.getRight()); // 예외 발생
	}
}

