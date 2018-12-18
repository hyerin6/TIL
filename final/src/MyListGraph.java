public class MyListGraph {
	private class Node {
		int vertex;
		Node link;
		Node(int vertex) {
			this.vertex = vertex;
		}
	}
	private Node[] list;	// 인접 리스트
	private int n;		// 정점 수

	// 정점 0, 1, 2, ..., n-1 인 그래프를 생성
	public MyListGraph(int n) {
		list = new Node[n];
		this.n = n;
	}

	// (1) 정점 v의 진출 차수를 구하여 리턴 (정점 번호 v의 유효성 검사할 필요 없음)
	public int outDegree(int v) {
		int count = 0;

		for(int i = 0; i < list.length; i++) {
			Node p = list[v];
			while(p != null) {
				if(p.vertex == i)
					count++;
				p = p.link;
			}
		}
		return count;
	}

	// 간선 (v1, v2)를 삽입
	public void addEdge(int v1, int v2) {
		Node newNode = new Node(v2);
		newNode.link = list[v1];
		list[v1] = newNode;
	}
}