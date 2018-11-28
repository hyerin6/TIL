package hw9_1;

import java.util.NoSuchElementException;

public class MyBinarySearchTree {
	// (1) 트리의 노드를 표현하는 private 클래스 Node - 필드(int형 key, leftChild, rightChild), 생성자(key값을 매개변수로 받아 초기화)
	class Node{
		int key;
		Node leftChild;
		Node rightChild;

		public Node(int key) {
			this.key = key;
			this.leftChild = null;
			this.rightChild = null;
		}
	}

	// (2) private 인스턴스 변수 트리의 루트 노드를 가킬 변수(root)를 선언하고 null로 초기화
	private Node root = null;

	// 트리에 키값 key를 삽입하는 메소드
	public void insert(int key) {
		root = insertKey(root, key);
	}

	// p를 루트로 하는 트리에 키값 key를 삽입하고, 삽입 후 루트를 리턴하는 메소드(재귀 알고리즘)
	private Node insertKey(Node p, int key) {
		if(p == null) {  
			Node newNode = new Node(key);
			return newNode; 
		}
		else if(key < p.key) {
			p.leftChild = insertKey(p.leftChild, key);
			return p;  // 루트 불변
		}
		else if(key > p.key) {
			p.rightChild = insertKey(p.rightChild, key);
			return p;  // 루트 불변 
		}
		else {  // key = p.key 인 경우 삽입 실패
			System.out.println("삽입 실패. 중복된 키값이 존재합니다: " + key);
			return p;   // 루트 불변
		}
	}  

	// 트리를 중위순회하며 출력하는 메소드
	public void printInorder() {
		inorder(root);
		System.out.println();
	}

	// (3) p를 루트로 하는 트리를 중위 순회하며 키값을 출력하는 메소드(재귀 알고리즘)
	private void inorder(Node p) {
		if(p != null) {
			inorder(p.leftChild);
			System.out.print(p.key+" ");
			inorder(p.rightChild);
		}
	}

	// (4) 트리의 최대 키값을 리턴하는 메소드(반복 알고리즘) - 공백 트리인 경우 NoSuchElementException 예외 발생
	public int max() {
		if(root == null) 
			throw new NoSuchElementException();

		Node t = root;
		while(t.rightChild != null) {
			t = t.rightChild;
		}
		return t.key;
	}

	// 트리가 키값 key를 포함하는지 여부를 리턴하는 메소드
	public boolean contains(int key) {
		return search(root, key);
	}

	// (5) p를 루트로 하는 트리에 키값 key가 존재하는지 여부를 리턴하는 메소드(재귀 알고리즘)
	private boolean search(Node p, int key) {
		Node t = root;
		while(t != null) {
			if(t.key == key) return true;
			else if(t.key > key)
				t = t.leftChild;
			else 
				t = t.rightChild;
		}
		return false; 
		/*
		 * 반복 알고리즘
		 Node t = root;
		 while(t != null) {
			if(t.key == key) return true;
			else if(t.key > key)
				t = t.leftChild;
			else 
				t = t.rightChild;
		}
		return false;
		 */
	}

	// (6) 트리에 키값 key를 삽입하는 메소드(반복 알고리즘) - 삽입 성공여부(true/false)를 리턴
	public boolean add(int key) {
		Node t = root;
		Node p = null;

		while(t != null) {
			p = t;
			if(key == t.key) return false;
			if(key > t.key)
				t = t.rightChild;
			else 
				t = t.leftChild;
		}

		Node newNode = new Node(key);
		if(p == null) root = newNode;
		else if(p.key > key) p.leftChild = newNode;
		else p.rightChild = newNode;
		return true;
	}

	public boolean remove(int item) {
		Node t, parent;
		t = parent = root;

		// 삭제할 노드와 부모 노드를 찾아서 t와 p에 저장해줍니다.
		while (t != null && t.key != item) {
			parent = t; // parentPointer 인스턴스가 삭제할 노드의 부모 노드를 참조하도록 한다.
			if (t.key > item) {
				t = t.leftChild;
			} else {
				t = t.rightChild;
			}
		}

		if (t == null) // 트리에 삭제할 노드가 존재하지 않을때
			return false;

		else{
			// 제거할 노드가 루트 노드이고, 루트노드가 왼쪽 자식 노드를 가지지 않을 때
			if(t == root && t.leftChild == null)
				root = root.rightChild;

			// 루트 노드가 아닌 제거할 노드가 왼쪽 자식 노드를 가지지 않을 때
			else if(t != root && t.leftChild == null) {
				if(t == parent.leftChild)
					parent.leftChild = t.rightChild;
				else 
					parent.rightChild = t.rightChild;
			}

			// 제거할 노드가 2개의 자식 노드 모두를 가지고 있을 때
			else {
				Node rightMostNode = t;

				while (rightMostNode.rightChild != null) {
					rightMostNode = rightMostNode.rightChild;
				}
				t.key = rightMostNode.key;
				rightMostNode = null;
			}
		}
		return true;
	}
}