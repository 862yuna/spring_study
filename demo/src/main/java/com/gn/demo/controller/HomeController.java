package com.gn.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@GetMapping({"","/"})
	public ModelAndView home() {
		// src/main/resources/templates/home.html
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home");
		mav.addObject("age",1);
		return mav;
	}
	
	@GetMapping({"","/test"}) // a태그의 href와 일치시키기, 인터넷 주소창에 나오는 주소가 된다.
	public String testView(Model model) {
		// src/main/resources/templates/test.html
		
		// request.setAttribute("name","김철수");
		model.addAttribute("name","김철수");
		return "test"; // templates 아래에서 해당 문자열 "test"와 일치하는 html을 조회한다.
	}
	
}
