package chap02;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Example1a {

	private static final String BASE_ADDRESS = "/Users/hyerin/Documents/TIL/NetworkProgramming/network/src/chap02/";

	/*
	256개의 ASCII 코드 문자 중 화면에 출력 가능한 문자를 전부 출력하는 메소드

	out 파라미터 변수의 타입이 OutputStream 클래스이다.
	System.out 객체는 OutputStream 클래스의 자식 클래스의 객체이다.
	up-casting으로 대입이 가능하다.
	 */
	public static void generateCharacters(OutputStream out) throws IOException {
		int firstPrintableCharacter = 33;
		int lastPrintableCharacter = 126;

		for (int ch = firstPrintableCharacter; ch <= lastPrintableCharacter; ++ch) {
			out.write(ch);
		}

		out.write('\r');
		out.write('\n');
	}

	public static void main(String[] args) throws IOException {
		System.out.println();
		generateCharacters(System.out);
		generateCharacters(new FileOutputStream(BASE_ADDRESS + "test.txt"));
	}
}
