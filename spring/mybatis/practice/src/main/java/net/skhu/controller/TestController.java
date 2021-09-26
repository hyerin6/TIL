package net.skhu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	// 첫 페이지
	@RequestMapping("/")
	public String index(Model model) {
		return "index";
	}
}
