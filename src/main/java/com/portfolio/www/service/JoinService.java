package com.portfolio.www.service;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portfolio.www.dao.mybatis.MemberAuthRepository;
import com.portfolio.www.dao.mybatis.MemberRepository;
import com.portfolio.www.dto.EmailDto;
import com.portfolio.www.dto.JoinForm;
import com.portfolio.www.dto.MemberAuthDto;
import com.portfolio.www.message.Message;
import com.portfolio.www.util.EmailUtil;
import com.portfolio.www.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinService {
	private final MemberRepository memberRepository;
	private final MemberAuthRepository memberAuthRepository;
	private final EmailUtil emailUtil;
	
	/*
	 * public int join(@RequestParam HashMap<String, String> params) { 
	 * return memberRepository.addMember(params); 
	 * }
	 */
	
	public boolean doesMemberIdExist(String memberId) {
		if (memberRepository.countMemberId(memberId) == 1) {
			return true;
		}
		return false;
	}
	
	public Message join(JoinForm joinForm) {
		// 1. 패스워드 암호화
		joinForm.setPasswd(PasswordUtil.encPassword(joinForm.getPasswd()));
		// 2. member db 반영
		memberRepository.addMember(joinForm);
		// 3. member_auth db 반영
		MemberAuthDto memberAuthDto = makeMemberAuthDto(joinForm.getMemberSeq());
		// 4. EmailDto 생성
		EmailDto emailDto = emailUtil.makeEmailDto(joinForm.getEmail(), memberAuthDto);
		// 5. 이메일 전송
		return emailUtil.sendEmail(emailDto, true) ? Message.AUTH_EMAIL_SEND_SUCCESS : Message.AUTH_EMAIL_SEND_FAIL;
	}
	
	private MemberAuthDto makeMemberAuthDto(int memberSeq) {
		MemberAuthDto memberAuthDto = new MemberAuthDto();
		// 1. member_seq 설정
		memberAuthDto.setMemberSeq(memberSeq);
		// 2. 인증 링크 설정
		memberAuthDto.setAuthUri(UUID.randomUUID().toString().replace("-", ""));
		// 3. 인증 링크 만료일자 설정
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 30); // 30분만 유효
		memberAuthDto.setExpireDtm(cal.getTimeInMillis());
		// 4. member_auth db 반영
		memberAuthRepository.addMemberAuth(memberAuthDto);
		return memberAuthDto;
	}

	public Message emailAuth(String uri) {
		MemberAuthDto memberAuthDto = memberAuthRepository.findMemberAuth(uri);
		
		// uri로 찾은 MemberAuthDto가 없다면 유효하지 않은 링크
		if (memberAuthDto == null) {
			return Message.AUTH_EMAIL_NOT_VALID;
		}
		
		// 현재 시간보다 인증 만료 시간이 작은 경우 
		if (Calendar.getInstance().getTimeInMillis() < memberAuthDto.getExpireDtm()) {
			memberAuthDto.setAuthYn("Y");
			memberAuthRepository.updateEmailAuthYn(memberAuthDto);
			return Message.AUTH_EMAIL_VALIDATED;
		}
		return Message.AUTH_EMAIL_EXPIRED;
	}
}