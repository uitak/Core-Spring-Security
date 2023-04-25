package io.security.basicsecurity.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws StreamWriteException, DatabindException, IOException {
		
		String errorMessage = "Invalid Username or Password";
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		if (exception instanceof BadCredentialsException) {
			errorMessage = "Invalid Username or Password";
		}
		else if (exception instanceof CredentialsExpiredException) {
			errorMessage = "Expired password";
		}
		else if (exception instanceof DisabledException) {
			errorMessage = "Locked";
		}
		
		objectMapper.writeValue(response.getWriter(), errorMessage);
	}
}
