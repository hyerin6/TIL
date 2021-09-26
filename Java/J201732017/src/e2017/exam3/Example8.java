package e2017.exam3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
import java.util.Map; 
import java.util.Random;

public class Example8 {

	static int solution(List<Integer> a) { 
		Map<Integer, Integer> m = new HashMap<>();
		int maxCount = 0, maxValue = 0;

		for(int value : a) {
			Integer count = m.get(value);

			m.put(value, count == null ? 1 : count + 1);

			if(m.get(value) > maxCount) {
				maxCount = m.get(value);
				maxValue = value;
			}
		}
		return maxValue;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random random = new Random(); 
		List<Integer> a = new ArrayList<>();     
		for (int i = 0; i < 20; ++i)          
			a.add(random.nextInt(10) + 1);    
		System.out.println(a.toString());     
		int maxValue = solution(a);       
		System.out.println(maxValue);

	}

}
