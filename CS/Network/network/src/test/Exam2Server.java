package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Exam2Server {
	public static void main(String[] args) {
		final int PORT = 10012;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				try (Socket autoClose = serverSocket.accept()) {
					ObjectInputStream in = new ObjectInputStream(autoClose.getInputStream());
					Student student = (Student)in.readObject();
					System.out.printf("%d %s\n", student.getId(), student.getName());
				} catch (Exception ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
