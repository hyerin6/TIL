package chap09;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer1 {

	public static void main(String[] args) {
		final int PORT = 9090;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			ExecutorService executor = Executors.newFixedThreadPool(200);
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					executor.submit(new EchoTask(socket));
				} catch (IOException ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	static class EchoTask implements Runnable {
		Socket socket;

		public EchoTask(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			/*
			try with resource 문법으로 생성한 autoClose 변수를 생성하면
			autoClose 변수가 참조하는 객체의 close 메소드가 호출되는 것이 보장된다.
			 */
			try (Socket autoClose = socket) {
				// 클라이언트가 전송한 문자열을 서버가 읽어서, 대문자로 변환하여 다시 클라이언트에게 전송한다.
				BufferedReader reader = new BufferedReader(new InputStreamReader(autoClose.getInputStream(), "UTF-8"));
				BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(autoClose.getOutputStream(), "UTF-8"));
				String s = reader.readLine();
				writer.write(s.toUpperCase());
				writer.write("\r\n");
				writer.flush();
			} catch (Exception ex) {
			}
		}
	}
}

