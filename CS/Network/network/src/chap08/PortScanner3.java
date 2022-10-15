package chap08;

import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PortScanner3 {

	static class PortTask implements Runnable {
		String host;
		int port;

		public PortTask(String host, int port) {
			this.host = host;
			this.port = port;
		}

		@Override
		public void run() {
			StringBuilder builder = new StringBuilder();
			builder.append(host).append(' ').append(port).append(' ');
			try (Socket socket = new Socket(host, port)) {
				builder.append("연결 성공");
			} catch (ConnectException e) {
				// 연결할 수 없다. 아무것도 출력 안함
				return;
			} catch (Exception e) {
				builder.append(e.getClass().getName()).append(' ').append(e.getMessage());
			}
			System.out.println(builder);
		}
	}

	public static void main(String[] args) {
		String host = "localhost";
		int portFrom = 1, portTo = 10000;

		ExecutorService executor = Executors.newFixedThreadPool(1000);
		for (int port = portFrom; port <= portTo; ++port) {
			executor.submit(new PortTask(host, port));
		}


		/* 작업 종료 확인
		작업 제출(submit)이 끝났고 더 이상 작업이 없다고 선언하는 것
		ExecutorService 내부의 쓰레드들이, 제출된 작업들을 모두 다 처리한 후 자동 종료
		 */
		executor.shutdown();
		try {
			// ExecutorService에 제출된 작업들이 모두 처리되는 것을, 메인 쓰레드가 블럭된 상태로 기다리게 된다.
			executor.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
		}
		System.out.println("DONE");
	}

}

