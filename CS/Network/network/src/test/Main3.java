package test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

// 객체를 서버에 전송하는 클라이언트를 구현
public class Main3 {
	public static void main(String[] args) throws UnknownHostException, IOException {
		final String HOST = "localhost";
		final int PORT = 13;
		try (Socket socket = new Socket(HOST, PORT)) {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			Message msg = new Message("hello", new Date());
			out.writeObject(msg);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
