package com.java.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class Serialization {

	public String serialize(User user) throws Exception {
		byte[] serializedMember;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(user);
		serializedMember = baos.toByteArray();
		return Base64.getEncoder().encodeToString(serializedMember);
	}

	public User deserialize(String base64) throws Exception {
		byte[] serializedUser = Base64.getDecoder().decode(base64);
		ByteArrayInputStream bais = new ByteArrayInputStream(serializedUser);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object object = ois.readObject();
		return (User)object;
	}
	
}
