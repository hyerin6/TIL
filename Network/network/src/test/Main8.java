package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/*
클라이언트가 전송한 객체를 받고,
그 객체에 값을 채워서 클라이언트에게 전송하는 작업을 무한 반복하는 서버를 구현
 */
public class Main8 {
	public static void main(String[] args) {
		final int PORT = 13;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				try (Socket autoClose = serverSocket.accept()) {
					ObjectInputStream in = new ObjectInputStream(autoClose.getInputStream());
					Message msg = (Message)in.readObject();
					msg.setValue("hello");
					msg.setDate(new Date());
					ObjectOutputStream out = new ObjectOutputStream(autoClose.getOutputStream());
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
