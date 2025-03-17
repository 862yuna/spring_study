package com.gn.mvc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gn.mvc.entity.Board;

public interface BoardRepository extends JpaRepository<Board,Long>
								,JpaSpecificationExecutor<Board>{
	
	// 3. Specification 사용
	Page<Board> findAll(Specification<Board> spec,Pageable pageable); // Pageable을 쓰면 반환 타입은 Page가 되어야한다.
	

	// 1. 메소드 네이밍
	List<Board> findByBoardTitleContaining(String keyword);
	
	List<Board> findByBoardContentContaining(String keyword);
	
	List<Board> findByBoardContentContainingOrBoardTitleContaining(String contentKeyword,
			String titleKeyword);

	// 2. JPQL이용
	@Query(value="SELECT b FROM Board b WHERE b.boardTitle LIKE CONCAT('%',:keyword,'%')")
	List<Board> findByTitleLike(String keyword);
	
	@Query(value="SELECT b FROM Board b WHERE b.boardContent LIKE CONCAT('%',?1,'%')")
	List<Board> findByContentLike(String keyword);
	
	@Query(value="SELECT b FROM Board b WHERE b.boardTitle LIKE CONCAT('%',:title,'%') "
			+ "OR b.boardContent LIKE CONCAT('%',:content,'%')")
	List<Board> findByTitleOrContentLike(String title,String content);
}
