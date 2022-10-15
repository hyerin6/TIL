package chap09;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DaytimeClient1 {

	public static void main(String[] args) throws UnknownHostException, IOException {
		final String HOST = "localhost";
		final int PORT = 13;
		try (Socket socket = new Socket(HOST, PORT)) { // 서버에 연결
			InputStream in = socket.getInputStream(); // 서버가 전송을 받기 위한 inputStream
			// 서버가 전송한 내용을 받아서 화면에 출력한다.
			StringBuilder result = new StringBuilder();
			while (true) {
				int c = in.read();
				if (c == -1) {
					break;
				}
				result.append((char)c);
			}
			System.out.println(result.toString());
		}
	}

}

