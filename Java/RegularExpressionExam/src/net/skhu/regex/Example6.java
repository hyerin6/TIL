package net.skhu.regex;

public class Example6 {
	public static void main(String[] args) {
		String s = "<table>\n" + "\t<tr>\n\t\t <td>홍길동</td>\n\t\t <td>18</td>\n\t</tr>\n" 
				+ "\t<tr>\n\t\t <td>전우치</td>\n\t\t <td>20</td>\n\t</tr>\n" 
				+ "</table>\n";

		System.out.printf("-------- (0)\n%s\n\n", s); 
		s = s.replaceAll("[ \t\n]+", ""); 
		System.out.printf("-------- (1)\n%s\n\n", s);
		s = s.replaceAll("</tr>", "\n"); 
		System.out.printf("-------- (2)\n%s\n\n", s);
		s = s.replaceAll("</td><td>", ", ");
		System.out.printf("-------- (3)\n%s\n\n", s);
		s = s.replaceAll("</?[a-zA-Z]+>", "");
		System.out.printf("-------- (4)\n%s\n\n", s);

	}

}
