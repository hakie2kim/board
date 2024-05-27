package com.portfolio.www.auth;

import java.util.Calendar;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JoinController {
	
	@RequestMapping("/auth/joinPage.do")
	public ModelAndView joinPage(@RequestParam HashMap<String, String> params) {		
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.setViewName("auth/join");
		
		return mv;
	}
}
