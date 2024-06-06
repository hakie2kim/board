package com.portfolio.www.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.portfolio.www.dao.mybatis.BoardRepository;
import com.portfolio.www.dto.BoardListDto;
import com.portfolio.www.dto.BoardWriteDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	
	public int cntTotalPosts(int boardTypeSeq) {
		return boardRepository.cntTotalPosts(boardTypeSeq);
	}

	public List<BoardListDto> findPaginatedBoards(int boardTypeSeq, int currentPage, int postsPerPage) {
		int from = (currentPage - 1) * postsPerPage;
		return boardRepository.findPaginatedBoards(boardTypeSeq, from, postsPerPage);
	}

	public int writePost(BoardWriteDto boardWriteDto) {
		return boardRepository.addBoard(boardWriteDto);
	}
}
