package com.gn.mvc.controller;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {

	
	// 1.필드 주입 -> 순환 참조
//	@Autowired
//	BoardService service;	// service 불러옴
	
	// 2. 메소드(Setter) 주입 -> 불변성 보장 X
//	private BoardService service;
//	
//	@Autowired
//	public void setBoardService(BoardService service) {
//		this.service = service;
//	}
	
	// 3. 생성자 주입 + final -> 
	private final BoardService service;
	
//	@Autowired
//	public BoardController(BoardService service) {
//		this.service = service;
//	}
	
	
	@GetMapping("/board/create") // nav에서 보내주는 href와 같음
	public String createBoardView() {
		return "board/create";
	}
	
	@PostMapping("/board")
	@ResponseBody  // 응답데이터가 json형태로 반환되어 전달 <-> @RequestBody(매개변수에 사용)
	public Map<String,String> createBoardApi(
//	첫번째		@RequestParam("board_title") String boardTitle,
//	두번째		@RequestParam("board_content") String boardContent

//	세번째		@RequestParam Map<String,String> param
			BoardDto dto
		) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_cod", "500");
		resultMap.put("res_msg", "게시글 등록중 오류가 발생하였습니다.");
		
		System.out.println(dto);
		// Service가 가지고 있는 createBoard 메소드 호출
		service.createBoard(dto);
		
		return resultMap;
	}
	
	
}
