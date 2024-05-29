package com.portfolio.www.dto;

import lombok.Data;

@Data
public class MemberAuthDto {
	private int authSeq;
	private int memberSeq;
	private String authNum;
	private String authUri;
	private String regDtm;
	private Long expireDtm;
	private String authYn;
}
