package test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Exam1Client {
	public static void main(String[] args) {
		final String HOST = "localhost";
		final int PORT = 10011;
		try (Socket socket = new Socket(HOST, PORT)) {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			writer.write("201732017 박혜린");
			writer.flush();
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
}
