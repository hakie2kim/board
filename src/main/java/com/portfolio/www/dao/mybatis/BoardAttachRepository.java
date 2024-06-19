package com.portfolio.www.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.portfolio.www.dto.BoardAttachDto;

@Repository
public interface BoardAttachRepository {
	void addBoardAttach(BoardAttachDto boardAttachDto);
}
