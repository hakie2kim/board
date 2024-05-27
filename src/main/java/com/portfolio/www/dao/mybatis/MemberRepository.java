package com.portfolio.www.dao.mybatis;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {
	
	public int addMember(HashMap<String, String> params);
}
