package com.gn.todo.repository;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gn.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo,Long>
										,JpaSpecificationExecutor<Todo>{

	Page<Todo> findAll(Specification<Todo> spec,Pageable pageable);
	
	List<Todo> findByContentContaining(String keyword);
	
	
	@Query(value="SELECT d FROM Todo d WHERE d.content LIKE CONCAT('%',:keyword,'%')")
	List<Todo> findByContentLike(String keyword);
}
