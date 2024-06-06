package com.portfolio.www.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.portfolio.www.dto.BoardListDto;
import com.portfolio.www.dto.BoardWriteDto;

@Repository
public interface BoardRepository {
	int cntTotalPosts(int boardTypeSeq);
	List<BoardListDto> findPaginatedBoards(@Param("boardTypeSeq") int boardTypeSeq, @Param("from") int from, @Param("postsPerPage") int postsPerPage);
	int addBoard(BoardWriteDto boardWriteDto);
}
