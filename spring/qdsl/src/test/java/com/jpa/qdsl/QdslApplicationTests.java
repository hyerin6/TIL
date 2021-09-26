package com.jpa.qdsl;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Transactional
@SpringBootTest
class QdslApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
		Post post = new Post("post");
		em.persist(post);

		JPAQueryFactory query = new JPAQueryFactory(em);
		QPost qPost = QPost.post;   // Querydsl Q타입 동작확인

		Post result = query
			.selectFrom(qPost)
			.fetchOne();

		Assertions.assertThat(result).isEqualTo(post);
		Assertions.assertThat(result.getId()).isEqualTo(post.getId()); // lombok 동작 확인

		System.out.println(result.toString());
		System.out.println(post.toString());
	}

}
