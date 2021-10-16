package com.java;

import java.io.IOException;
import java.io.OutputStream;

public class Example1c {

	/*
	아무것도 출력되지 않는 메소드이다.
	write 메소드로 출력된 내용은 즉시 출력되는 것이 아니라 출력 버퍼에 모아지기만 한다.
	'\n' 문자가 출력될 때, 출력 버퍼의 내용이 실제로 출력된다.
	혹은 flush() 메소드를 호출하면 출력된다.
	 */
	public static void generateCharacters(OutputStream out) throws IOException {
		int first = 33;
		int last = 126;
		
		for (int i = first; i <= last; ++i) {
			out.write(i);
		}

		out.write('\r');
		// out.write('\n');
		// out.flush();
	}

	public static void main(String[] args) throws IOException {
		System.out.println();
		generateCharacters(System.out);
	}

}
