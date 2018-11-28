/*
 * 파일명: UndirectedListGraph.java
 * 작성일: 2018.11.22
 * 작성자: 박혜린
 * 설명: 무방향 그래프를 생성하고 이용하는 프로그램
 */
package lab10_2;

public class UndirectedListGraph {
	private class Node{
		int vertex;
		Node link;

		Node(int vertex){
			this.vertex  = vertex;
		}
	}

	private Node[] list;
	private int n; // 정점 수를 저장하는 변수

	// 정점수 n을 매개변수로 받아 정점은 n개이고 간선은 없는 초기 그래프를 생성
	public UndirectedListGraph(int n) {
		this.n = n;
		this.list = new Node[n];
	}

	public boolean hasEdge(int v1, int v2) {
		Node t = list[v1];
		while(t != null) {
			if(t.vertex == v2) return true;
			t = t.link;
		}
		return false;
	}

	public boolean isValid(int v) {
		if(v < 0 || v > n-1) return false;
		return true;
	}

	public void addEdge(int v1, int v2) {
		// 간선 삽입 오류 발생은 무시하면 됨. 예를 들어 7개의 간선 삽입 과정에서 2개가 오류이면 5개만 삽입하면 됨
		if(!isValid(v1) || !isValid(v2)) 
			System.out.println("잘못된 정점 번호입니다.");
		else if(hasEdge(v1, v2))
			System.out.print(String.format("이미 존재하는 간선입니다. (%d, %d)\n", v1, v2));
		// 단순연결 리스트 addFirst - Null, notNull 전부 실행 가능
		else { 
			Node n1 = new Node(v1);
			n1.link = list[v2];
			list[v2] = n1;

			Node n2 = new Node(v2);
			n2.link = list[v1];
			list[v1] = n2;
		}
	}

	public void printAdjacentVertices(int v) {
		Node t = list[v];
		while(t != null) {
			System.out.print(t.vertex + " ");
			t = t.link;
		}System.out.println();
	}
}
