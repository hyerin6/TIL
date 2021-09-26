package net.skhu.exam3;
// 05 실습과제3 - 2.구현 실습 - 1), 2)
public class Example4 {
	//1) StringBuilder
	static String reverse(String s) {
		StringBuilder str = new StringBuilder();
		str.append(s);
		str.reverse();
		return str.toString();
	}

	//2) StringBuilder
	static String toString(String[] a) {
		StringBuilder str = new StringBuilder();
		str.append("{");

		for(int i=0; i<a.length; i++) {
			str.append("\"");
			str.append(a[i]);
			str.append("\"");
			if(i<a.length-1) str.append(", ");
		}str.append("}");

		return str.toString();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//1) StringBuilder
		String s = "hello world";
		s = reverse(s);
		System.out.println(s);

		//2) StringBuilder
		String[] a = { "one", "two", "three", "four" };
		String str = toString(a);
		System.out.println(str);
	}

}