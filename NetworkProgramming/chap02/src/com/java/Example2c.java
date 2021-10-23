package com.java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Example2c {

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
				if (count < 0)
					break;
				out.write(a, 0, count);
			}
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				// close 메소드 호출 중 exception을 무시하기 위한 catch 블럭
			}
		}
	}

	public static void main(String[] args) throws IOException {
		fileCopy(BASE_ADDRESS + "a.txt", BASE_ADDRESS + "b.txt");
	}
}
