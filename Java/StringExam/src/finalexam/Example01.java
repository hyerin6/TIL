package finalexam;

public class Example01 { 
	// trim(), length() 사용
	public static boolean isNullEmptyBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = { null, "", " ", " ", " \t ", " \t\n", " . "};

		for (String s : a) 
			System.out.println(isNullEmptyBlank(s));
	}
}