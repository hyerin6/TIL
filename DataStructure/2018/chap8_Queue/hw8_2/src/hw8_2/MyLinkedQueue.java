package hw8_2;

import java.util.NoSuchElementException;

// int형 큐를 단순연결리스트로 구현하고 사용하는 프로그램을 작성하시오.

public class MyLinkedQueue {
	class Node{
		Node link;
		int data;
	}

	private Node front;
	private Node rear;
	private int itemCount;

	// 생성자
	public MyLinkedQueue(){ 
		this.front = null;
		this.rear = null;
	}

	public boolean isEmpty() {
		return front == null;
	}

	public void enQueue(int data) {
		Node newNode = new Node();
		newNode.data = data;
		itemCount++;

		if(isEmpty()) {
			front = newNode;
			rear = newNode;
		}
		else {
			rear.link = newNode;
			rear = newNode;
		}
	}

	public int deQueue() {
		if(isEmpty()) throw new NoSuchElementException();
		else {
			int data = front.data;
			front = front.link;
			itemCount--;
			return data;
		}
	}

	public int peek() {
		if(isEmpty()) throw new NoSuchElementException();
		else return front.data;
	}

	public int size() {
		return itemCount;
	}
}
