// String format 연습
public class Exam {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String s;
		s = String.format("Integer : %d", 15);
		System.out.println(s);
		s = String.format("String : %s", "hyerin");
		System.out.println(s);

		s = String.format("[width] 사용(간격 10) : %10d", 10);
		System.out.println(s);

		s = String.format("[argumant index$] 사용 : %2$d %1$d", 1, 2);
		System.out.println(s);

		// -이면 오른쪽에 그만큼 간격 , 아무것도 안하면 왼쪽에 그만큼 간격을 둔다.
		s = String.format("[flag] 사용 : %-10s%-10s", "hyerin", "exo");
		System.out.println(s);

		// 소수점 아래로 3개만 출력해보자.
		s = String.format("[.precision] 사용 : %.3f", 3.123456789);
		System.out.println(s + "\n");

		/*			구현 실습
		 * 		  int 	  double	 String
		 * ---------- ---------- ----------
		 *        345      23.240       one
		 *         12     301.234       two 
		 *        478       3.124     three 
		 *       1003      98.024      four
		 */


		int[] a1 = new int[] { 345, 12, 478, 1003 };
		double[] a2 = new double[] { 23.24, 301.234, 3.1235, 98.0238 };
		String[] a3 = new String[] { "one", "two", "three", "four" };

		System.out.println("      int      double 	  String");
		System.out.println("---------- ---------- ----------");

		for (int i = 0; i < a1.length; ++i) {
			System.out.println(String.format("%10d %10.3f %10s", a1[i], a2[i], a3[i]));
		}
	}
}