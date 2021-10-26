package chap02;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Example4 {

	private static final String BASE_ADDRESS = "/Users/hyerin/Documents/TIL/NetworkProgramming/network/src/chap02/";

	public static void main(String[] args) throws IOException {
		OutputStream out = new FileOutputStream(BASE_ADDRESS + "c.txt");
		// "안녕하세요" 문자열을 UTF-8 인코딩으로 c.txt 파일에 저장한다.
		OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
		writer.write("안녕하세요");
		writer.close();
	}

}
