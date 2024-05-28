package com.portfolio.www.service;

import org.springframework.stereotype.Service;

import com.portfolio.www.dao.mybatis.MemberRepository;
import com.portfolio.www.dto.JoinForm;
import com.portfolio.www.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinService {
	private final MemberRepository memberRepository;
	
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
	
	public int join(JoinForm joinForm) {
		// 패스워드 암호화
		joinForm.setPasswd(PasswordUtil.encPassword(joinForm.getPasswd()));
		return memberRepository.addMember(joinForm);
	}
}