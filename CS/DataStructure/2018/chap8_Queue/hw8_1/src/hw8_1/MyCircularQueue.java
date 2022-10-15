/*
 * 파일명 : MyCircularQueue.java
 * 작성자 : 박혜린 
 * 작성일 : 11/1
 * 목적 : 배열을 이용한 원형 큐 구현을 연습한다.
 ****** 배열의 원소 수 만큼 저장 가능한 원형 큐를 구현 ******
 */
package hw8_1;

import java.util.NoSuchElementException;

public class MyCircularQueue {
	private int[] array;
	private int arraySize;
	private int itemCount; // 배열의 크기만큼 저장하기 위해 원소수를 count 한다.
	private int front;
	private int rear;

	// 생성자 : 배열로 큐를 구현하도록 인스턴스 변수들을 초기화
	public MyCircularQueue(int size) {
		this.array = new int[size];
		this.arraySize = size;
		this.itemCount = 0;
		this.front = 0;
		this.rear = 0;
	}

	// 큐가 비어있는지를 검사
	public boolean isEmpty() {
		return itemCount == 0;
	}

	// 큐가 가득찼는지를 검사
	public boolean isFull() {
		return itemCount == arraySize;
	}

	// 큐에 data를 삽입
	public void enQueue(int data) {
		if(isFull())
			System.out.println("큐가 가득차서 삽입 실패: " + data);
		else {
			rear = (rear+1) % arraySize;
			array[rear] = data;
			itemCount++;
		}
	}

	// 큐에서 원소를 하나 삭제하여 리턴
	public int deQueue() {
		if(isEmpty())
			throw new NoSuchElementException();
		else {
			front = (front+1) % arraySize;
			itemCount--;
			return array[front];
		}
	}

	// 맨앞 원소 반환
	public int peek() {
		if(isEmpty())
			throw new NoSuchElementException();
		else {
			return array[(front+1)%arraySize];
		}
	}

	// 배열의 크기 반환
	public int size() {
		return arraySize;
	}
}