package com.portfolio.www.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pagination {	
	private int totalPosts; // 전체 게시글 개수
	private int currentPage; // 현재 페이지 번호
	private int postsPerPage; // 한 페이지 당 게시글 개수

	public static int DISPLAY_PAGE_NUM = 10; // 한 페이지 당 출력할 게시글
	
	private int totalPages; // 전체 페이지 개수
	private int startPage; // 시작 페이지 번호
	private int endPage; // 끝 페이지 번호
	private boolean prev; // 이전 화살표 표시 여부
	private boolean next; // 다음 화살표 표시 여부
	
	public Pagination(int totalPosts, int currentPage, int postsPerPage) {
		this.totalPosts = totalPosts;
		this.currentPage = currentPage;
		this.postsPerPage = postsPerPage;
		
		setTotalPages();
		setStartPage();
		setEndPage();
		setPrev();
		setNext();
	}

	public void setTotalPages() {
		this.totalPages = ((this.totalPosts - 1) / this.postsPerPage) + 1;
	}

	public void setStartPage() {
		this.startPage = ((this.currentPage - 1) / DISPLAY_PAGE_NUM) * DISPLAY_PAGE_NUM + 1;
	}

	public void setEndPage() {
		this.endPage = (((this.currentPage - 1) / DISPLAY_PAGE_NUM) + 1) * DISPLAY_PAGE_NUM;
		
		if (this.totalPages < this.endPage)
			this.endPage = this.totalPages;
	}

	public void setPrev() {
		this.prev = (this.startPage == 1) ? false : true;
	}

	public void setNext() {
		this.next = (this.endPage == this.totalPages) ? false : true;
	}
}
