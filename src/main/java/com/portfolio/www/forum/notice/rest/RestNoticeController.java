package com.portfolio.www.forum.notice.rest;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

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
			@Valid @RequestPart("boardWriteDto") BoardWriteDto boardWriteDto,
			BindingResult bindingResult,
			@RequestPart(value = "attFiles", required=false) MultipartFile[] attFiles,
			@SessionAttribute(name = SessionCookieConst.LOGIN_MEMBER, required = false) Integer memberSeq
	) throws MethodArgumentNotValidException {
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		boardWriteDto.setRegMemberSeq(memberSeq);
		boardService.writePost(boardWriteDto, attFiles);
		return boardWriteDto;
	}
	
}
