package e2018.exer01;

public class Example05 {

	// String.replace 메소드와 String.split 메소드를 사용하여 구현
	static String[] split1(String s) { 
		String[] str = s.split(",");

		for(int i = 0; i<str.length;  i++) 
			str[i] = str[i].replace(" ", "");

		return str;

	} 
	// String.split 메소드와 String.trim 메소드를 사용하여 구현
	static String[] split2(String s) { 
		String[] str = s.split(",");

		for(int i = 0; i<str.length;  i++) 
			str[i] = str[i].trim();

		return str;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String s = "One, Two ,Three , Four,Five"; 

		String[] a1 = split1(s); 
		for (String t : a1)
			System.out.printf("[%s]\n", t);

		String[] a2 = split2(s); 
		for (String t : a2) 
			System.out.printf("[%s]\n", t);
	}

}
