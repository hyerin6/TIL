package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Exam3Client {
	public static void main(String[] args) {
		final String HOST = "localhost";
		final int PORT = 10013;
		try (Socket socket = new Socket(HOST, PORT)) {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(socket.getInputStream(), "UTF-8"));
			String s = reader.readLine();
			System.out.println(s);
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
