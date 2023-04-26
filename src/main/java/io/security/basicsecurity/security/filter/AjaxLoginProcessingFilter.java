package io.security.basicsecurity.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.security.basicsecurity.domain.AccountDto;
import io.security.basicsecurity.security.token.AjaxAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Component
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private ObjectMapper objectMapper = new ObjectMapper();
	
	public AjaxLoginProcessingFilter(String url) {
		//super(url);
		super(new AntPathRequestMatcher(url, "POST"));
		//this.setAuthenticationManager(authManager);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
			throws AuthenticationException, IOException {
		
		System.out.println("Ajax 인증 필터: attemptAuthentication() 실행 ----------------------------------");
		
		if (!isAjax(request)) {
			throw new IllegalStateException("Authentication is not supported");
		}
		
		AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
		if (ObjectUtils.isEmpty(accountDto.getUsername()) || ObjectUtils.isEmpty(accountDto.getPassword())) {
			throw new IllegalArgumentException("Username or Password is empty");
		}
		
		AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());
		
		return this.getAuthenticationManager().authenticate(ajaxAuthenticationToken);
	}
	
	private boolean isAjax(HttpServletRequest request) {
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			return true;
		}
		return false;
	}
}
