package chap02;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Example2a {

	private static final String BASE_ADDRESS = "/Users/hyerin/Documents/TIL/NetworkProgramming/network/src/chap02/";

	public static void fileCopy(String sourceFile, String targetFile) throws IOException {
		FileInputStream in = new FileInputStream(sourceFile); // 파일에서 데이터를 읽어옴
		FileOutputStream out = new FileOutputStream(targetFile); // 파일에 데이터를 씀

		byte[] a = new byte[1024];
		while (true) {
			int cnt = in.read(a); // 입력 스트림에서 데이터를 읽는다. 읽을 데이터가 없는 경우 -1 리턴
			if (cnt < 0) { // 읽을 값이 없으면 -1이 리턴되므로 읽기 중단
				break;
			}
			out.write(a, 0, cnt); // 읽은 데이터를 출력 스트림에 출력
		}

		// 입력 스트림과 출력 스트림을 닫는다.
		in.close();
		out.close();
	}

	public static void main(String[] args) throws IOException {
		fileCopy(BASE_ADDRESS + "a.txt", BASE_ADDRESS + "b.txt");
	}
}
