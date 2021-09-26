package net.skhu.map;

import java.util.HashMap;
import java.util.Map;

public class Example1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Map<String, Integer> map = new HashMap<>();

		map.put("a", 101);
		map.put("b", 102); 
		map.put("c", 103);

		// map.get(value) - 파라미터로 받은 value에 맞는 key값을 반환한다. 
		System.out.printf("%d %d %d\n", map.get("a"), map.get("b"), map.get("c"));

	}

}
