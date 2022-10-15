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

public class Example1a {

	static class GZipThread extends Thread {
		String filePath;

		public GZipThread(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public void run() {
			// 파일 입력 스트림 객체
			try (InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
				String gzFilePath = filePath + ".gz";
				try (OutputStream out = new GZIPOutputStream( // 압축 파일 출력 스트림 객체
					new BufferedOutputStream(new FileOutputStream(gzFilePath)))) {
					// 스트림 복사
					byte[] a = new byte[4096];
					while (true) {
						int count = in.read(a);
						if (count == -1) {
							break;
						}
						out.write(a, 0, count);
					}
					System.out.printf("Result of compressing %s \n", filePath);
					System.out.printf("   original size: %d \n", new File(filePath).length());
					System.out.printf("   compressed size: %d \n", new File(gzFilePath).length());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
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
