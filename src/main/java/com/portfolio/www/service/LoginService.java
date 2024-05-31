package com.portfolio.www.service;

import org.springframework.stereotype.Service;

import com.portfolio.www.dao.mybatis.MemberAuthRepository;
import com.portfolio.www.dao.mybatis.MemberRepository;
import com.portfolio.www.dto.LoginForm;
import com.portfolio.www.dto.MemberDto;
import com.portfolio.www.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	private final MemberRepository memberRepository;
	private final MemberAuthRepository memberAuthRepository;
	
	public MemberDto login(LoginForm loginForm) {
		MemberDto memberDto = memberRepository.findMember(loginForm.getMemberId());
		
		boolean result = false;
		if (memberDto != null) {
			result = PasswordUtil.verifyPassword(loginForm.getPasswd(), memberDto.getPasswd());
		}
		
		// 아이디로 조회한 회원이 존재하고 패스워드가 일치할 경우: memberDto
		return memberDto != null && result ? memberDto : null;
	}

	public boolean isEmailAuthenticated(int memberSeq) {
		if (memberAuthRepository.findAuthYn(memberSeq).equals("Y")) {
			return true;
		}
		return false;
	}

}
