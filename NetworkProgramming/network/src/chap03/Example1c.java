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

public class Example1c {

	static class GZipRunnable implements Runnable {

		String filePath;

		public GZipRunnable(String filePath) {
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
						if (count == -1) {
							break;
						}
						out.write(a, 0, count);
					}
				}
				StringBuilder builder = new StringBuilder();
				builder.append(String.format("Result of compressing %s\n", filePath));
				builder.append(String.format("  original size: %d\n", new File(filePath).length()));
				builder.append(String.format("  compressed size: %d\n", new File(gzFilePath).length()));
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
			Thread thread = new Thread(new GZipRunnable(s));
			thread.start();
		}
	}

}

