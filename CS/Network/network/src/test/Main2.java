package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

// 클라이언트가 전송한 문자열을 화면에 출력하는 작업을 무한반복하는 서버
public class Main2 {
	public static void main(String[] args) throws IOException {
		final int PORT = 13;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				try (Socket autoClose = serverSocket.accept()) {
					// 클라이언트가 전송한 문자열 읽고 출력
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

