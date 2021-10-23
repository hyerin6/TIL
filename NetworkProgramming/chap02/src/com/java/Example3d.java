package com.java;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Example3d {

	/*
	이 메소드의 단점은 bytesToRead 크기의 데이터를 전부 읽기 전에 입력 스트림이 종료되면 exception이 발생한다.
	입력 스트림이 종료되기 직전에 데이터가 a 배열에 들어있을 수 있지만, 그 크기를 알 수 없다.
	 */
	static void receive(InputStream in, byte[] a, int bytesToRead) throws IOException {
		DataInputStream din = new DataInputStream(in);
		din.readFully(a, 0, bytesToRead);
	}

	public static void main(String[] args) throws IOException {
		byte[] a = new byte[10];
		receive(System.in, a, 10);
		System.out.println(Arrays.toString(a));
	}

}

