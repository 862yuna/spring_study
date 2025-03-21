package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService service;
	
	@PostMapping("/member/{id}/update")
	@ResponseBody
	public Map<String,String> memberUpdateApi(MemberDto param,
			HttpServletResponse response){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원정보 수정중 오류가 발생했습니다.");
		int result = service.updateMember(param);
		if(result > 0) {
			// 쿠키(remember-me) 무효화
			Cookie rememberMe = new Cookie("remember-me",null);
			rememberMe.setMaxAge(0);
			rememberMe.setPath("/");
			response.addCookie(rememberMe);
			
			resultMap.put("res_cod", "200");
			resultMap.put("res_msg", "회원정보 수정이 정상적으로 완료되었습니다.");
		}
//		MemberDto temp = service.memberUpdate(param);
//		if(temp != null) {
//			resultMap.put("res_cod", "200");
//			resultMap.put("res_msg", "회원정보 수정이 정상적으로 완료되었습니다.");
//		}
		
		return resultMap;
	}
	
	@GetMapping("/member/{id}/update")
	public String memberUpdateView(@PathVariable("id") Long id,Model model) {
		Member member = service.selectMemberOne(id);
		model.addAttribute("member",member);
		return "/member/update";
	}
	

	
	
	@GetMapping("/login")
	public String loginView(
			// required=false 없어도 되지만, 있으면 받아줄게~ 라는 뜻 / 에러메세지가 있든 없든 허락하겠다.
			@RequestParam(value="error", required=false) String error, 
			@RequestParam(value="errorMsg", required=false) String errorMsg,
			Model model) {
		model.addAttribute("error", error);
		model.addAttribute("errorMsg", errorMsg);
		
		return "member/login";
	}
	
	@GetMapping("/signup")
	public String createMemberView() {
		return "/member/create";
	}
	
	@PostMapping("/signup")
	@ResponseBody
	public Map<String,String> createMemberApi(MemberDto dto){
		
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_cod", "500");
		resultMap.put("res_msg", "회원가입중 오류가 발생하였습니다.");
		
		System.out.println(dto);
		MemberDto temp = service.createMember(dto);
		
		if(temp != null) {
			resultMap.put("res_cod", "200");
			resultMap.put("res_msg", "회원가입이 정상적으로 완료되었습니다.");
		}
		
		return resultMap;
		
	}
	
	@DeleteMapping("/member/{id}")
	@ResponseBody
	public Map<String,String> deleteMemberApi(@PathVariable("id") Long id,HttpServletResponse response){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg","회원탈퇴중 오류가 발생했습니다.");
		
		int result = service.deleteMember(id);
		if(result > 0) {
			Cookie rememberMe = new Cookie("remember-me",null);
			rememberMe.setMaxAge(0);
			rememberMe.setPath("/");
			response.addCookie(rememberMe);
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원 탈퇴 되었습니다.");
		}
		
		return resultMap;
	}
	
	
}
