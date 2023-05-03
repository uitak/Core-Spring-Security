package io.security.basicsecurity.security.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.security.basicsecurity.security.handler.CustomAccessDeniedHandler;
import io.security.basicsecurity.security.handler.CustomAuthenticationFailureHandler;
import io.security.basicsecurity.security.handler.CustomAuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;

@Order(2)
@Configuration
public class SecurityConfig {
	
	@Autowired
	private CustomAuthenticationFailureHandler formAuthenticationFailureHandler;
	
	@Autowired
	private CustomAuthenticationSuccessHandler formAuthenticationSuccessHandler;
	
	@Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
	
	// 정적인 자원(css, js, image)에 대해서 스프링 시큐리티 필터를 거치지 않도록 설정.
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
		RequestMatcher databaseMatcher = new AntPathRequestMatcher("/h2-console/**");
		RequestMatcher resourceMatcher = new AntPathRequestMatcher("/images/**");
		RequestMatcher jsMatcher = new AntPathRequestMatcher("/js/**");
		RequestMatcher cssMatcher = new AntPathRequestMatcher("/css/**");
		
		return (web) -> web.ignoring().requestMatchers(
					new OrRequestMatcher(resourceMatcher, databaseMatcher, jsMatcher, cssMatcher)
				);
    }
	
	/*
	@Bean
	public UserDetailsService users() {
		
		String pw = passwordEncoder().encode("1111");
		
		UserDetails user = 
				User.builder()
					.username("user")
					.password(pw)
					.roles("USER")
					.build();

		UserDetails manager = 
				User.builder()
				.username("manager")
				.password(pw)
				.roles("MANAGER")
				.build();

		UserDetails admin = 
				User.builder()
				.username("admin")
				.password(pw)
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user, manager, admin);
	}
	*/
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests()
			.requestMatchers("/", "/users", "/login*").permitAll()
			.requestMatchers("/mypage").hasAnyRole("USER", "MANAGER", "ADMIN")
			.requestMatchers("/messages").hasAnyRole("MANAGER", "ADMIN")
			.requestMatchers("/config").hasRole("ADMIN")
			.anyRequest().authenticated()

		.and()
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/login_proc")
			.authenticationDetailsSource(authenticationDetailsSource)
			.successHandler(formAuthenticationSuccessHandler)
			.failureHandler(formAuthenticationFailureHandler)
			.permitAll()
		;

		http
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
		;
		
		// h2-console 페이지 표시 안될 경우 필요.
        http.headers().frameOptions().disable();
        
        //http.csrf().disable();
		return http.build();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
		accessDeniedHandler.setErrorPage("/denied");

		return accessDeniedHandler;
	}

}
