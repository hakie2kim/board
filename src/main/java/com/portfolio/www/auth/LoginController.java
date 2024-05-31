package com.portfolio.www.auth;

import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.portfolio.www.dto.JoinForm;
import com.portfolio.www.dto.LoginForm;
import com.portfolio.www.dto.MemberDto;
import com.portfolio.www.message.Message;
import com.portfolio.www.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;
	
	@RequestMapping("/auth/loginPage.do")
	public ModelAndView loginPage(@RequestParam HashMap<String, String> params, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.addObject("loginForm", new JoinForm());
		Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SessionCookieConst.REMEBER_ME.equals(cookie.getName())) {
                    String memberId = cookie.getValue();
                    req.getSession().setAttribute(SessionCookieConst.REMEBER_ME, memberId);
                    break;
                }
            }
        }
		mv.setViewName("auth/login");
		return mv;
	}
	
	@PostMapping("/auth/login.do")
	public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest req, HttpServletResponse resp) {
		if (bindingResult.hasErrors()) {
			return "auth/login";
		}
		
		// 아이디, 패스워드 일치 여부 확인
		MemberDto memberDto = loginService.login(loginForm);
		if (memberDto == null) {
			bindingResult.reject("loginFail", Message.ID_OR_PWD_IS_WRONG.getDescription());
			return "auth/login";
		}
		
		// 이메일 유효 여부 인증 확인
		if (!loginService.isEmailAuthenticated(memberDto.getMemberSeq())) {
			bindingResult.reject("emailNotValidated", Message.AUTH_EMAIL_NOT_VALIDATED.getDescription());
			return "auth/login";
		}
		
		// 로그인 성공
		
		// 아이디 기억하기
		if (loginForm.isRememberMe()) {
            Cookie cookie = new Cookie(SessionCookieConst.REMEBER_ME, loginForm.getMemberId());
            cookie.setMaxAge(7 * 24 * 60 * 60); // 일주일
            cookie.setHttpOnly(true);
            resp.addCookie(cookie);
        } else {
        	Cookie cookie = new Cookie(SessionCookieConst.REMEBER_ME, null);
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
        }
		
		// 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
		req.getSession().setAttribute(SessionCookieConst.LOGIN_MEMBER, memberDto.getMemberSeq());
		// String referer = req.getHeader("referer");
		return "redirect:/index.do";
	}
	
	@PostMapping("/auth/logout.do") 
	public String login(HttpServletRequest req) {
		// 세션이 있으면 있는 세션 반환, 없으면 null 반환
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/index.do";
	}
	
	@RequestMapping("/auth/recoverPassPage.do")
	public ModelAndView recoverPassPage(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.setViewName("auth/recover-pass");
		return mv;
	}
}
