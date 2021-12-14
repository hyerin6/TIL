package test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

// 문자열을 서버에 전송하는 클라이언트 구현
public class Main1 {
	public static void main(String[] args) throws UnknownHostException, IOException {
		final String HOST = "localhost";
		final int PORT = 13;
		try (Socket socket = new Socket(HOST, PORT)) {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			writer.write("hello");
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
