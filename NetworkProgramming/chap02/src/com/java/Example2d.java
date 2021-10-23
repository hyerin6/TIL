package com.java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Example2d {

	private static final String BASE_ADDRESS = "/Users/hyerin/Documents/TIL/NetworkProgramming/chap02/src/com/java/";

	public static void fileCopy(String sourceFile, String targetFile) throws IOException {
		try (FileInputStream in = new FileInputStream(sourceFile);
			 FileOutputStream out = new FileOutputStream(targetFile)) {
			byte[] a = new byte[1024];
			while (true) {
				int count = in.read(a);
				if (count < 0) {
					break;
				}
				out.write(a, 0, count);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		fileCopy(BASE_ADDRESS + "a.txt", BASE_ADDRESS + "b.txt");
	}

}
