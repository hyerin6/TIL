package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

// 클라이언트가 전송한 객체를 화면에 출력하는 작업을 무한 반복
public class Main4 {
	public static void main(String[] args) throws IOException {
		final int PORT = 13;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				try (Socket autoClose = serverSocket.accept()) {
					// 클라이언트가 전송한 객체를 읽고 출력
					ObjectInputStream in = new ObjectInputStream(autoClose.getInputStream());
					Message msg = (Message)in.readObject();
					System.out.printf("%s %s\n", msg.getValue(), msg.getDate());
				} catch (Exception ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
