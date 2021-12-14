package chap09;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DaytimeServer4 {

	public static void main(String[] args) {
		final int PORT = 13;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
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
