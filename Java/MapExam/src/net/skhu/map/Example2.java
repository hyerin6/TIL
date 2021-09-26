package net.skhu.map;

import java.util.HashMap;
import java.util.Map;

public class Example2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Map<Integer, String> map = new HashMap<>(); 

		map.put(101, "a"); 
		map.put(102, "b"); 
		map.put(103, "c"); 

		System.out.printf("%s %s %s\n", map.get(101), map.get(102), map.get(103)); 

		// map.remove(key) - key값에 맞는 value를 삭제
		map.remove(102); 
		// map.get() - key값에 맞는 value가 없으면 null을 반환
		System.out.printf("%s %s %s\n", map.get(101), map.get(102), map.get(103));

	}

}
