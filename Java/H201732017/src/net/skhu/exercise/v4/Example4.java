package net.skhu.exercise.v4;

import java.util.ArrayList;
import java.util.Arrays; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Example4 {

	static String[] convertToArray(String s) {
		// 문자열 s에서 단어를 추출하여 단어 목록 배열을 리턴하라.
		// 단어는 알파벳들로만 구성되어야 한다. 
		// 리턴되는 배열의 단어는 전부 소문자로 변환되어야 한다.
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = pattern.matcher(s);

		ArrayList<String> list = new ArrayList<>();

		// find() - 정규식과 일치하는 부분이 this에 포함되어 있으면 true
		while (matcher.find())
			list.add(matcher.group(0).toLowerCase());
		return list.toArray(new String[list.size()]);
	}

	static String[] convertToArray2(String s) {
		s = s.replaceAll("[^a-zA-Z]+", " ");
		s = s.replaceAll("^[^a-zA-Z]+", "");
		s = s.replaceAll("[^a-zA-Z]+$", "");
		s = s.toLowerCase();
		return s.split(" ");
	}

	static String[] convertToArray3(String s) {
		s = s.replaceAll("[^a-zA-Z]+", " ");
		s = s.trim().toLowerCase();
		return s.split(" ");
	}

	static String[] convertToArray4(String s) {
		return s.replaceAll("[^a-zA-Z]+", " ").trim().toLowerCase().split(" ");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = { "one 2 two, - Three,\t four; @ \t\nfive..,SIX)",
				"1 one 2 two, - Three,\t four; @ \t\nfive..,SIX)" 
		};

		for (String s : a) {
			String[] t = convertToArray(s);
			System.out.println(Arrays.toString(t));
		} System.out.println("----------------------------------");

		for (String s : a) {
			String[] t = convertToArray2(s);
			System.out.println(Arrays.toString(t));
		} System.out.println("----------------------------------");

		for (String s : a) {
			String[] t = convertToArray3(s);
			System.out.println(Arrays.toString(t));
		} System.out.println("----------------------------------");

		for (String s : a) {
			String[] t = convertToArray4(s);
			System.out.println(Arrays.toString(t));
		} 


	}

}
