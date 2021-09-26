package e2018.exam1;

public class Exam06 {

	static String getExtension(String path) {
		int index = path.lastIndexOf(".");
		return path.substring(index);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = {
				"c:/data/student/lecture.docx",
				"c:/www/mainpage.html", 
				"c:/program files/java/javac.exe" 
		};

		for(String s : a) {
			String ext = getExtension(s);
			System.out.println(ext);
		}
	}

}
