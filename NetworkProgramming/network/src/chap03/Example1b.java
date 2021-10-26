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

public class Example1b {

	/*
	System.out.print 메소드는 thread safe 하다.
	여러 스레드에서 이 메소드를 동시에 호출해도 문제가 없다.
	System.out.print 메소드가 출력을 마칠 때까지 다른 스레드는 출력할 수 없고 대기해야 한다.

	System.out 객체의 클래스는 PrintStream 클래스이다.
	이 클래스의 모든 메소드는 thread safe 하다.
	메소드 실행을 시작할 때 this 객체를 lock하고 메소드를 리턴할 때 this 객체를 unlock 한다.
	이 lock, unlock 작업은 꽤 무겁다. StringBuilder에 모았다가 한번에 출력하는 것이 빠르다.
	 */

	static class GZipThread extends Thread {
		String filePath;

		public GZipThread(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public void run() {
			try (InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
				if (filePath.endsWith("gz")) {
					return;
				}
				String gzFilePath = filePath + ".gz";
				try (OutputStream out = new GZIPOutputStream(
					new BufferedOutputStream(new FileOutputStream(gzFilePath)))) {
					byte[] a = new byte[4096];
					while (true) {
						int count = in.read(a);
						if (count == -1) {
							break;
						}
						out.write(a, 0, count);
					}
				}
				StringBuilder builder = new StringBuilder();
				builder.append(String.format("Result of compressing %s\n", filePath))
					.append(String.format("   original size: %d\n", new File(filePath).length()))
					.append(String.format("   comporessed size: %d\n", new File(gzFilePath).length()));
				System.out.print(builder.toString());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		for (String s : args) {
			Thread thread = new Example1a.GZipThread(s);
			thread.start();
		}
	}

}
