package com.portfolio.www.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.portfolio.www.dto.EmailDto;
import com.portfolio.www.dto.JoinForm;
import com.portfolio.www.dto.MemberAuthDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailUtil {
	private final JavaMailSender javaMailSender;
	private final String senderEmail;
	
	/*
	 * public void setJavaMailSender(JavaMailSender javaMailSender) {
	 * this.javaMailSender = javaMailSender; }
	 */
	
	public boolean sendEmail(EmailDto emailDto) {
		return sendEmail(emailDto, false);
	}
	
	public boolean sendEmail(EmailDto emailDto, boolean isHtml) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setTo(emailDto.getReceiver());
			messageHelper.setText(emailDto.getText());
			messageHelper.setFrom(emailDto.getFrom());
			messageHelper.setSubject(emailDto.getSubject()); // 제목은 생략 가능
			messageHelper.setText(emailDto.getText(), isHtml); // isHtml이 true인 경우 링크 클릭 가능
			javaMailSender.send(message);
		} catch (MessagingException e) {
			return false;
		}
		
		return true;
	}
	
	public EmailDto makeEmailDto(String receiverEmail, MemberAuthDto memberAuthDto) {
		EmailDto emailDto = new EmailDto();
		emailDto.setFrom(senderEmail);
		emailDto.setReceiver(receiverEmail);
		emailDto.setSubject("회원 가입 인증을 위한 메일 안내입니다.");
		
		// host + context root + uri
		String html = String.format("<a href='http://localhost:8080/pf/auth/emailAuth.do?uri=%s'>회원 가입 인증 하기</>",
				memberAuthDto.getAuthUri()
				);
		emailDto.setText(html);
		
		return emailDto;
	}
}
