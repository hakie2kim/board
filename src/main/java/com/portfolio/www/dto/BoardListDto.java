package com.portfolio.www.dto;

import lombok.Data;

@Data
public class BoardListDto {
	// Board
	private int boardSeq;
	private int boardTypeSeq;
	private String title;
	private String content;
	private int hit;
	private String delYn;
	private String regDtm;
	private int regMemberSeq;
	private String updateDtm;
	private int updateMemberSeq;
		
	// Member
	private String memberNm;
}
