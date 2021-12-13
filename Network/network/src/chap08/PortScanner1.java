package chap08;

import java.net.Socket;

public class PortScanner1 {

	public static void main(String[] args) {
		String host = "localhost";
		int portFrom = 1, portTo = 10000;

		for (int port = portFrom; port <= portTo; ++port) {
			System.out.printf("%s %d ", host, port);
			// 소켓 자원을 할당받고, 호스트에 연결을 시도
			try (Socket socket = new Socket(host, port)) {
				System.out.println("연결 성공");
			} catch (Exception e) {
				// 연결할 수 없다면 java.net.ConnectException 예외가 발생
				System.out.println(e.getClass().getName() + " " + e.getMessage());
			}
		}
	}

}
