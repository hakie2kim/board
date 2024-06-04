package com.portfolio.www.message;

import lombok.Getter;

@Getter
public enum Message {
	AUTH_EMAIL_SEND_SUCCESS(2000, "발송된 인증 메일을 확인하세요."),
	AUTH_EMAIL_SEND_FAIL(2001, "인증 메일 발송에 실패했습니다."),
	AUTH_EMAIL_NOT_VALID(2002, "유효하지 않은 인증 링크입니다."),
	AUTH_EMAIL_VALIDATED(2003, "이메일 인증에 성공했습니다."),
	AUTH_EMAIL_NOT_VALIDATED(2004, "이메일 인증을 완료해주세요."),
	AUTH_EMAIL_EXPIRED(2004, "만료된 인증 링크입니다."), 
	
	ID_OR_PWD_IS_WRONG(3000, "아이디 또는 비밀번호가 맞지 않습니다.");

	Message(int code, String description) {
		this.code = code;
		this.description = description; 
	}
	
	private int code;
	private String description;
}
