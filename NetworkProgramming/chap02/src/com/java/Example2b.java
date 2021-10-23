package com.java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Example2b {

	private static final String BASE_ADDRESS = "/Users/hyerin/Documents/TIL/NetworkProgramming/chap02/src/com/java/";

	public static void fileCopy(String sourceFile, String targetFile) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			in = new FileInputStream(sourceFile);
			out = new FileOutputStream(targetFile);

			byte[] a = new byte[1024];

			while (true) {
				int count = in.read(a);
				if (count < 0) {
					break;
				}
				out.write(a, 0, count);
			}
		} finally { // 어떠한 경우에도 finally 블럭의 코드는 반드시 실행된다.
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		fileCopy(BASE_ADDRESS + "a.txt", BASE_ADDRESS + "b.txt");
	}
}
