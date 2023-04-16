package io.security.basicsecurity.security.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private String errorPage;
	
	//private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String deniedUrl = errorPage + "?exception=" + accessDeniedException.getMessage();
		//redirectStrategy.sendRedirect(request, response, deniedUrl);
		response.sendRedirect(deniedUrl);
	}
	
	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
}
