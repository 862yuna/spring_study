package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.AttachRepository;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.BoardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
//	@Autowired
//	BoardRepository repository; // repository 불러옴
	private final BoardRepository repository;
	private final AttachRepository attachRepository;
	
	
	public Board selectBoardOne(Long id) {
		return repository.findById(id).orElse(null); // 만약 데이터가 없다면 null로 반환(optional을 닫은 상태)
	}
	
	public Page<Board> selectBoardAll(SearchDto searchDto,PageDto pageDto){
//		List<Board> list = new ArrayList<Board>();
//		if(searchDto.getSearch_type() == 1) {
//			// 제목을 기준으로 검색
//			list = repository.findByBoardTitleContaining(searchDto.getSearch_text());
//		}else if(searchDto.getSearch_type() == 2) {
//			// 내용 기준으로 검색
//			list = repository.findByBoardContentContaining(searchDto.getSearch_text());
//		}else if(searchDto.getSearch_type() == 3) {
//			// 제목 또는 내용 기준으로 검색
//			list = repository.findByBoardContentContainingOrBoardTitleContaining(searchDto.getSearch_text(),
//					searchDto.getSearch_text());
//		}else {
//			// WHERE절 없이 검색
//			list = repository.findAll();
//		}
//		
//		return list;
		
//		Sort sort = Sort.by("regDate").descending();
//		if(searchDto.getOrder_type() == 2) {
//			sort = Sort.by("regDate").ascending();
//		} // pageable을 쓰면 sort는 같이 쓸 수 없다
		
		Pageable pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").descending()); // 시작 페이지-1, 한 페이지에서 보여줄 갯수 , sort
		if(searchDto.getOrder_type() == 2) {
			pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").ascending());
		}
		
		Specification<Board> spec = (root,query,criteriaBuilder) -> null;
		if(searchDto.getSearch_type() == 1) {
			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
		}else if(searchDto.getSearch_type() == 2) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		}else if(searchDto.getSearch_type() == 3) {
			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()))
					.or(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		}
		
		Page<Board> list = repository.findAll(spec,pageable);
		return list;
		
		
		
		
	}
	// 자료 데이터 변화가 2개 이상이면 트랜잭션 사용 @Transactional
	@Transactional(rollbackFor = Exception.class) // 모든 예외상황에 롤백하기 위한 별도의 설정
	public int createBoard(BoardDto dto,List<AttachDto> attachList) {
		int result = 0;
		try {
			// 1. Board Entity를 insert
			Board entity = dto.toEntity(); // dto를 entity로 바꿔줌
			Board saved = repository.save(entity); // entity를 매개변수로 써서 repository에 save
			// 2. insert 결과로 반환받은 PK
			Long boardNo = saved.getBoardNo(); 
			// 3. attachList에 데이터가 있는 경우
			if(attachList.size() != 0) {
				for(AttachDto attachDto : attachList) {
					attachDto.setBoard_no(boardNo);
					Attach attach = attachDto.toEntity();
					// 4. Attach 엔티티 insert
					attachRepository.save(attach);
					
				}
			}
			
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
		// 1. 매개변수 dto -> entity
//		Board param = Board.builder()
//					.boardTitle(dto.getBoard_title())
//					.boardContent(dto.getBoard_content())
//					.build();
//		Board param = dto.toEntity(); // dto를 Entity로 바꿔줌
//		
//		// 2. Repository 의 save() 메소드 호출
//		Board result = repository.save(param); // insert쿼리 실행, entity가 반환
//		
//		// 3. 결과 entity -> dto
//		return new BoardDto().toDto(result);
		
		
		
		
		
	}
	
	public Board updateBoard(BoardDto param) {
		Board result = null;
		// 1. @Id를 쓴 필드를 기준으로 타겟을 조회
		Board target = repository.findById(param.getBoard_no()).orElse(null);
		// 2. 타겟이 존재하는 경우 업데이트
		if(target != null) {
			result = repository.save(param.toEntity());
		}
		return result;
	}

	
	public int deleteBoard(Long id) {
		int result = 0;
		try {
			Board target = repository.findById(id).orElse(null);
			if(target != null) {
				repository.deleteById(id);
			}
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
}
