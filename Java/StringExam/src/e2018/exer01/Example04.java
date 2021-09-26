package e2018.exer01;

public class Example04 {

	static String removeO(String s) { 
		// "o"와 "O"를 빈문자열로 치환 - this 문자열은 바뀌지 않는다.
		s = s.replace("o", "");
		s = s.replace("O", "");
		return s;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] a = new String[] { "One", "Two", "Three", "Four", "Five", "hello world", "yahoo" }; 

		for (String s : a) {
			String temp = removeO(s);
			System.out.println(temp); 
		}

		System.out.println("***this 문자열은 바뀌지 않았습니다.***");
		for (String s : a) 
			System.out.println(s); 
	}
}