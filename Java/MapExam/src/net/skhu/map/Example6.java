package net.skhu.map;

import java.util.HashMap; 
import java.util.Map;

public class Example6 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Map<String, Integer> map = new HashMap<>();

		map.put("a", 101); 
		map.put("b", 102); 
		map.put("c", 103);

		// map.keySet() -  등록된 key 목록을 Set 타입으로 리턴한다.
		for(String key : map.keySet())
			System.out.printf("(%s, %d) ", key, map.get(key));
		System.out.println();

		// Set<Map.Entry<K,V>> entrySet() - 등록된 데이터 항목들의 목록을 Set 컬렉션 타입으로 리턴한다.
		for(Map.Entry<String, Integer> entry : map.entrySet())
			System.out.printf("(%s, %d) ", entry.getKey(), entry.getValue());
		System.out.println();
	}

}
