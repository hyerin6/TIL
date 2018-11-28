import java.util.NoSuchElementException; 

public class MyDQue {
	class Node{
		int data;
		Node llink;
		Node rlink;
	}

	private Node front;
	private Node rear;

	public MyDQue() { // 생성자 - front = rear = null
		front = null;
		rear = null;
	}

	public boolean isEmpty() {
		return front == null;
	}

	public void insertFront(int data) {
		Node newNode = new Node();
		newNode.data = data;

		if(isEmpty()) {
			front = newNode;
			rear = newNode;
		}else {
			front.llink = newNode;
			newNode.rlink = front;
			newNode.llink = null;
			front = newNode;
		}
	}

	public void insertRear(int data) {
		Node newNode = new Node();
		newNode.data = data;

		if(isEmpty()) {
			front = newNode;
			rear = newNode;
		}else {
			rear.rlink = newNode;
			newNode.llink = rear;
			newNode.rlink = null;
			rear = newNode;
		}
	}

	public int deleteFront() {
		if(isEmpty()) 
			throw new NoSuchElementException();
		else {
			int data = front.data;
			front = front.rlink;
			// front.llink = null;
			return data;
		}
	}

	public int deleteRear() {
		if(isEmpty()) 
			throw new NoSuchElementException();
		else {
			int data = rear.data;
			rear.llink.rlink = null;
			return data;
		}
	}

	public int peekFront() {
		if(isEmpty()) 
			throw new NoSuchElementException();
		return front.data;
	}

	public int peekRear() {
		if(isEmpty()) 
			throw new NoSuchElementException();
		return rear.data;
	}

	public void print() {
		if(isEmpty()) System.out.println("비었다..");
		else {
			Node t = front;
			System.out.print("DQueue>> ");
			while(t != null) {
				System.out.print(t.data + " ");
				t = t.rlink;
			}System.out.println();
		}
	}
}
