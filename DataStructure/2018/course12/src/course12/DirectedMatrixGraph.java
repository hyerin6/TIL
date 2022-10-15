package course12;
/*
 * 파일명: DirectedMatrixGraph.java
 * 작성일: 2018.12.3
 * 작성자: 박혜린
 * 설명: 인접행렬을 이용하여 방향 그래프를 구현한 클래스
 */
public class DirectedMatrixGraph {
	private int[][] matrix;  // 인접 행렬
	private int n;    // 정점 수

	// 정점 0, 1, 2, ..., n-1 인 그래프를 생성
	public DirectedMatrixGraph(int n) {
		matrix = new int[n][n];
		this.n = n;
	}

	// 정점 v의 진입차수를 구하여 리턴하는 메소드 ************* (1)
	public int inDegree(int v) {
		int count = 0;
		for(int i = 0; i < matrix.length; ++i)
			if(matrix[i][v] == 1)
				count++;
		return count;    
	}

	// 간선 (v1, v2) 존재 여부를 검사 - 정점 번호가 잘못된 경우는 예외 발생
	public boolean hasEdge(int v1, int v2) {
		return matrix[v1][v2] == 1;
	}

	// 간선 (v1, v2) 를 삽입
	public void addEdge(int v1, int v2) {
		if(!isValid(v1) || !isValid(v2)) {
			System.out.println("간선 삽입 오류 - 잘못된 정점 번호입니다. <" + v1 + ", " + v2 + ">");
		}
		else if(hasEdge(v1, v2)) {
			System.out.println("간선 삽입 오류 - 이미 존재하는 간선입니다. <" + v1 + ", " + v2 + ">");
		}
		else {
			matrix[v1][v2] = 1;
		}
	}

	// 정점 번호의 유효성을 검사
	private boolean isValid(int v) {
		return v >= 0 && v < n;
	}
}

