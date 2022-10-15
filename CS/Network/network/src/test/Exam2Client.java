package test;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class Exam2Client {
	public static void main(String[] args) {
		final String HOST = "localhost";
		final int PORT = 10012;
		try (Socket socket = new Socket(HOST, PORT)) {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			Student student = new Student(201732017, "박혜린");
			out.writeObject(student);
			out.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
