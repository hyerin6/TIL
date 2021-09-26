package net.skhu.regex2;

import java.util.regex.Matcher; 
import java.util.regex.Pattern;

public class Example1c {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "Telephone: 032-431-2323 010-5533-2299 02-555-3388 010-222-5678";
		String regex = "010-([0-9]{3,4})-([0-9]{4})"; // () 괄호 확인 !

		Pattern pattern = Pattern.compile(regex); 
		Matcher matcher = pattern.matcher(s);


		// 정규식에서 ( ) 괄호와 일치하는 부분만 알고 싶을 때 group(int 순서) 메소드를 사용한다.
		while (matcher.find()) {
			// 정규식과 일치하는 부분 전체를 리턴
			System.out.printf("전체:%s ", matcher.group(0)); 
			// 첫 번째 () 괄호에 해당하는 부분 리턴
			System.out.printf("국번:%s ", matcher.group(1));
			// 두 번째 () 괄호에 해당하는 부분 리턴
			System.out.printf("번호:%s\n", matcher.group(2)); 
		}

	}

}
