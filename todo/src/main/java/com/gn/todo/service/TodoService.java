package com.gn.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.repository.TodoRepository;
import com.gn.todo.specification.TodoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
	
	private final TodoRepository repository;

	
	// 할일 등록
//	public int createTodo(TodoDto dto) {
//		int result = 0;
//		try {
//			
//			Todo entity = dto.toEntity();
//			Todo saved = repository.save(entity);
//			
//			result = 1;
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		return result;
//	}
	public Todo createTodo(TodoDto dto) {
		Todo entity = dto.toEntity();
		Todo result = repository.save(entity);
		return result;
	}
	
	
	// 목록 조회, 페이징, 검색
	public Page<Todo> selectTodoAll(SearchDto searchDto,PageDto pageDto){
		Pageable pageable = PageRequest.of(pageDto.getNowPage() - 1, pageDto.getNumPerPage());
		
		Specification<Todo> spec = (root,query,criteriaBuilder) -> null;
		if(searchDto.getSearch_text() != null) {
			spec = spec.and(TodoSpecification.todoContentContains(searchDto.getSearch_text()));
		}
		
		Page<Todo> list = repository.findAll(spec,pageable);
		return list;
		
		
		
	}
	
	// 삭제
	public int deleteTodo(Long id) {
		int result = 0;
		try {
			Todo target = repository.findById(id).orElse(null);
			if(target != null) {
				repository.deleteById(id);
			}
			
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Todo updateTodo(Long id) {
		Todo result = null;
		try {
			Todo target = repository.findById(id).orElse(null);
			if(target != null) {
				target.setFlag("Y");
			}
			result = repository.save(target);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 *  public Todo updateTodo(Long id){
	 *  	Todo target = repository.findById(id).orElse(null);
	 *  	
	 *  	TodoDtio dto = TodoDto.builder()
	 *  						.no(target.getNo())
	 *  						.content(target.getContent())
	 *  						.flag(target.getFlag())
	 *  						.build();
	 *  
	 *  	if(target != null){
	 *  		if("Y".equals(target.getFlag()) dto.setFlag("N");
	 *  		else dto.setFlag("Y");	
	 *  
	 *  
	 *  	}
	 *  	return repository.save(dto.toEntity());
	 *  }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	
}
