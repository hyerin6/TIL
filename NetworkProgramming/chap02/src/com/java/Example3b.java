package com.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Example3b {
	static void receive(InputStream in, byte[] a, int bytesToRead) throws IOException {
		int bytesRead = 0;

		// bytesToRead 크기의 데이터가 전부 도착할 때까지 기다렸다가
		// bytesToRead 크기의 데이터를 다 읽으면 a 배열에 채운다.
		// 입력 스트림이 끊기면 IndexOutOfBoundsException 에러가 발생한다.
		while (bytesRead < bytesToRead) {
			bytesRead += in.read(a, bytesRead, bytesToRead - bytesRead);
		}
	}

	public static void main(String[] args) throws IOException {
		byte[] a = new byte[10];
		receive(System.in, a, 10);
		System.out.println(Arrays.toString(a));
	}
}
