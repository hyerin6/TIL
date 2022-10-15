package chap09;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DaytimeServer1 {

	// 서버가 데이터를 보내고, 클라이언트가 받는 기능
	public static void main(String[] args) {
		final int PORT = 13;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) { // 포트를 차지하고 연결 신호를 기다린다.
			while (true) {
				// 클라이언트와 연결되면 현재 시각 문자열을 클라이언트에 전송하고 연결을 끊는다.
				try (Socket socket = serverSocket.accept()) {
					Writer out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					Date now = new Date();
					out.write(now.toString());
					out.write("\r\n");
					out.flush();
				} catch (IOException ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}

