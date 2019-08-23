package hw10_2;
/*
 * 파일명: UndirectedListGraphTest.java
 * 작성일: 2018.12.01
 * 작성자: 박혜린
 * 설명: 무방향 그래프를 생성하고 이용하는 프로그램
 */

import java.util.Scanner;

public class UndirectedListGraphTest {
	public static void main(String[] args) {
		System.out.println("hw10_2 : 박혜린 \n");

		// 정점 수 n 입력
		Scanner scan = new Scanner(System.in);
		System.out.print("정점 수 입력: ");
		int n = scan.nextInt();

		// 정점 수가 n인 무방향 그래프를 생성
		UndirectedListGraph graph = new UndirectedListGraph(n);

		// 간선 수 e 입력
		System.out.print("간선 수 입력: ");
		int e = scan.nextInt();

		// e개의 간선(정점 쌍)을 입력받아 그래프에 삽입
		System.out.println(e + "개의 간선을 입력하세요(각 간선은 정점 번호 2개를 whitespace로 구분하여 입력):");
		for(int i = 0; i < e; i++) {
			int v1 = scan.nextInt();
			int v2 = scan.nextInt();
			graph.addEdge(v1, v2);
		}

		// 각 정점의 인접 정점들을 출력
		System.out.println();
		for(int i = 0; i < n; i++) {
			System.out.print("정점 " + i + "에 인접한 정점 = ");
			graph.printAdjacentVertices(i);
		}

		// 각 정점을 시작정점으로 한 너비우선탐색 결과를 출력 
		for(int i = 0; i < n; i++)
			graph.breadthFirstSearch(i);
	}
}