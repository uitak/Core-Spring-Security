package io.security.basicsecurity.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.security.basicsecurity.security.common.FormWebAuthenticationDetails;
import io.security.basicsecurity.security.service.AccountContext;
import jakarta.transaction.Transactional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;

	private PasswordEncoder passwordEncoder;
	
	public CustomAuthenticationProvider(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		System.out.println("커스텀: authenticate() 실행 ------------------------------");

		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

		if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		FormWebAuthenticationDetails formWebAuthenticationDetails = 
				(FormWebAuthenticationDetails) authentication.getDetails();
		String secretKey = formWebAuthenticationDetails.getSecretKey();

		if (secretKey == null || !"secret".equals(secretKey)) {
			throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
		}

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				accountContext.getAccount(), null, accountContext.getAuthorities());

		return authenticationToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
