package com.java;

import java.io.IOException;
import java.io.OutputStream;

public class Example1e {

	public static void generateCharacters(OutputStream out) throws IOException {
		int first = 33;
		int last = 126;

		for (int i = first; i <= last; ++i) {
			out.write(i);
		}

		out.write('\r');

		/*
		close 될 때, 자동으로 flush() 메소드가 호출되기 때문에 버퍼의 내용이 출력된다.
		그러나 네트워크 통신 출력 스트림 중 close 하기 전에 flush를 해야 하는 출력 스트림도 있다.
		 */
		out.close();
	}

	public static void main(String[] args) throws IOException {
		System.out.println();
		generateCharacters(System.out);
	}
}
