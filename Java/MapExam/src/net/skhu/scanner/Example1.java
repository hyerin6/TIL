package net.skhu.scanner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

// 구현 실습 문제 #1, #2 - lambda expression
// 파일에서 각 단어의 수를 세어서 출력하라.
// 단어 : 영어 알파벳 만으로 구성된 단어
// 정렬하여 출력하라.
public class Example1 {

	class DataItem {
		String s; 
		int count; 

		public DataItem(String s, int count) {
			this.s = s; 
			this.count = count;
		} 
	}


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filePath = "/Users/hyerin/Desktop/test.java";

		// 경로명이 filePath 인 텍스트 파일을 읽기 위한 Scanner 객체를 생성한다.
		Scanner scanner = new Scanner(Paths.get(filePath)); 
		// [a-zA-Z]+ :  알파벳 제외 (대소문자 전부)
		// [^a-zA-Z]+ : 영어 소문자나 대문자가 한 개 이상 연속된 문자열과 일치하는 패턴
		scanner.useDelimiter("[^a-zA-Z]+"); 

		/*
		// scanner.hasNext() - 텍스트 파일에서 다음 단어(token)를 읽어서 리턴한다.
		while (scanner.hasNext()) {
			String s = scanner.next();
			System.out.println(s);
		} scanner.close();
		 */

		Map<String, Integer> map = new TreeMap<>();

		while(scanner.hasNext()) {
			String s = scanner.next();
			Integer count = map.get(s);
			if(count == null) count = 0;
			++count;
			map.put(s, count);
		} scanner.close();

		for (String key : map.keySet())
			System.out.printf("%s = %d\n", key, map.get(key));

		List<DataItem> list = new ArrayList<>();
		Collections.sort(list, (o1, o2) -> { 
			if (o1 == null && o2 == null) return 0;
			else if (o1 == null) return -1;
			else if (o2 == null) return 1; 
			else { int r = o2.count - o1.count;
			if (r != 0) return r; 
			return o1.s.compareTo(o2.s); 
			} 
		});

		for (DataItem d : list)
			System.out.printf("%s %d\n", d.s, d.count);

	}
}
