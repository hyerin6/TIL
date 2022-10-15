package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

// 이러 이러한 문자열을 서버에 전송하고, 서버가 보내온 문자열을 받아서 화면에 출력하는 클라이언트를 구현
public class Main5 {
	public static void main(String[] args) {
		final String HOST = "localhost";
		final int PORT = 13;
		try (Socket socket = new Socket(HOST, PORT)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			String s1 = "hello";
			writer.write(s1);
			writer.write("\r\n");
			writer.flush();
			String s2 = reader.readLine();
			System.out.printf("%s %s\n", s1, s2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
