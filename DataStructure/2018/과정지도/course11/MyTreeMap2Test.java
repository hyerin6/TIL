package course11;

/*
 * 파일명: MyTreeMap2Test.java
 * 작성일: 2018.11.27
 * 작성자: 박혜린
 * 설명: String-String 쌍을 저장하는 이진탐색트리를 생성하고 연산을 수행하는 프로그램
 */

public class MyTreeMap2Test {
	public static void main(String[] args) {

		System.out.println("course11 : 박혜린");

		// 공백 이진탐색트리 tree를 생성
		MyTreeMap2 tree = new MyTreeMap2();

		// 공백 tree에서 key 값에 해당하는 value를 조회
		System.out.println("kim=" + tree.get("kim"));
		System.out.println();

		// tree에 key-value 쌍을 삽입  
		tree.put("kim", "Seoul");
		tree.put("park", "Pusan");
		tree.put("choi", "Incheon");
		tree.put("hong", "Seoul");
		tree.put("seo", "Pusan");
		tree.put("lee", "Suwon");
		tree.put("kim", "Jeju");

		// tree에서 key 값에 해당하는 value를 조회
		System.out.println("kim=" + tree.get("kim"));
		System.out.println("park=" + tree.get("park"));
		System.out.println("choi=" + tree.get("choi"));
		System.out.println("hong=" + tree.get("hong"));
		System.out.println("seo=" + tree.get("seo"));
		System.out.println("lee=" + tree.get("lee"));   
	}
}