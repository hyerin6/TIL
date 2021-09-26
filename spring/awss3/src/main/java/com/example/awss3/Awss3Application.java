package com.example.awss3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class Awss3Application {

	public static void main(String[] args) {
		SpringApplication.run(Awss3Application.class, args);
	}

}
