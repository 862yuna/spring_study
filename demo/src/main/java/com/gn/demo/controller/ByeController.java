package com.gn.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gn.demo.vo.Member;

@Controller
public class ByeController {
	@GetMapping("/bye")
	public String byeView(Model model) {
		//Member member = new Member();
		model.addAttribute("member",new Member("홍길동",50));
		return "member/bye";
	}
	
}
