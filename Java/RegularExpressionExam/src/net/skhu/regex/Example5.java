package net.skhu.regex;

public class Example5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] a = new String[] { 
				"http://localhost:8080/test/list.jsp?pg=3&sz=15",
				"http://localhost:8080/test/list.jsp?pg=9&sz=10",
				"http://localhost:8080/test/list.jsp?pg=25&sz=20"
		};

		String regex = "(\\?|&)[a-z]+=[0-9]+"; 


		// replaceFirst() - 정규식과 일치하는 첫 부분만 치환(this는 바뀌지 않는다.)
		for (String s : a) { 
			String temp = s.replaceFirst(regex, "");
			System.out.printf("%s => %s\n", s, temp); 
		} 
	}
}