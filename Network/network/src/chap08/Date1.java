package chap08;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Date1 {

	// 현재시각(미국) 메시지를 출력하고
	static String getTime() throws UnknownHostException, IOException {
		try (Socket socket = new Socket("time.nist.gov", 13)) {
			StringBuilder result = new StringBuilder();
			InputStream in = socket.getInputStream();
			while (true) {
				int c = in.read(); // 바이트 값을 읽는다.
				if (c == -1) {
					break;
				}
				result.append((char)c); // 문자 생성
			}
			return result.toString();
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println(getTime());
	}

}
