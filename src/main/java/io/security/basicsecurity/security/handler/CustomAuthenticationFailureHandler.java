package io.security.basicsecurity.security.handler;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		
		String errorMessage = "Invalid Username or Password";
		
		if (exception instanceof BadCredentialsException) {
			errorMessage = "Invalid Username or Password";
		}
		else if (exception instanceof InsufficientAuthenticationException) {
			errorMessage = "Invalid Secret Key";
		}
		else if (exception instanceof CredentialsExpiredException) {
			errorMessage = "Expired password";
		}
		else if (exception instanceof DisabledException) {
			errorMessage = "Locked";
		}
		
		setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);
		
		super.onAuthenticationFailure(request, response, exception);
		
	}
}
