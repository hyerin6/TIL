package com.java;

import java.io.IOException;
import java.io.OutputStream;

public class Example1b {

	/*
	ASCII 코드를 배열 a에 대입한다.
	배열 a의 내용을 출력 스트림에 출력한다.

	버퍼(buffer)
	Example1a는 문자를 하나씩 출력하고, Example1b는 배열 전체를 한 번에 출력한다.
	그러나 Example1a가 한 문자씩 출력되는 것은 아니다.
	출력 시스템 내부에서 출력 데이터를 버퍼에 모아서 출력한다.
	 */
	public static void generateCharacters(OutputStream out) throws IOException {
		int first = 33;
		int last = 126;
		// 마지막에 CR, LF를 넣기 위해 +2 를 해준다.
		int size = (last - first + 1) + 2;

		byte[] a = new byte[size];
		int index = 0;

		for (int i = first; i <= last; ++i) {
			a[index++] = (byte)i;
		}
		a[index++] = '\r';
		a[index++] = '\n';
		out.write(a);
	}

	public static void main(String[] args) throws IOException {
		System.out.println();
		generateCharacters(System.out);
	}

}
