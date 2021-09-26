package e2018.exer01;

public class Example06 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = { " the", "world\t ", " \nwar " }; 
		for (String s : a) { 
			System.out.printf("%s %d\n", s.trim(), s.trim().length()); 
		}
	}

}
