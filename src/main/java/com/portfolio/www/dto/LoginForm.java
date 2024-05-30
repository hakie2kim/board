package com.portfolio.www.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {
	@NotBlank
	private String memberId;
	
	@NotBlank
	private String passwd;
}