package com.portfolio.www.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.portfolio.www.auth.SessionCookieConst;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, servletNames = { "pf", "pf-rest" })
public class LoginFilter extends HttpFilter implements Filter {
	private final String[] LOGIN_REQUIRED_URIS = {
			"/forum/notice/listPage.do",
			"/forum/notice/writePage.do"
	};
	private final String LOGIN_PAGE_URI = "/auth/loginPage.do";
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public LoginFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		String requestURL = httpServletRequest.getRequestURL().toString();
		String requestURI = httpServletRequest.getRequestURI();
		HttpSession session = httpServletRequest.getSession(false);
		String contextPath = httpServletRequest.getContextPath();
		
		// 요청 URI가 로그인이 필요한 URI 배열에 존재하는지 확인
		if (Arrays.asList(LOGIN_REQUIRED_URIS).contains(requestURI.replace(contextPath, ""))) {
			// 세션에 memberId의 값이 존재하는지 확인
			if (session == null || session.getAttribute(SessionCookieConst.LOGIN_MEMBER) == null) {
				// 존재하지 않으면 로그인 페이지로 리다이렉트
				httpServletResponse.sendRedirect(contextPath + LOGIN_PAGE_URI + "?reqURL=" + requestURL);
				return;
			}
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
}