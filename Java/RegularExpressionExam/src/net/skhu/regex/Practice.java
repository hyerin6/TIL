package net.skhu.regex;

public class Practice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = new String[] {"aBcD", "123", "hyerin_", "012456-1123456",
				"010-6259-9692", "(2)", "(980611)", "0x2B", "\"%d\"", "hyerin@naver.com"};

		String regex1 = "[a-zA-Z_][a-zA-Z0-9_]*";
		String regex2 = "[0-9]{6}-[1234][0-9]{6}";
		String regex3 = "01[0-9]-[0-9]{3,4}-[0-9]{4}"; // 전화번호
		String regex4 = "\\([1-9][0-9]*\\)";
		String regex5 = "0x[0-9A-F]{2}";
		String regex6 = "\"%[0-9]*[dsfx]\"";
		String regex7 = "[a-zA-Z]+@[a-zA-Z]+\\.com"; // (알파벳만)이메일


		for (String s : a) {
			if (s.matches(regex1))
				System.out.printf("\"%s\"   <===matches===>   \"%s\"\n", s, regex1); 
			else if(s.matches(regex2))
				System.out.printf("\"%s\"   <===matches===>   \"%s\"\n", s, regex2);
			else if(s.matches(regex3))
				System.out.printf("\"%s\"   <===matches===>   \"%s\"\n", s, regex3);
			else if(s.matches(regex4))
				System.out.printf("\"%s\"   <===matches===>   \"%s\"\n", s, regex4);
			else if(s.matches(regex5))
				System.out.printf("\"%s\"   <===matches===>   \"%s\"\n", s, regex5);
			else if(s.matches(regex6))
				System.out.printf("\"%s\"   <===matches===>   \"%s\"\n", s, regex6);

			else if(s.matches(regex7))
				System.out.printf("\"%s\"   <===matches===>   \"%s\"\n", s, regex7);
			else 
				System.out.printf("\"%s\"   doesn't match... \n", s);
		}

	}

}
