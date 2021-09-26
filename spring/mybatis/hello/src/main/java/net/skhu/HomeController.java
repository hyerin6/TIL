package net.skhu;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 어노테이션 꼭 필요함
public class HomeController {

	@RequestMapping("/") // url임 이 url에 의해서 액션 메소드가 실행됨
	public String index(Model model) { // view 에게 전달될 데이터를 변수 model에 전달
		model.addAttribute("message", "좋은 아침");
		return "index"; // view의 이름이 index이기 때문) view 이름 리턴
	}
}
