package e2018.exer01;

public class Example02 {
	// a, e, i, o, u
	static int getVowelCount(String s) {
		int count= 0;
		for(int i=0; i<s.length();i++) {
			// 소문자로 바꾸고 문자 하나하나 모음과 같은지 비교
			switch(s.toLowerCase().charAt(i)){
			case 'a': count++; break;
			case 'e': count++; break;
			case 'i': count++; break;
			case 'o': count++; break;
			case 'u': count++; break;
			}
		}
		return  count;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] a = new String[] {"One", "Two", "Four", "Five", "hello world"};

		for(String s : a) {
			int count = getVowelCount(s);
			System.out.printf("%s %d\n", s, count);
		}
	}
}
