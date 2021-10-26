package chap02;

import java.io.IOException;
import java.io.OutputStream;

public class Example1d {

	// flush() 메소드에 의해서 출력 버퍼의 내용이 실제로 출력되는 것을 확인할 수 있다.
	public static void generateCharacters(OutputStream out) throws IOException {
		int first = 33;
		int last = 126;

		for (int i = first; i <= last; ++i) {
			out.write(i);
		}

		out.write('\r');
		out.flush();
	}

	public static void main(String[] args) throws IOException {
		System.out.println();
		generateCharacters(System.out);
	}
}
