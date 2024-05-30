package com.portfolio.www.auth;

import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
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
	public ModelAndView loginPage(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.addObject("loginForm", new JoinForm());
		mv.setViewName("auth/login");
		return mv;
	}
	
	@PostMapping("/auth/login.do")
	public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest req) {
		if (bindingResult.hasErrors()) {
			return "auth/login";
		}
		
		MemberDto memberDto = loginService.login(loginForm);
		
		if (memberDto == null) {
			bindingResult.reject("loginFail", Message.ID_OR_PWD_IS_WRONG.getDescription());
			return "auth/login";
		}
		
		// 로그인 성공
		
		// 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
		req.getSession().setAttribute(SessionConst.LOGIN_MEMBER, memberDto.getMemberSeq());
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
