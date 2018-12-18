import java.util.Scanner;

//방향 그래프를 생성하고 이용하는 프로그램
public class GraphTest { 
	public static void main(String[] args) { 
		System.out.println("final : 박혜린\n");

		// 정점 수 n 입력
		Scanner scan = new Scanner(System.in);
		System.out.print("정점 수 입력: ");
		int n = scan.nextInt();

		// 정점 수가 n인 방향 그래프 graph1과 graph2를 생성
		MyMatrixGraph graph1 = new MyMatrixGraph(n);
		MyListGraph graph2 = new MyListGraph(n);

		// 간선 수 e 입력
		System.out.print("간선 수 입력: ");
		int e = scan.nextInt();

		// e개의 간선(정점 쌍)을 입력받아 graph1과 graph2에 삽입
		System.out.println(e + "개의 간선을 입력하세요(각 간선은 정점 번호 2개를 whitespace로 구분하여 입력):");
		for(int i = 0; i < e; i++) {
			int v1 = scan.nextInt();
			int v2 = scan.nextInt();
			graph1.addEdge(v1, v2);
			graph2.addEdge(v1, v2);
		}

		// graph1 각 정점의 out-degree를 출력
		System.out.println("\ngraph1");
		for(int i = 0; i < n; i++) {
			System.out.println("정점 " + i + "의 진출차수 = " + graph1.outDegree(i));		
		}

		// graph2 각 정점의 out-degree를 출력
		System.out.println("\ngraph2");
		for(int i = 0; i < n; i++) {
			System.out.println("정점 " + i + "의 진출차수 = " + graph2.outDegree(i));		
		}
	}
}