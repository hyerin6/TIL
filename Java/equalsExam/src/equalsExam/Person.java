package equalsExam;

import java.util.Objects;

public class Person {
	String name;
	int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age; 
	} 

	public String getName() { return name; }    			
	public int getAge() { return age; } 

	/* 구현 #1
	@Override public boolean equals(Object obj) {
		if ((obj instanceof Person) == false) 
			return false; 
		Person p = (Person)obj; 
		return this.name.equals(p.name) && this.age == p.age; 
	}
	>> this 객체의 name 멤버 변수 값이 null이면 NullPointerException 발생
	 */

	// 구현 #2
	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof Person) == false)
			return false; 

		Person p = (Person)obj; 
		// return (this.name == null ? p.name == null : this.name.equals(p.name)) && this.age == p.age; 
		return Objects.equals(this.name, p.name) && this.age == p.age; 
		// 참조형 멤버변수는 null인지 검사하거나 Object에서 정의된 equals를 사용하면 된다.

	}

	@Override 
	public String toString() { 
		return String.format("Person{name = \"%s\", age = %d}", name, age);
	} 
}