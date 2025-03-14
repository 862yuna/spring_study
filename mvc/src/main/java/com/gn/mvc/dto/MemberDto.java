package com.gn.mvc.dto;

import java.time.LocalDateTime;

import com.gn.mvc.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MemberDto {
	private Long member_no;
	private String member_id;
	private String member_pw;
	private String member_name;
	private LocalDateTime reg_date;
	private LocalDateTime mod_date;
	
	public Member toEntity() { // 여기에 날짜 정보 넣어주면 NULL로 들어감 그래서 안넣어줌
		return Member.builder()
					.memberId(member_id)
					.memberPw(member_pw)
					.memberName(member_name)
					.build();
	}
	
	public MemberDto toDto(Member member) { // 엔티티로 가지고 온걸 바꿔줄때는 넣어준다.
		return MemberDto.builder()
				.member_id(member.getMemberId())
				.member_pw(member.getMemberPw())
				.member_name(member.getMemberName())
				.reg_date(member.getRegDate())
				.mod_date(member.getModDate())
				.build();
	}
	
	
	
	
}
