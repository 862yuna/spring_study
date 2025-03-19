package com.gn.mvc.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.AttachRepository;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.AttachSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachService {

	@Value("${ffupload.location}")
	private String fileDir;
	
	private final BoardRepository boardRepository;
	private final AttachRepository attachRepository;
	
	// 파일 메타 데이터 삭제 
	public int deleteMetaData(Long attach_no) {
		int result = 0;
		try {
			Attach target = attachRepository.findById(attach_no).orElse(null);
			if(target != null) {
				attachRepository.delete(target);
			}
			
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	// 파일 자체 메모리에서 삭제
	public int deleteFileData(Long attachNo) {
		int result = 0;
		try {
			Attach attach = attachRepository.findById(attachNo).orElse(null);
			if(attach != null) {
				File file = new File(attach.getAttachPath());
				// 파일이 존재하면 삭제
				if(file.exists()) {
					file.delete();
				}
				
			}
			
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public Attach selectAttachOne(Long id) {
		return attachRepository.findById(id).orElse(null);
	}
	
	public List<Attach> selectAttachList(Long boardNo){
		// 1. boardNo 기준 Board Entity 조회
		Board board = boardRepository.findById(boardNo).orElse(null);
		// 2. Specification 생성(Attach)
		Specification<Attach> spec = (root, query, criteriaBuilder) -> null;
		spec = spec.and(AttachSpecification.boardEquals(board));
		// 3. findAll 메소드에 전달(spec)
		return attachRepository.findAll(spec);
	}
	
	public AttachDto uploadFile(MultipartFile file) {
		AttachDto dto = new AttachDto(); // 기본생성자 생성
		try {
			// 1. 정상 file 여부 확인
			if(file == null || file.isEmpty()) {
				throw new Exception("존재하지 않는 파일입니다."); // 도달했을 때 강제로 예외 발생
			}
			// 2. 파일 최대 용량 체크
			// Spring에서 허용하는 파일 최대 용량은 1MB(1048576byte) application.properties에서 용량 변경 가능
			// byte -> KB(1024byte) -> MB
			long fileSize = file.getSize();
			if(fileSize >= 1048576) { // 파일의 사이즈가 1MB보다 크거나 같을 경우
				throw new Exception("허용 용량을 초과한 파일입니다."); // 예외발생
			}
			// 3. 파일 최초 이름 읽어오기
			String oriName = file.getOriginalFilename(); // 파일의 오리지널네임
			dto.setOri_name(oriName); // dto에 담아주기
			
			// 4. 파일 확장자 자르기
			String fileExt // Ext = 확장자이름  extension
				= oriName.substring(oriName.lastIndexOf("."),oriName.length());
			
			// 5. 파일 명칭 바꾸기
			UUID uuid = UUID.randomUUID(); // 랜덤 UUID 사용 8자리마다 '-' 하이픈이 들어있음
			// 6. UUID의 8자리 마다 반복되는 하이픈 제거
			String uniqueName = uuid.toString().replaceAll("-", ""); // uuid를 toString 하여 하이픈을 전부 비어있는 문자로 바꿔줌
			// 7. 새로운 파일명 생성
			String newName = uniqueName+fileExt;
			dto.setNew_name(newName); // dto에 새로운 이름 넣어주기
			// 8. 파일 저장 경로 설정
			String downDir = fileDir+"board/"+newName;   // ex) C:upload/board/uuid.png
			dto.setAttach_path(downDir);
			
			// 9. 파일 껍데기 생성 -> 파일을 업로드 할 수 있도록 객체를 만들어준다'
			File saveFile = new File(downDir);
			
			// 10. 경로 존재 유무 확인
			if(saveFile.exists() == false) { // savaFile이라는 file이 존재하지 않는다면
				saveFile.mkdirs(); // 만들어준다
			}
			// 11. 껍데기에 파일 정보 복제
			file.transferTo(saveFile);
			
			
		}catch(Exception e) {
			dto = null; // null로 초기화
			e.printStackTrace();
		}
		
		return dto;
		
		
	}
}
