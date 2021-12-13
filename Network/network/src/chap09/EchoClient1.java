package chap09;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;

public class EchoClient1 {
	static Random random = new Random();
	static String[] a = {"one", "two", "three", "four", "하나", "둘", "셋"};

	static class EchoTask implements Runnable {
		// 서버에 어떤 문자열을 전송하고 그 응답으로 서버가 전송한 문자열을 받아서 출력한다.
		@Override
		public void run() {
			final String HOST = "localhost";
			final int PORT = 9090;
			try (Socket socket = new Socket(HOST, PORT)) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
				String s1 = a[random.nextInt(a.length)];
				writer.write(s1);
				writer.write("\r\n");
				writer.flush();
				String s2 = reader.readLine();
				System.out.printf("%s, %s\n", s1, s2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// 실행 흐름 200개를 만들어서 거의 동시에 실행한다.
		for (int i = 0; i < 200; ++i)
			new Thread(new EchoTask()).start();
	}

}

