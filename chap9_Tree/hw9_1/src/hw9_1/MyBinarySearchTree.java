package hw9_1;

import java.util.NoSuchElementException;

public class MyBinarySearchTree {
	// (1) 트리의 노드를 표현하는 private 클래스 Node - 필드(int형 key, leftChild, rightChild), 생성자(key값을 매개변수로 받아 초기화)
	private class Node{
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
		if(root == null) {
			throw new NoSuchElementException();
		}
		Node t = root;
		while(t.rightChild != null) 
			t = t.rightChild;

		return t.key;  // 임시로 추가한 문장임
	}

	// 트리가 키값 key를 포함하는지 여부를 리턴하는 메소드
	public boolean contains(int key) {
		return search(root, key);
	}

	// (5) p를 루트로 하는 트리에 키값 key가 존재하는지 여부를 리턴하는 메소드(재귀 알고리즘)
	private boolean search(Node p, int key) {
		if(p != null) {
			if(p.key > key)
				return search(p.leftChild, key);
			else if(p.key < key)
				return search(p.rightChild, key);
			else return true; 
		}
		return false;  // 임시로 추가한 문장임
	}

	public boolean add(int key) {
		Node newNode = new Node(key);
		Node pointer; 
		boolean insertComplete = false;
		if (root == null) { // 트리가 empty 상태일 경우, 루트 노드에 삽입
			root = newNode;
			insertComplete = true;
		} else {
			pointer = root;
			// while 문 사용하여 탐색 - 삽입 성공할때까지
			while (!insertComplete) {
				if (pointer.key > key) {
					// 작으면 왼쪽으로 크면 오른쪽으로
					if (pointer.leftChild != null) {
						pointer = pointer.leftChild;
					} else {
						pointer.leftChild = newNode;
						insertComplete = true;
						break;
						// 삽입했으면 종료
					}
				} else if (pointer.key < key) {
					if (pointer.rightChild != null) {
						pointer = pointer.rightChild;
					} else {
						pointer.rightChild = newNode;
						insertComplete = true;
						break;
					}
				} else {
					// 이미 존재하면 종료
					break;
				}
			}
		}
		return insertComplete;
	}

	public boolean remove(int key) {
		Node pointer, parentPointer;
		pointer = parentPointer = root;

		// 삭제할 노드를 찾을 때까지 루핑으로 트리를 순회한다.
		while (pointer != null && pointer.key != key) {
			parentPointer = pointer; // parentPointer 인스턴스가 삭제할 노드의 부모 노드를 참조하도록 한다.
			if (pointer.key > key) {
				pointer = pointer.leftChild;
			} else {
				pointer = pointer.rightChild;
			}
		}

		if (pointer == null) { // 트리에 삭제할 노드가 존재하지 않을때
			return false;
		} else {
			// 제거할 노드가 루트 노드이고, 루트노드가 왼쪽 자식 노드를 가지지 않을 때
			if (pointer == root && pointer.leftChild == null) {
				root = root.rightChild;				
				// 루트 노드가 아닌 제거할 노드가 왼쪽 자식 노드를 가지지 않을 때
			} else if (pointer != root && pointer.leftChild == null) {
				if (pointer == parentPointer.leftChild) {
					parentPointer.leftChild = pointer.rightChild;
				} else {
					parentPointer.rightChild = pointer.rightChild;
				}
			} else { // 제거할 노드가 2개의 자식 노드 모두를 가지고 있을 때
				Node rightMostNode = pointer;

				while (rightMostNode.rightChild != null) {
					rightMostNode = rightMostNode.rightChild;
				}
				pointer.key = rightMostNode.key;
				rightMostNode = null;
			}
		}
		return true;
	}
}
