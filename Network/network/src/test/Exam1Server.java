package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Exam1Server {
	public static void main(String[] args) {
		final int PORT = 10011;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				try (Socket autoClose = serverSocket.accept()) {
					BufferedReader reader = new BufferedReader(
						new InputStreamReader(autoClose.getInputStream(), "UTF-8"));
					String s = reader.readLine();
					System.out.println(s);
				} catch (Exception ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
