package net.skhu.file;

import java.io.IOException; 
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Example1 {

	static String readTextFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// 홈페이지_게시판.html 텍스트 파일의 전체 내용을 읽어서 String 변수에 대입하라.
		String path = "/Users/hyerin/Desktop/홈페이지_게시판.html";
		String s = readTextFile(path, Charset.forName("EUC-KR"));
		System.out.println(s);
		System.out.println("\n------------------------------------------\n");


		// 실습 1 - 제목 찾기
		// 정규식 찾기 기능을 활용하여 원하는 키워드 추출
		/*
		 * [^>] : ">"만 아니면됨
		 * [^<] : "<"만 아니면됨
		 *  + : + 앞에 정규식과 일치하는 문자 1~여러개 있는지 비교 
		 */
		String regex = "<a href=\"http://www.skhu.ac.kr/board/boardread.aspx[^>]+>([^<]+)</a>";

		Pattern pattern = Pattern.compile(regex); 
		Matcher matcher = pattern.matcher(s);

		while (matcher.find()) 
			System.out.println(matcher.group(1));

	}

}
