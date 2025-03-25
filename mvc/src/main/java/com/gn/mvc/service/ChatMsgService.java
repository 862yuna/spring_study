package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.entity.ChatMsg;
import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.repository.ChatMsgRepository;
import com.gn.mvc.repository.ChatRoomRepository;
import com.gn.mvc.specification.ChatMsgSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMsgService {

	private final ChatMsgRepository chatMsgRepository;
	private final ChatRoomRepository chatRoomRepository;
		
	// 1. 기능 : 채팅 메세지 목록 조회(채팅방의 상세)
	// 2. 조건 : 채팅방 번호 (ChatRoom의 PK가 필요)
	// 3. 결과 : 채팅 메시지 목록
	public List<ChatMsg> selectChatMsgAll(Long id){
		
		// 조건 : 채팅방 번호
		// (1) 전달받은 id를 기준으로 ChatRoom Entity를 조회
		ChatRoom chatRoom = chatRoomRepository.findById(id).orElse(null);
		
		// (2) ChatRoom Entity를 기준으로 Specification 생성
		Specification<ChatMsg> spec = (root,query,criteriaBuilder) -> null;
		spec = spec.and(ChatMsgSpecification.roomNoEquals(chatRoom));
		// (3) Specification을 매개변수로 전달하여 findAll 반환
		
		List<ChatMsg> msgList = chatMsgRepository.findAll(spec);
//		List<ChatMsg> msgList = chatMsgRepository.findAll();
		return msgList;
	}
	
	
}
