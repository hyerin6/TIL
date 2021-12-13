package chap08;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PortScanner4 {

	static class PortTask implements Runnable {
		String host;
		int port, timeout;

		public PortTask(String host, int port, int timeout) {
			this.host = host;
			this.port = port;
			this.timeout = timeout;
		}

		@Override
		public void run() {
			StringBuilder builder = new StringBuilder();
			builder.append(host).append(' ').append(port).append(' ');
			try (Socket socket = new Socket()) { // 소켓 운영체제에 자원만 할당
				SocketAddress address = new InetSocketAddress(host, port); // 연결할 주소 객체를 생성한다.
				socket.connect(address, timeout); // 연결할 때 연결 대기 시간을 지정할 수 있다.
				builder.append("연결 성공");
			} catch (ConnectException e) {
				// 연결할 수 없다.
				return;
			} catch (SocketTimeoutException e) {
				// 연결할 수 없다.
				return;
			} catch (Exception e) {
				builder.append(e.getClass().getName()).append(' ').append(e.getMessage());
			}
			System.out.println(builder);
		}
	}

	public static void main(String[] args) {
		String host = "localhost";
		int timeout = 100;
		int portFrom = 1, portTo = 10000;

		ExecutorService executor = Executors.newFixedThreadPool(1000);
		for (int port = portFrom; port <= portTo; ++port) {
			executor.submit(new PortTask(host, port, timeout));
		}

		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
		}
		System.out.println("DONE");
	}

}

