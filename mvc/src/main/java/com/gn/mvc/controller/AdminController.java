package com.gn.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin") // 클래스에도 URL을 지정 할 수 있다. 공통적으로 사용
public class AdminController {
	
	@GetMapping("/read")   // -> admin/read -> 위에 있으니 여기선 생략 가능
	public String adminPage() { 
		return "admin/home";  // -> /admin/home.html
	}

}
