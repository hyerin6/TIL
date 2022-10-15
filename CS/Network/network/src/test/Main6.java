package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
클라이언트가 전송한 문자열을 받고,
그 문자열을 약간 변형하여 클라이언트에게 전송하는 작업을 무한 반복하는 서버를 구현
 */
public class Main6 {
	public static void main(String[] args) {
		final int PORT = 13;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				try (Socket autoClose = serverSocket.accept()) {
					BufferedReader reader = new BufferedReader(
						new InputStreamReader(autoClose.getInputStream(), "UTF-8"));
					BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(autoClose.getOutputStream(), "UTF-8"));
					String s = reader.readLine();
					writer.write(s.toUpperCase());
					writer.write("\r\n");
					writer.flush();
				} catch (Exception ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
