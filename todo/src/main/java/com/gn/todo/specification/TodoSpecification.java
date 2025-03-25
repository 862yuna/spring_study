package com.gn.todo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.todo.entity.Todo;

public class TodoSpecification {
	
	public static Specification<Todo> todoContentContains(String keyword){
		
		// 내용에 키워드 포함
		return(root,query,criteriaBuilder) ->
		criteriaBuilder.like(root.get("content"), "%"+keyword+"%");
	}

}
