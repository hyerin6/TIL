package chap09;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DaytimeServer5c {

	public static void main(String[] args) {
		final int PORT = 13, BACKLOG = 200;
		try (ServerSocket serverSocket = new ServerSocket(PORT, BACKLOG)) {
			// 연결 요청이 동시에 너무 많이 몰려와도, 쓰레드(실행흐름)의 수는 200 개로 고정된다.
			ExecutorService executor = Executors.newFixedThreadPool(200);
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					executor.submit(new DaytimeTask(socket));
				} catch (IOException ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	static class DaytimeTask implements Runnable {
		Socket socket;

		public DaytimeTask(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				Message msg = new Message("안녕하세요", new Date());
				out.writeObject(msg);
				out.flush();
				socket.close();
			} catch (Exception ex) {
			}
		}
	}
}
