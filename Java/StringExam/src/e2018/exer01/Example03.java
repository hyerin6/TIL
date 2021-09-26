package e2018.exer01;

public class Example03 {

	static String getFileName1(String path) {
		// 문자열의 끝부터 "/"의 인덱스 찾아서 정수로 받는다.
		// substring은 (시작위치 포함)파라미터 정수부터 반환하기 때문에 +1 을 해준다.
		String s = path.substring(path.lastIndexOf("/")+1);
		return s;
	} 
	static String getFileName2(String path) {
		String[] s = path.split("/");

		// 배열의 크기를 받아 마지막 배열 원소 출력을 위해 -1 을 해준다.
		return s[s.length-1];
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] a = { 
				"c:/data/student/lecture.docx",
				"c:/www/mainpage.html",
				"c:/program files/java/javac.exe" 
		}; 

		for (String s : a) {
			String fileName1 = getFileName1(s);
			String fileName2 = getFileName2(s);
			System.out.printf("%s, %s\n", fileName1, fileName2); 
		}

	}

}