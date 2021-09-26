package e2017.exam3;

import java.util.regex.Matcher;
import java.util.regex.Pattern; 

public class Example4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "<tr><td>홍길동</td><td>18</td></tr><tr><td>임꺽정</td><td>19</td></tr>" +       
				"<span>false</span><div><span>true</span></div>"; 

		// [^>]+ - '>'가 아닌 것 1개 ~ 여러개
		// [^<]+ - '<'가 아닌 것 1개 ~ 여러개
		// String regex = "<([^>]+)>([^<]+)";
		String regex = "<([a-z]+)>([^<]+)</[a-z]+>";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);

		// 주의 - group(0)은 전체 출력이다!
		while(m.find()) 
			System.out.printf("TAG: %s  TEXT: %s \n", m.group(1), m.group(2));
	}

}
