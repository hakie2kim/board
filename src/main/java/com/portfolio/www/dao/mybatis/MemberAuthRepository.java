package com.portfolio.www.dao.mybatis;

import com.portfolio.www.dto.MemberAuthDto;

public interface MemberAuthRepository {
	public int addMemberAuth(MemberAuthDto memberAuthDto);
}
