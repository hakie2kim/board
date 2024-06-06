package com.portfolio.www.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class BoardWriteDto {
	private int boardSeq;
	
	@NotNull
	private int boardTypeSeq;
	
	@NotBlank
	@Size(min = 5, max = 50)
	private String title;
	
	@NotBlank
	@Size(min = 5, max = 1000)
	private String content;
	
	private int regMemberSeq;
}
