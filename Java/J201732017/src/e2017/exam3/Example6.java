package e2017.exam3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random; 

public class Example6 {

	/*static void solution(int[] a) { 

		List<Integer> list = new ArrayList<>();

		for(int i = 0; i < a.length; ++i)
			if(a[i] % 2 != 0 && a[i] % 3 != 0)
				list.add(a[i]);

		for(int i = 0; i < a.length; ++i)
			a[i] = (i < list.size() ? list.get(i) : -1);
	}*/

	static void solution(int[] a) {
		int index = 0;
		for (int i = 0; i < a.length && a[i] > 0; ++i)
			if ((a[i] % 2) != 0 && (a[i] % 3) != 0)
				a[index++] = a[i];
		for (int i = index; i < a.length; ++i)
			a[i] = -1;
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random random = new Random();   
		int[] a = new int[30];    
		for (int i = 0; i < 30; ++i)  
			a[i] = random.nextInt(8) + 1;    
		System.out.println(Arrays.toString(a)); 
		solution(a);       
		System.out.println(Arrays.toString(a)); 

	}

}
