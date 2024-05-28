package com.portfolio.www.message;

public enum Message {
	AUTH_EMAIL_SEND_SUCCESS(2000, "발송된 인증 메일을 확인하세요."),
	AUTH_EMAIL_SEND_FAIL(2001, "인증 메일 발송에 실패했습니다. 관리자에게 문의하세요.");

	Message(int code, String description) {
		this.code = code;
		this.description = description; 
	}
	
	private int code;
	private String description;
	
	public int getCode() {
		return code;
	}
	public String getDescription() {
		return description;
	}
}
