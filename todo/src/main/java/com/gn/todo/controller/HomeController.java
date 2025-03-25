package com.gn.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final TodoService service;
	
	
//	@GetMapping({"","/"})
//	public String home() {
//		return "home";
//	}
	
	@PostMapping("/todo")
	@ResponseBody
	public Map<String,String> createTodoApi(TodoDto dto) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할일 등록중 오류가 발생했습니다.");
		
		int result = service.createTodo(dto);
		
		if(result > 0 ) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "할일이 등록되었습니다.");
		}
		
		return resultMap;
	}
	
	@GetMapping({"","/"})
	public String selectTodoAll(Model model, SearchDto searchDto, PageDto pageDto) {
		
		if(pageDto.getNowPage() == 0) {
			pageDto.setNowPage(1);
		}
		
		if(searchDto.getSearch_text() == null) {
			searchDto.setSearch_text("");
		}
		
		Page<Todo> resultList = service.selectTodoAll(searchDto,pageDto);
		
		model.addAttribute("todoList",resultList);
		
		pageDto.setTotalPage(resultList.getTotalPages());
		
		if(resultList.isEmpty()) {
			pageDto.setTotalPage(1);
		}
			
		model.addAttribute("searchDto",searchDto);
		model.addAttribute("pageDto",pageDto);
		
		return "home";
	}
	
	// 삭제
	@DeleteMapping("/todo/{id}/delete")
	@ResponseBody
	public Map<String,String> deleteTodoApi(@PathVariable("id") Long id){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg","삭제중 오류가 발생했습니다.");
		
		int result = service.deleteTodo(id);
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "정상적으로 삭제되었습니다.");
		}
		
		return resultMap;
	}
	
	// 수정
	@PostMapping("/todo/{id}/update")
	@ResponseBody
	public Map<String,String> updateTodoApi(@PathVariable("id") Long id){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "완료 수정중 오류가 발생하였습니다.");
		
		Todo saved = service.updateTodo(id);
		if(saved != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "수정되었습니다.");
		}
		
		return resultMap;
	}
	
	
	
}
