package net.skhu.map;

public class Person {
	String name;
	int age;
	
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	@Override
	public String toString() {
		return String.format("Person{\"%s\", %d}", this.name, this.age);
	}
}
