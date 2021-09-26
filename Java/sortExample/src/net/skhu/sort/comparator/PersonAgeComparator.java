package net.skhu.sort.comparator;

import java.util.Comparator; 

public class PersonAgeComparator implements Comparator<Person> {
	@Override 
	public int compare(Person p1, Person p2) { 
		int r = p1.age - p2.age; // 먼저 나이(age)를 비교한다.
		if (r != 0) return r; // 나이가 같지 않다면, 나이 비교 결과를 리턴한다.

		return p1.name.compareTo(p2.name); // 나이가 같다면, 이름(name) 비교 결과를 리턴한다.
	} 
}
