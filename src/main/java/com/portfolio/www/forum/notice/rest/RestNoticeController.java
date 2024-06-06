package com.portfolio.www.forum.notice.rest;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.portfolio.www.auth.SessionCookieConst;
import com.portfolio.www.dto.BoardWriteDto;
import com.portfolio.www.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestNoticeController {
	private final BoardService boardService;
	
	@PostMapping("/forum/notice/write.rest")
	public BoardWriteDto write(
			@Valid @RequestBody BoardWriteDto boardWriteDto,
			BindingResult bindingResult,
			@SessionAttribute(name = SessionCookieConst.LOGIN_MEMBER, required = false) Integer memberSeq
	) throws MethodArgumentNotValidException {
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		boardWriteDto.setRegMemberSeq(memberSeq);
		boardService.writePost(boardWriteDto);
		return boardWriteDto;
	}
	
}
