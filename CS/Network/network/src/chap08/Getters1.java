package chap08;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Getters1 {

	public static void main(String[] args) throws UnknownHostException, IOException {
		try (Socket socket = new Socket("www.skhu.ac.kr", 80)) {
			System.out.printf("remote host: %s\n", socket.getInetAddress());
			System.out.printf("remote port: %d\n", socket.getPort());
			System.out.printf("local host: %s\n", socket.getLocalAddress());
			System.out.printf("local port: %d\n", socket.getLocalPort());
		}
	}

}
