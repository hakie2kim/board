package com.portfolio.www.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.portfolio.www.dao.mybatis.MemberRepository;

@Service
public class JoinService {
	@Autowired
	MemberRepository memberRepository;
	
	public int join(@RequestParam HashMap<String, String> params) {
		return memberRepository.addMember(params);
	}
}
