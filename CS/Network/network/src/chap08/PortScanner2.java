package chap08;

import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortScanner2 {

	static class PortTask implements Runnable {
		String host;
		int port;

		public PortTask(String host, int port) {
			this.host = host;
			this.port = port;
		}

		@Override
		public void run() {
			System.out.printf("%s %d ", host, port);
			try (Socket socket = new Socket(host, port)) {
				System.out.println("연결 성공");
			} catch (ConnectException e) {
				// 연결할 수 없다
			} catch (Exception e) {
				System.out.println(e.getClass().getName() + " " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		String host = "localhost";
		int portFrom = 1, portTo = 10000;

		// ExecutorService를 이용하여 쓰레드 1000개를 만들어서 동시에 연결을 시도
		ExecutorService executor = Executors.newFixedThreadPool(1000);
		for (int port = portFrom; port <= portTo; ++port) {
			executor.submit(new PortTask(host, port));
		}
	}

}
