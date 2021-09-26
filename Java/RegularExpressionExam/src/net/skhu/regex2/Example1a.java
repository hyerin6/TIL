package net.skhu.regex2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Example1a {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String s = "Telephone: 032-431-2323 010-5533-2299 02-555-3388 010-222-5678"; 
		String regex = "010-[0-9]{3,4}-[0-9]{4}"; 

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s); 

		
		// find() - 정규식과 일치한 부분을 찾아 true, false 반환
		if (matcher.find()) 
			System.out.println(matcher.group(0));

	}

}
