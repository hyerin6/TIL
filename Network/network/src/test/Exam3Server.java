package test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Exam3Server {
	public static void main(String[] args) {
		final int PORT = 10013;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				try (Socket autoClose = serverSocket.accept()) {
					BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(autoClose.getOutputStream(), "UTF-8"));
					writer.write("201732017 박혜린");
					writer.flush();
				} catch (Exception ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
