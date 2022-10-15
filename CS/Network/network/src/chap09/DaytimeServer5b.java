package chap09;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DaytimeServer5b {

	public static void main(String[] args) {
		final int PORT = 13, BACKLOG = 200;
		try (ServerSocket serverSocket = new ServerSocket(PORT, BACKLOG)) {
			while (true) {
				// try with resource 문법을 사용하지 않았다.
				try {
					Socket socket = serverSocket.accept();
					Thread thread = new Thread(new DaytimeTask(socket));
					thread.start();
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
				Thread.sleep(100);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				Message msg = new Message("안녕하세요", new Date());
				out.writeObject(msg);
				out.flush();
				// thread.start() 메소드에 의해 생성된 새 실행 흐름이
				// socket 객체를 사용한 후 close 해야 한다.
				socket.close();
			} catch (Exception ex) {
			}
		}
	}
}

/*
아직 문제점이 남았다.
클라이언트 연결 요청이 accept 될 때 마다 새 실행 흐름이 생성된다.

생성되는 실행 흐름의 수에 제한이 없기 때문에 너무나 많은 클라이언트가 연결 요청이 발생하면
지나치게 많은 실행 흐름이 생성되어 에러가 발생할 수 있다.
생성되는 실행 흐름 수를 제한해야 한다. > ExcutorService 활용
 */
