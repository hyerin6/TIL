package com.test.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AopTestController {

	@GetMapping("/aop/test")
	public String hello() {
		return "Hello World :)";
	}

}
