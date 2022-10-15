package test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// 객체를 서버에 전송하고, 서버가 보내온 객체를 받아서 화면에 출력하는 클라이언트를 구현
public class Main7 {
	public static void main(String[] args) throws Exception {
		final String HOST = "localhost";
		final int PORT = 13;
		try (Socket socket = new Socket(HOST, PORT)) {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			Message msg = new Message();
			out.writeObject(msg);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			msg = (Message)in.readObject();
			System.out.printf("%s %s\n", msg.getValue(), msg.getDate());
		}
	}
}
