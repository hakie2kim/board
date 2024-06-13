package com.portfolio.www.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.www.dao.mybatis.BoardAttachRepository;
import com.portfolio.www.dao.mybatis.BoardRepository;
import com.portfolio.www.dto.BoardAttachDto;
import com.portfolio.www.dto.BoardListDto;
import com.portfolio.www.dto.BoardWriteDto;
import com.portfolio.www.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	private final BoardAttachRepository boardAttachRepository;
	private final FileUtil fileUtil;
	
	public int cntTotalPosts(int boardTypeSeq) {
		return boardRepository.cntTotalPosts(boardTypeSeq);
	}

	public List<BoardListDto> findPaginatedBoards(int boardTypeSeq, int currentPage, int postsPerPage) {
		int from = (currentPage - 1) * postsPerPage;
		return boardRepository.findPaginatedBoards(boardTypeSeq, from, postsPerPage);
	}

	public boolean writePost(BoardWriteDto boardWriteDto, MultipartFile[] attFiles) {
		if (attFiles == null)
			return boardRepository.addBoard(boardWriteDto) == 1 ? true : false;
		
		return uploadFiles(boardWriteDto, attFiles);
	}
	
	public boolean uploadFiles(BoardWriteDto boardWriteDto, MultipartFile[] attFiles) {
		File file = null;
		
		try {
			for (MultipartFile mf : attFiles) {
				// 원본 파일 이름이 빈 경우 = 파일 3개 중 하나라도 업로드 하지 않은 경우 -> 그 다음 MultipartFile로 넘어가기
				if (ObjectUtils.isEmpty(mf.getOriginalFilename()))
					continue;
				
				// 2. 물리적 파일 저장
				file = fileUtil.saveFile(mf);

				BoardAttachDto boardAttachDto = new BoardAttachDto();
				boardAttachDto.setBoardSeq(boardWriteDto.getBoardSeq());
				boardAttachDto.setBoardTypeSeq(boardWriteDto.getBoardTypeSeq());
				boardAttachDto.setOrgFileNm(mf.getOriginalFilename());
				boardAttachDto.setChngFileNm(file.getName());
				boardAttachDto.setFileType(mf.getContentType());
				boardAttachDto.setFileSize(mf.getSize());
				boardAttachDto.setSavePath(file.getAbsolutePath());

				// 3. DB에 파일 정보 저장
				boardAttachRepository.addBoardAttach(boardAttachDto);
			}

			return true;
		} catch (IllegalStateException | IOException e) {
			// 물리적 파일 지우기
			if (ObjectUtils.isEmpty(file))
				file.delete();

			return false;
		}
	}
}
