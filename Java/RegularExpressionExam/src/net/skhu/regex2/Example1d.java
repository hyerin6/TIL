package net.skhu.regex2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Example1d {

	/*
	 (?<이름>정규식)
	 위 정규식에서 ?<이름> 부분은 괄호로 묶인 정규식에 이름을 부여한다.
	 이 괄호와 일치하는 부분만 알고 싶을 때 group(String 이름) 메소드를 사용한다.
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "Telephone: 032-431-2323 010-5533-2299 02-555-3388 010-222-5678"; 
		String regex = "010-(?<a>[0-9]{3,4})-(?<b>[0-9]{4})";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s); 

		while (matcher.find()) { 
			System.out.printf("전체:%s ", matcher.group(0));
			System.out.printf("국번:%s ", matcher.group("a"));
			System.out.printf("번호:%s\n", matcher.group("b"));
		}
	}

}
