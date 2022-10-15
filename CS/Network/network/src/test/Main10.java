package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 서버의 전송 작업(클라이언트에게 전송하는 작업)을 멀티 쓰레드로 구현
public class Main10 {
	public static void main(String[] args) {
		final int PORT = 13, BACKLOG = 200;
		// 연결 요청이 동시에 너무 많이 몰려와도, 쓰레드(실행흐름)의 수는 200 개로 고정
		try (ServerSocket serverSocket = new ServerSocket(PORT, BACKLOG)) {
			ExecutorService executor = Executors.newFixedThreadPool(200);
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					executor.submit(new Task(socket));
				} catch (IOException ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	static class Task implements Runnable {
		Socket socket;

		public Task(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				socket.close();
			} catch (Exception ex) {
			}
		}
	}
}
