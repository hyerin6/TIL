/*
 * 파일명: DirectedMatrixGraph.java
 * 작성일: 2018.11.22
 * 작성자: 박혜린
 * 설명: 방향 그래프를 생성하고 이용하는 프로그램
 */
package lab10_1;

public class DirectedMatrixGraph {
	private int[][] matrix; // 인접 행렬
	private int n; // vertex 개수

	public DirectedMatrixGraph(int n) {
		this.matrix = new int[n][n];
		this.n= n;
	}

	public boolean isValid(int v) {
		if(v < 0 || v > n-1) return false;
		return true;
	}

	public boolean hasEdge(int v1, int v2) {
		if(matrix[v1][v2] == 1) return true;
		return false;
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
		for(int i = 0; i < n; i++) 
			if(matrix[v][i] == 1)
				System.out.print(i + " ");
		System.out.println();

	}
}
