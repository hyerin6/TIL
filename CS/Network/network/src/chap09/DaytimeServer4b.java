package chap09;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/*
DaytimeServer4b 서버: 싱글 스레드로 실행된다.
DaytimeClient5 클라이언트: 200개의 스레드로 실행된다.

DaytimeServer4b 서버가 싱글 스레드지만
작업 시간이 짧기 때문에 순식간에 처리된다.

서버가 싱글 스레드이기 때문에 각 클라이언트의 연결 요청이 동시에 처리되는 것은 아니고
클라이언트의 연결이 하나씩 순서대로 처리된다.
 */
public class DaytimeServer4b {

	public static void main(String[] args) {
		final int PORT = 13, BACKLOG = 200;
		// ServerSocket 생성자의 두 번째 파라미터가, 연결 대기 큐의 크기이다.
		// 디폴트 크기가 50이므로 200으로 늘린다.
		try (ServerSocket serverSocket = new ServerSocket(PORT, BACKLOG)) {
			while (true) {
				try (Socket socket = serverSocket.accept()) {
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					Message msg = new Message("안녕하세요", new Date());
					out.writeObject(msg);
					out.flush();
				} catch (Exception ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}

