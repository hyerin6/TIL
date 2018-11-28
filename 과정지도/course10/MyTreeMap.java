package course10;
public class MyTreeMap {
	// 트리의 노드를 표현하는 private 클래스 Node - 필드(String형 key, String형 value, leftChild, rightChild), 생성자(key, value를 매개변수로 받아 초기화)
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

	// 트리에 쌍을 삽입하는 메소드
	public void put(String key, String value) {
		root = insertKey(root, key, value);
	}

	// (3) p를 루트로 하는 트리에 쌍을 삽입 후 루트를 리턴하는 메소드(recursive algorithm)
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
		else {  // 키 값이 p의 키값과 같은 경우, 삽입 오류를 내지 않고 노드 p의 value를 새로운 값으로 갱신
			p.value = value;
			return p;   // 루트 불변
		}
	}  

	// (4) 트리에서 키값 key를 찾아 그에 해당하는 value를 리턴하는 메소드
	//     key 값을 찾지 못하면 null을 리턴할 것
	//     ******* 반드시 (반복분)iterative method로 작성할 것.  즉, (재귀)recursive algorithm 사용하지 말 것
	public String get(String key) {
		Node t = root;

		while(t != null) {
			if (key.compareTo(t.key) < 0) 
				t = t.leftChild;
			else if (key.compareTo(t.key) > 0) 
				t = t.rightChild;
			else 
				return t.value;
		}

		return null; // 반복문이 실행하는 동안 찾지못하면 없는 것 - null
	}
}

