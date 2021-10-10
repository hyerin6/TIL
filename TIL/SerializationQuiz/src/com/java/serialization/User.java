package com.java.serialization;

import java.io.Serializable;

public class User implements Serializable {

	private String name;
	private long age;
	private String email;

	public User() {
	}

	public User(String name, long age, String email) {
		this.name = name;
		this.age = age;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User{" +
			"name='" + name + '\'' +
			", age=" + age +
			", email='" + email + '\'' +
			'}';
	}
}