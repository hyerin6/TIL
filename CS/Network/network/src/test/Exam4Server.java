package test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Exam4Server {
	public static void main(String[] args) {
		final int PORT = 10014;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				try (Socket autoClose = serverSocket.accept()) {
					ObjectOutputStream out = new ObjectOutputStream(autoClose.getOutputStream());
					Student student = new Student(201732017, "박헤린");
					out.writeObject(student);
					out.flush();
				} catch (Exception ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
