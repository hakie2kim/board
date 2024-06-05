package com.portfolio.www.forum.notice;

import java.util.Calendar;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.portfolio.www.dao.mybatis.BoardRepository;
import com.portfolio.www.service.BoardService;
import com.portfolio.www.util.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NoticeController {
	private final BoardService boardService;
	private final Integer BOARD_TYPE_SEQ = 1;
	
	/*
	 * @RequestMapping("/forum/notice/listPage.do") public ModelAndView
	 * listPage(@RequestParam HashMap<String, String> params) { ModelAndView mv =
	 * new ModelAndView(); mv.addObject("key",
	 * Calendar.getInstance().getTimeInMillis());
	 * mv.setViewName("forum/notice/list");
	 * 
	 * return mv; }
	 */
	
	@RequestMapping("/forum/notice/listPage.do")
	public String listPage(
			@RequestParam(defaultValue = "1") Integer currentPage, // 현재 페이지
			@RequestParam(defaultValue = "10") Integer postsPerPage, // 한 페이지 당 게시글 개수
			Model model
	) {
		model.addAttribute("key", Calendar.getInstance().getTimeInMillis());
		model.addAttribute("boardList", boardService.findPaginatedBoards(BOARD_TYPE_SEQ, currentPage, postsPerPage));
		System.out.println(boardService.findPaginatedBoards(BOARD_TYPE_SEQ, currentPage, postsPerPage));
		model.addAttribute("pagination", new Pagination(boardService.cntTotalPosts(BOARD_TYPE_SEQ), currentPage, postsPerPage));
		return "forum/notice/list";
	}
	
	@RequestMapping("/forum/notice/writePage.do")
	public ModelAndView writePage(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.setViewName("forum/notice/write");
		
		return mv;
	}
	
	@RequestMapping("/forum/notice/readPage.do")
	public ModelAndView readPage(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.setViewName("forum/notice/read");
		
		return mv;
	}
}
