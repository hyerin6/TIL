package com.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Example3a {

	static void receive(InputStream in, byte[] a, int bytesToRead) throws IOException {
		// 입력 스트림에서 bytesToRead 크기의 데이터를 읽어서 a 배열에 채운다.
		in.read(a, 0, bytesToRead);
	}

	public static void main(String[] args) throws IOException {
		byte[] a = new byte[10];
		receive(System.in, a, 10);
		System.out.println(Arrays.toString(a));
	}

}
