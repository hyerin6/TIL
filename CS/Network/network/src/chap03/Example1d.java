package chap03;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class Example1d {

	static class GZipThread extends Thread {

		String filePath;

		public GZipThread(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public void run() {
			try (InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
				if (filePath.endsWith(".gz"))
					return;
				String gzFilePath = filePath + ".gz";
				try (OutputStream out = new GZIPOutputStream(
					new BufferedOutputStream(new FileOutputStream(gzFilePath)))) {
					byte[] a = new byte[4096];
					while (true) {
						int count = in.read(a);
						if (count == -1)
							break;
						out.write(a, 0, count);
					}
				}

				/*
				  synchronized 로 객체를 잠근다.
				  작업 블록의 실행이 끝나면 자동으로 잠금이 해제된다.

				  다른 스레드들도 잠긴 객체의 메소드를 호출할 수는 있지만
				  잠금이 풀릴 때까지 대기 상태에 빠진다.

				  하지만 이렇게 lock, unlock 하는 것은 꽤 무거운 작업이기 때문에,
				  앞에서 StringBuilder를 이용해서 구현한 기법이 더 효율적이다.
				 */
				synchronized (System.out) {
					System.out.printf("Result of compressing %s\n", filePath);
					System.out.printf("  original size: %d\n", new File(filePath).length());
					System.out.printf("  compressed size: %d\n", new File(gzFilePath).length());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		for (String s : args) {
			Thread thread = new GZipThread(s);
			thread.start();
		}
	}

}

