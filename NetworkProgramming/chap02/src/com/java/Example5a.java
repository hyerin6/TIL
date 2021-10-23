package com.java;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Example5a {

	private static final String BASE_ADDRESS = "/Users/hyerin/Documents/TIL/NetworkProgramming/chap02/src/com/java/";

	public static String readFile(String filePath, String encoding) throws IOException {
		// InputStream 객체는 InputStreamReader 내부의 멤버 변수에 대입된다.
		InputStream in = new FileInputStream(filePath);
		// InputStreamReader와 InputStream 연결
		InputStreamReader reader = new InputStreamReader(in, encoding);
		StringBuilder builder = new StringBuilder();
		char[] a = new char[1024];
		while (true) {
			/*
			InputStreamReader 클래스의 read() 메소드
			InputStream 객체의 read 메소드를 호출하여
			바이트들을 리턴 받아서, 이 값을 문자열(String)로 변환하여 리턴
			 */
			int count = reader.read(a);
			if (count == -1) {
				break;
			}
			builder.append(a, 0, count);
		}
		reader.close();
		return builder.toString();
	}

	public static void main(String[] args) throws IOException {
		String s = readFile(BASE_ADDRESS + "c.txt", "UTF-8");
		System.out.println(s);
	}

}
