package com.aaa.board.util;

import lombok.Getter;

@Getter
public class PageNavigator {
	private final int countPerPage = 15;	// 한 페이지 당 글개수
	private final int pagePerGroup = 5;     // 페이지 그룹
	private int currentPage;				// 현재 페이지
	private int totalRecordCount;			// 전체 글 개수
	private int totalPageCount;				// 총 페이지수
	private int currentGroup;				// 현재 그룹
	private int startPageGroup;				// 현재 그룹의 첫 페이지
	private int endPageGroup;				// 현재 그룹의 마지막 페이지			
	private int srow;
	private int erow;
	
	public PageNavigator(int currentPage, int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
		
		totalPageCount = (totalRecordCount % countPerPage > 0) 
                ? totalRecordCount / countPerPage + 1 : totalRecordCount / countPerPage;
		
		// 현재 페이지 요청 시 
		this.currentPage = currentPage; 
		if(currentPage <= 0) this.currentPage = 1;
		if(currentPage > totalPageCount) this.currentPage = totalPageCount;

		
		currentGroup = (this.currentPage - 1) / pagePerGroup; 

		
		startPageGroup = currentGroup * pagePerGroup + 1;
		startPageGroup = startPageGroup < 1 ? 1 : startPageGroup;
		
		endPageGroup = startPageGroup + pagePerGroup - 1;
		endPageGroup = endPageGroup < totalPageCount ? endPageGroup : totalPageCount;
		

		srow = 1 + (this.currentPage-1) * countPerPage;
		erow = countPerPage + (this.currentPage-1) * countPerPage;
	}
}
