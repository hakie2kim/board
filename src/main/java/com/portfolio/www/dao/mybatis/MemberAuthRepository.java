package com.portfolio.www.dao.mybatis;

import com.portfolio.www.dto.MemberAuthDto;

public interface MemberAuthRepository {
	public int addMemberAuth(MemberAuthDto memberAuthDto);
	public MemberAuthDto findMemberAuth(String uri);
	public MemberAuthDto findMemberAuthByMemberSeq(int memberSeq);
	public String findAuthYn(int memberSeq);
	public int updateEmailAuthYn(MemberAuthDto memberAuthDto);
	public void updateMemberAuth(MemberAuthDto memberAuthDto);
}
