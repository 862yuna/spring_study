package com.gn.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.service.AttachService;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {

	private Logger logger = LoggerFactory.getLogger(BoardController.class);
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
	private final AttachService attachService;
	
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
		
		// list를 만들어 attachDto 객체를 하나씩 담아줌
		List<AttachDto> attachDtoList = new ArrayList<AttachDto>();
		
		for(MultipartFile mf : dto.getFiles()) {
//			가지고 온 이름을 출력할 수 있다.?
//			logger.info(mf.getOriginalFilename());
			AttachDto attachDto = attachService.uploadFile(mf);
			if(attachDto != null) attachDtoList.add(attachDto); 
		}
//		if(dto.getFiles().size() == attachDtoList.size()) { // 스프링이 처리하도록 하여 조건 X
			int result = service.createBoard(dto,attachDtoList);
			if(result > 0) {
				resultMap.put("res_cod", "200");
				resultMap.put("res_msg", "게시글이 등록되었습니다.");
//			}
		}
//		System.out.println(dto);
		// Service가 가지고 있는 createBoard 메소드 호출
//		BoardDto result = service.createBoard(dto);
		
//		logger.debug("1 : "+result.toString()); // 개발할때
//		logger.info("2 : "+result.toString()); // 출시할때
//		logger.warn("3 : "+result.toString()); // 출시할때
//		logger.error("4 : "+result.toString()); 
		
		return resultMap;
	}
	
	
	@GetMapping("/board")
	public String selectBoardAll(Model model, SearchDto searchDto,
			PageDto pageDto) {
		
		if(pageDto.getNowPage() == 0) pageDto.setNowPage(1);
		
		// 1. DB에서 목록 SELECT
		Page<Board> resultList = service.selectBoardAll(searchDto,pageDto);
		
		pageDto.setTotalPage(resultList.getTotalPages());
		
		// 2. 목록에서 Model에 등록
		model.addAttribute("boardList",resultList);
		model.addAttribute("searchDto",searchDto);
		model.addAttribute("pageDto",pageDto);
		
		// 3. list.html에 데이터 셋팅
		return "board/list";
	}
	
	@GetMapping("/board/{id}")
	public String selectBoardOne(@PathVariable("id") Long id,Model model) {
		logger.info("게시글 단일 조회 : "+id);
		Board result = service.selectBoardOne(id);
		model.addAttribute("board",result);
		List<Attach> attachList = attachService.selectAttachList(id);	
		model.addAttribute("attachList",attachList);
				
		return "board/detail";
	}
	
	@GetMapping("/board/{id}/update")
	public String updateBoardView(@PathVariable("id") Long id,Model model) {
		Board board = service.selectBoardOne(id);
		model.addAttribute("board",board);
		
		List<Attach> attachList = attachService.selectAttachList(id);
		model.addAttribute("attachList",attachList);
		return "board/update";
	}
	
	@PostMapping("/board/{id}/update")
	@ResponseBody
	public Map<String,String> updateBoardApi(BoardDto param){
		// 1. BoardDto 출력(전달 확인)
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 수정중 오류가 발생하였습니다.");
		List<AttachDto> attachDtoList = new ArrayList<AttachDto>();
		
		for(MultipartFile mf : param.getFiles()) {
			AttachDto attachDto = attachService.uploadFile(mf);
//			logger.info(mf.getOriginalFilename());
			if(attachDto != null) {
				attachDtoList.add(attachDto);
			}
		}
		Board saved = service.updateBoard(param,attachDtoList);
		if(saved != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 정상적으로 수정되었습니다.");
		}
//		if(attachDtoList.size() == param.getFiles().size()) {
//			// 2. BoardService -> BoardRepository 게시글 수정(save)
//
////		// 3. 수정 결과 Entity가 null이 아니면 성공 그외는 실패
//			}
//			
//		}
//		// 삭제하고자 하는 파일이 존재하는 경우
//		if(param.getDelete_files() != null && !param.getDelete_files().isEmpty()) {
//
//			for(Long attach_no : param.getDelete_files()) {
//				// (1) 메모리에서 파일 자체 삭제
//				if(attachService.deleteFileData(attach_no) > 0) {
//					// (2) DB에서 메타 데이터 삭제 -> 사용자 눈에 보이는 부분 (더 중요)
//					attachService.deleteMetaData(attach_no);
//				}
//			}
//			resultMap.put("res_code", "200");
//			resultMap.put("res_msg", "게시글이 정상적으로 수정되었습니다.");
//		}
//		
		
//		logger.info("삭제 파일 정보 : "+param.getDelete_files());
		
		logger.debug(param.toString()); //출력 (전달 확인)
//		System.out.println(param);
//		Board saved = service.updateBoard(param); // 파일 정보를 삭제하는 로직
		
		return resultMap;
	}
	
	@DeleteMapping("/board/{id}/delete")
	@ResponseBody
	public Map<String,String> deleteBoardApi(@PathVariable("id") Long id){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg","게시글 삭제중 오류가 발생했습니다.");
		
		
		int result = service.deleteBoard(id);
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 정상적으로 삭제되었습니다.");
		}
		
		return resultMap;
	}
	
	
}
