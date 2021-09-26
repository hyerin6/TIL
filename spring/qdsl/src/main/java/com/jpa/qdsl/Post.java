package com.jpa.qdsl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Entity
@AllArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String text;

	public Post(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Post{" +
			"id=" + id +
			", text='" + text + '\'' +
			'}';
	}
}
