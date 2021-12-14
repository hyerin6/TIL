package test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 서버의 전송 작업(클라이언트에게 전송하는 작업)을 싱글 쓰레드로 구현
public class Main9 {
	public static void main(String[] args) {
		final int PORT = 13;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			// while (true) {
			try (Socket autoClose = serverSocket.accept()) {
				// 문자열 전송
				BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(autoClose.getOutputStream(), "UTF-8"));
				// 객체 전송
				ObjectOutputStream out = new ObjectOutputStream(autoClose.getOutputStream());
			} catch (Exception ex) {
			}
			// } 무한 반복인 경우 while 추가
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
