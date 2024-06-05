package com.portfolio.www.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private Date regDtm;
	private int regMemberSeq;
	private String updateDtm;
	private int updateMemberSeq;
		
	// Member
	private String memberNm;
	
	public void setRegDtm(String regDtm) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			this.regDtm = formatter.parse(regDtm);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
