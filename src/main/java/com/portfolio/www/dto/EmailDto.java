package com.portfolio.www.dto;

import lombok.Data;

@Data
public class EmailDto {
	private String from;
	private String receiver;
	private String text;
	private String subject;
}
