package hw10_1;

/*
 * 파일명: DirectedMatrixGraph.java
 * 작성자: 박혜린
 * 작성일: 2018.11.29
 * 프로그램 설명: adjacency matrix로 구현한 directed graph의 깊이우선탐색을 이해한다.
 */

public class DirectedMatrixGraph {
	private int[][] matrix; // 인접 행렬
	private int n; // 정점 개수
	private int[] visited; 

	public DirectedMatrixGraph(int n) {
		this.matrix = new int[n][n];
		this.n = n;
	}

	public boolean hasEdge(int v1, int v2) {
		if(!isValid(v1) || !isValid(v2)) 
			System.out.println("잘못된 정점입니다.");
		else if(matrix[v1][v2] == 1) return true;
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
		else 
			matrix[v1][v2] = 1;
	}

	public void printAdjacentVertices(int v) {
		if(!isValid(v)) 
			System.out.println("잘못된 정점입니다.");
		else {
			for(int i = 0; i < n; ++i) {
				if(matrix[v][i] == 1)
					System.out.print(i + " ");
			} System.out.println();
		}
	}

	//  depthFirstSearch : 시작 정점 v를 매개변수로 받아 v를 시작으로 dfs를 호출하는 메소드
	// 전체 그래프를 탐색하는 것이 아니라 v와 연결된 부분만 탐색 (재귀 알고리즘)
	public void depthFirstSearch(int v) {
		// (1) 잘못된 정점 번호인 경우 오류 메시지 출력
		if(!isValid(v)) System.out.println("잘못된 정점입니다.");

		else {
			// (2) visited 배열 초기화
			visited = new int[n];
			for(int i = 0; i < n; ++i) 
				visited[i] = 0; 		

			// (3) dfs 메소드 호출
			dfs(v);
		}
	}

	private void dfs(int v) {
		// v 방문
		visited[v] = 1;

		// 출력
		for(int i = 0; i < n; ++i) {
			// v에 인접한 정점중에 방문한 것 출력
			if(matrix[v][i] == 1 && visited[i] == 0)  {
				System.out.printf("%d -> %d\n", v, i);
			}
		}
	}
}
