package chap09;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 클라이언트 서버가 기본 자료형 int 값을 주고 받는 코드
public class GuguServer1 {

	public static void main(String[] args) {
		final int PORT = 9090;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			ExecutorService executor = Executors.newFixedThreadPool(200);
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					executor.submit(new GuguTask(socket));
				} catch (IOException ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	static class GuguTask implements Runnable {
		Socket socket;

		public GuguTask(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try (Socket autoClose = socket) {
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				while (true) { // 클라이언트가 연결을 끊을 때까지 반복
					// 클라이언트가 전송한 정수 두 개를 읽어서, 두 수를 곱해 클라이언트에 전송
					int a = in.readInt();
					int b = in.readInt();
					out.writeInt(a * b);
					out.flush();
				}
			} catch (IOException ex) {
			}
		}
	}
}


