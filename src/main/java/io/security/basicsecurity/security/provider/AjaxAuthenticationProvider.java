package io.security.basicsecurity.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.security.basicsecurity.security.service.AccountContext;
import io.security.basicsecurity.security.token.AjaxAuthenticationToken;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		System.out.println("커스텀(ajax): authenticate() 실행 ------------------------------");

		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

		if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		/*
		FormWebAuthenticationDetails formWebAuthenticationDetails = 
				(FormWebAuthenticationDetails) authentication.getDetails();
		String secretKey = formWebAuthenticationDetails.getSecretKey();
		
		if (secretKey == null || !"secret".equals(secretKey)) {
			throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
		}
		*/

		AjaxAuthenticationToken authenticationToken = new AjaxAuthenticationToken(
				accountContext.getAccount(), null, accountContext.getAuthorities());

		return authenticationToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(AjaxAuthenticationToken.class);
	}
}
