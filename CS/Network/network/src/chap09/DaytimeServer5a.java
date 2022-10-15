package chap09;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DaytimeServer5a {

	public static void main(String[] args) {
		final int PORT = 13, BACKLOG = 200;
		try (ServerSocket serverSocket = new ServerSocket(PORT, BACKLOG)) {
			while (true) {
				// 서버 작업을 DaytimeTask 클래스의 run 메소드로 구현
				// 아래 코드에 버그가 있다.
				// 메인 스레드가 try 블럭을 빠져나갈 때 socket.close() 메소드가 자동으로 호출된다
				// 그런데 start() 메소드는 새 실행 흐름을 생성한다.
				try (Socket socket = serverSocket.accept()) {
					Thread thread = new Thread(new DaytimeTask(socket));
					thread.start(); // 이 메소드는 금방 리턴된다.
					// 이 메소드가 생성된 새 실행 흐름에서 socket 객체가 사용된다.
					// 메인 스레드는 start 메소드에서 금방 리턴되고 try 블럭을 빠져나간다.
					// 새 실행 흐름이 아직 socket 객체를 사용하고 있을 때
					// socket.close() 메소드가 호출되므로 에러가 발생한다.
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
			} catch (Exception ex) {
			}
		}
	}
}
