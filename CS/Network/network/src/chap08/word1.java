package chap08;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class word1 {

	public static void main(String[] args) throws Exception {
		/*
		연결된 클라이언트가 보낸 메시지를 기다린다.
		메시지는 domain name이어야 한다.
		전송 받은 domain name에 대한 자세한 정보를 전송한다.
		 */
		try (Socket socket = new Socket("whois.kisa.or.kr", 43)) {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.write("skhu.ac.kr\r\n");
			writer.flush();
			while (true) {
				String s = reader.readLine();
				if (s == null) {
					break;
				}
				System.out.println(s);
			}
		}
	}

}
