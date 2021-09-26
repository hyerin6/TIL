package net.skhu.sort.comparator;

import java.util.Arrays;

public class Example1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("*** 객체 정렬 ***");
		Person[] a = new Person[] {
				new Person("홍길동", 18),
				new Person("임꺽정", 22),
				new Person("전우치", 23) 
		}; 

		Arrays.sort(a, new PersonNameComparator());
		System.out.println(Arrays.toString(a));

		Arrays.sort(a, new PersonAgeComparator());
		System.out.println(Arrays.toString(a));
		System.out.println();

		/*
		 두 객체를 compare 메소드로 비교한 결과가 0이면
		equals 메소드로 비교한 결과도 true가 성립되어야한다. 
		[ compare 메소드가 0 이면 equals도 true ]
		즉, 두 객체의 멤버변수 전부를 equals 비교해야한다.
		 */
		System.out.println("*** equals, compare 비교 *** ");
		Person p1 = new Person("혜린", 21);
		Person p2 = new Person("혜린", 21);
		PersonAgeComparator pac = new PersonAgeComparator();

		System.out.println(p1.equals(p2));
		System.out.println(pac.compare(p1, p2));

	}
}
