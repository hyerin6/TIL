package course11;

/*
 * 파일명: MyTreeMap2.java
 * 작성일: 2018.11.27
 * 작성자: 박혜린
 * 설명: String-String 쌍을 저장하는 저장하는 이진탐색트리를 연결자료구조로 구현한 클래스
 */

class MyTreeMap2 {
	// 트리의 노드를 표현하는 private 클래스 Node
	private class Node {
		String key;
		String value;
		Node leftChild = null;
		Node rightChild = null;
		Node(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	// private 인스턴스 변수 트리의 루트 노드를 가킬 변수(root)를 선언하고 null로 초기화
	private Node root = null;


	/*
	// 키값 key에 해당하는 value를 리턴하고, key 값을 찾지 못하면 null을 리턴하는 메소드(반복 알고리즘)
	public String get(String key) {
		Node p = root;
		while(p != null) {
			if(key.compareTo(p.key) < 0)
				p = p.leftChild;
			else if(key.compareTo(p.key) > 0)
				p = p.rightChild;
			else
				return p.value;
		}
		return null;
	}*/


	// (1) 위의 get 메소드 대신, 재귀 알고리즘을 이용한 get 메소드를 작성하시오.(위의 get 메소드는 삭제할 것)
	// 키값 key에 해당하는 value를 리턴하고, key 값을 찾지 못하면 null을 리턴하는 메소드
	public String get(String key) {
		return get(root, key);
	}

	public String get(Node p, String key) {
		if (p == null)
			return null;
		else if (key.compareTo(p.key) < 0)
			return get(p.leftChild, key);
		else if (key.compareTo(p.key) > 0)
			return get(p.rightChild, key);
		else
			return p.value;
	}

	/*
	// 트리에  key-value 쌍을 삽입하는 메소드
	public void put(String key, String value) {
		root = insertKey(root, key, value);
	}

	// p를 루트로 하는 트리에 key-value 쌍을 삽입 후 루트를 리턴하는 메소드(재귀 알고리즘)
	private Node insertKey(Node p, String key, String value) {
		if(p == null) {  
			Node newNode = new Node(key, value);
			return newNode; 
		}
		else if(key.compareTo(p.key) < 0) {   // 키 값이 p의 키값보다 작은 경우
			p.leftChild = insertKey(p.leftChild, key, value);
			return p;  // 루트 불변
		}
		else if(key.compareTo(p.key) > 0) { // 키 값이 p의 키값보다 큰 경우
			p.rightChild = insertKey(p.rightChild, key, value);
			return p;  // 루트 불변       
		}
		else {  // 키 값이 p의 키값과 같은 경우, 삽입 오류를 내지 말고 노드 p의 value를 새로운 값으로 갱신
			p.value = value;
			return p;   // 루트 불변
		}
	}  */

	// (2) (옵션) 위의 두 메소드 put, insertKey 대신, 반복 알고리즘을 이용한 put 메소드를 작성하시오.(위의 put, insertKey 메소드는 삭제할 것)
	public void put(String key, String value) {
		Node p = root; //
		Node r = null; // 해당 키가 없을 경우

		while (p != null) {
			if (key.compareTo(p.key) == 0) { // 이미 존재하는 경우
				p.value = value;
				break;
			}
			r = p;
			if (key.compareTo(p.key) < 0)
				p = p.leftChild;
			else
				p = p.rightChild;
		}
		Node newNode = new Node(key, value);
		if (root == null) // 빈Map
			root = newNode;
		else if (p == null) { // 노드가 존재하지 않을 경우
			if (key.compareTo(r.key) < 0)
				r.leftChild = newNode;
			else
				r.rightChild = newNode;
		}
	}
}
