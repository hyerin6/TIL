package net.skhu.regex;

import java.util.Arrays;

public class Example7 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String s = "one two, three,\t four; \t\nfive..,six"; 

		// String[] split(String regex)
		// 정규식과 일치하는 부분을 기준으로 문자열을 잘라 배열을 만들어 리턴한다. 
		// this 문자열은 바뀌지 않는다.
		String[] a = s.split("[ ,.;\t\n]+");

		System.out.println(Arrays.toString(a));

	}

}
