package com.portfolio.www.dto;

import lombok.Data;

@Data
public class BoardAttachDto {
	private int attachSeq;
	private int boardSeq;
	private int boardTypeSeq;
	private String orgFileNm;
	private String savePath;
	private String chngFileNm;
	private long fileSize;
	private String fileType;
	private String accessUri;
	private String regDtm;
}
