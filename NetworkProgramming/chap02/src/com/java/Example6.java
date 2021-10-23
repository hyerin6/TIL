package com.java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.GregorianCalendar;

public class Example6 {

	private static final String BASE_ADDRESS = "/Users/hyerin/Documents/TIL/NetworkProgramming/chap02/src/com/java/";

	static void writeToFile(Person person, String filePath)
		throws FileNotFoundException, IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
			out.writeObject(person);
		}
	}

	static Person readFromFile(String filePath)
		throws FileNotFoundException, IOException, ClassNotFoundException {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
			return (Person)in.readObject();
		}
	}

	public static void main(String[] args)
		throws FileNotFoundException, IOException, ClassNotFoundException {
		Date birthday = new GregorianCalendar(1998, 8 - 1, 15).getTime();
		Person p1 = new Person(3, "홍길동", birthday);
		writeToFile(p1, BASE_ADDRESS + "a.dat");
		Person p2 = readFromFile(BASE_ADDRESS + "a.dat");
		System.out.println(p1);
		System.out.println(p2);
	}

}

