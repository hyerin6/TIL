package chap09;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DaytimeServer3 {

	public static void main(String[] args) {
		final int PORT = 13;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				try (Socket socket = serverSocket.accept()) {
					// Java 객체를 출력하려면 ObjectOutputStream 객체를 사용해야 한다.
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					Date now = new Date();
					out.writeObject(now);
					out.flush();
				} catch (IOException ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
