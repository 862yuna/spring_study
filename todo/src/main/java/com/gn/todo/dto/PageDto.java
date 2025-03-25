package com.gn.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PageDto {
	// 한 페이지에 보이는 내용의 개수 5
	private int numPerPage = 5;
	
	// 현재 페이지
	private int nowPage;
	// 하나의 페이지바에 보이는 페이지 개수 5
	private int pageBarSize = 5;
	private int pageBarStart;
	private int pageBarEnd;
	// 이전, 다음
	private boolean prev = true;
	private boolean next = true;
	
	
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	private int totalPage;
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		calcPaging();
	}
	
	private void calcPaging() {
		pageBarStart = ((nowPage-1)/pageBarSize)*pageBarSize + 1;
		
		pageBarEnd = pageBarStart + pageBarSize - 1;
		
		if(pageBarEnd >  totalPage) pageBarEnd = totalPage;
		
		if(pageBarStart == 1) prev = false;
		
		if(pageBarEnd >= totalPage) next = false;
	}
}
