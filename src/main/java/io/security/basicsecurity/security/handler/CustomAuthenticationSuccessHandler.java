package io.security.basicsecurity.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
			throws IOException, ServletException {
		
		System.out.println("커스텀: onAuthenticationSuccess() 실행 ---------------------");

		setDefaultTargetUrl("/");
		
		System.out.println("getDefaultTargetUrl()" + getDefaultTargetUrl());
		
		SavedRequest saveRequest = requestCache.getRequest(request, response);
		if (saveRequest != null) {
			String targetUrl = saveRequest.getRedirectUrl();
			
			System.out.println("targetUrl: " + targetUrl);
			
			redirectStrategy.sendRedirect(request, response, targetUrl);
		}
		else {
			redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
		}
		
	}
}
