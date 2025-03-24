package com.gn.mvc.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.entity.Member;

public class ChatRoomSpecification {
	
	// WHERE from_member = (SELECT * FROM member WHERE = 회원번호)
	
	// 어디서든 불릴 수 있도록 static 처리 , 하나만 써도 되기 떄문에 클래스명 노상관
	public static Specification<ChatRoom> fromMemberEquals(Member member){
		return(root,query,criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("fromMember"), member);
	}
	
	public static Specification<ChatRoom> toMemberEquals(Member member){
		return(root,query,criteriaBuilder) ->
			criteriaBuilder.equal(root.get("toMember"), member);
	}
}
