package test;

import java.io.ObjectInputStream;
import java.net.Socket;

public class Exam4Client {
	public static void main(String[] args) {
		final String HOST = "localhost";
		final int PORT = 10014;
		try (Socket socket = new Socket(HOST, PORT)) {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Student student = (Student)in.readObject();
			System.out.printf("%d %s\n", student.getId(), student.getName());
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
}
