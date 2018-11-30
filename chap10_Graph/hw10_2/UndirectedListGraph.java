package hw10_2;

public class UndirectedListGraph {
	private class Node{
		int vertex;
		Node link;

		Node(int vertex){
			this.vertex = vertex;
		}
	}

	private Node[] list;
	private int n; // 정점 수를 저장하는 변수
	private int[] visited; 


	public UndirectedListGraph(int n) {
		this.list = new Node[n];
		this.n = n;
	}

	// 방향이 없기 때문에 v1과 v2중 하나만 검사하면됨
	public boolean hasEdge(int v1, int v2) {
		if(!isValid(v1) || !isValid(v2)) 
			System.out.println("잘못된 정점입니다.");

		Node t = list[v1];
		while(t != null) {
			if(t.vertex == v2) return true;
			t = t.link;
		}
		return false;
	}

	public boolean isValid(int v) {
		if(v < 0 || v >= n) return false;
		return true;
	}


	public void addEdge(int v1, int v2) {
		if(!isValid(v1) || !isValid(v2)) 
			System.out.print(String.format("잘못된 정점 번호입니다. <%d, %d>\n", v1, v2));
		else if(hasEdge(v1, v2))
			System.out.print(String.format("이미 존재하는 간선입니다. <%d, %d>\n", v1, v2));

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
		if(!isValid(v)) 
			System.out.println("잘못된 정점입니다.");

		Node t = list[v];
		while(t != null) {
			System.out.print(t.vertex + " ");
			t = t.link;
		}System.out.println();
	}

	//  breadthFirstSearch : 시작 정점 v를 매개변수로 받아 v를 시작으로 그래프를 너비우선탐색 
	public void breadthFirstSearch(int v) {
		// (1) 잘못된 정점 번호인 경우 오류 메시지 출력
		if(!isValid(v)) System.out.println("잘못된 정점입니다.");

		else {
			// (2) visited 배열 초기화
			visited = new int[n];
			for(int i = 0; i < n; ++i) 
				visited[i] = 0; 		

			// (3) v를 시작 정점으로 그래프를 너비우선 탐색하며 순서대로 정점 번호를 출력
			visited[v] = 1;
			for(int i = 0; i < n; ++i) {
				// v에 인접한 정점중에 방문한 것 출력
				if(hasEdge(v, i) && visited[i] == 0)  {
					System.out.printf("%d -> %d\n", v, i);
				}
			}

		}
	}
}
