package com.portfolio.www.dao.mybatis;

import org.apache.ibatis.annotations.Param;

import com.portfolio.www.dto.MemberAuthDto;

public interface MemberAuthRepository {
	public int addMemberAuth(MemberAuthDto memberAuthDto);
	public MemberAuthDto findMemberAuth(String uri);
	public int updateEmailAuthYn(MemberAuthDto memberAuthDto);
	public void updateMemberAuth(MemberAuthDto memberAuthDto);
}
