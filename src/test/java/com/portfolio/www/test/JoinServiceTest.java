package com.portfolio.www.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.www.dao.mybatis.MemberAuthRepository;
import com.portfolio.www.dao.mybatis.MemberRepository;
import com.portfolio.www.dto.JoinForm;
import com.portfolio.www.dto.MemberAuthDto;
import com.portfolio.www.dto.MemberDto;
import com.portfolio.www.message.Message;
import com.portfolio.www.service.JoinService;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)	// junit5부터 지원
@ContextConfiguration(locations = {"classpath:context-beans-test.xml", "classpath:context-datasource.xml", "classpath:**/pf-servlet.xml"})
@Slf4j
//@RequiredArgsConstructor
class JoinServiceTest {
	@Autowired
//	@Resource(name = "joinService")
	private JoinService joinService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberAuthRepository memberAuthRepository;
	
	private JoinForm joinForm;
	private MemberAuthDto memberAuthDto;
	private Message msgAftJoin;
	private String validAuthUri;
	private static String INVALID_AUTH_URI = "28f1cbd0241e49dda461e2d7dca32064";
	
	@BeforeEach
	void initMemberAndMemberAuth() {
		joinForm = new JoinForm();
		joinForm.setMemberId("test001");
		joinForm.setPasswd("test001");
		joinForm.setMemberNm("테스트 계정");
		joinForm.setEmail("hakie2kim@gmail.com");
		msgAftJoin = joinService.join(joinForm);
		
		memberAuthDto = memberAuthRepository.findMemberAuthByMemberSeq(joinForm.getMemberSeq());
		validAuthUri = memberAuthDto.getAuthUri();
	}
	
	@Test
	@Transactional
	@DisplayName("가입한 아이디를 사용해 이미 존재하는 아이디인지 확인")
	void testDoesMemberIdExist() {
		assertTrue(joinService.doesMemberIdExist("test001"));
	}
	
	@Test
	@Transactional
	@DisplayName("가입하지 않은 아이디를 사용해 이미 존재하는 아이디인지 확인")
	void testDoesMemberIdNotExist() {
		assertFalse(joinService.doesMemberIdExist("test002"));
	}
	
	@Test
	@Transactional
	@DisplayName("회원 가입 - member, member_auth에 insert 여부 확인, join() 메서드의 리턴 값 Message 확인")
	void testJoin() {
		// member, member_auth db insert 확인
		MemberDto memberDto = memberRepository.findMember(joinForm.getMemberId());
		assertEquals(memberDto.getMemberId(), "test001");
		assertEquals(memberAuthRepository.findAuthYn(memberDto.getMemberSeq()), "N");
		
		// join() 메서드의 결과 확인
		assertEquals(msgAftJoin, Message.AUTH_EMAIL_SEND_SUCCESS);
	}

	@Test
	@Transactional
	@DisplayName("유효하지 않은 인증 이메일 URI인지 확인")
	void testAuthEmailNotValid() {
		Message msg = joinService.emailAuth(INVALID_AUTH_URI);
		assertEquals(msg, Message.AUTH_EMAIL_NOT_VALID);
	}

	@Test
	@Transactional
	@DisplayName("유효한 인증 이메일 URI인지 확인")
	void testAuthEmailValidated() {
		Message msg = joinService.emailAuth(validAuthUri);
		assertEquals(msg, Message.AUTH_EMAIL_VALIDATED);
	}
	
	@Test
	@Transactional
	@DisplayName("만료된 인증 이메일인지 확인")
	void testAuthEmailExpired() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -30); // 현재 시간 30분 전으로 설정
		memberAuthDto.setExpireDtm(cal.getTimeInMillis());
		
		Message msg = joinService.emailAuth(validAuthUri);
		assertEquals(msg, Message.AUTH_EMAIL_EXPIRED);
	}
}
