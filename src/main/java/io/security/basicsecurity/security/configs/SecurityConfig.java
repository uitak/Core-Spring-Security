package io.security.basicsecurity.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	// 정적인 자원(css, js, image)에 대해서 스프링 시큐리티 필터를 거치지 않도록 설정.
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css", "/js", "/image");
    }
	
	
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
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests()
			.requestMatchers("/", "/users").permitAll()
			.requestMatchers("/mypage").hasAnyRole("USER", "MANAGER", "ADMIN")
			.requestMatchers("/messages").hasAnyRole("MANAGER", "ADMIN")
			.requestMatchers("/config").hasRole("ADMIN")
			.anyRequest().authenticated();

		http.formLogin();

		/*
		http.rememberMe()
			.rememberMeParameter("remember")
			.tokenValiditySeconds(3600) // 초 단위(3600=1시간), Default 14일
			.alwaysRemember(false)
			.userDetailsService(userDetailsService);

		http.sessionManagement()
			.maximumSessions(1)
			.maxSessionsPreventsLogin(false); // 동시 로그인 차단. Default는 false(기존 세션 만료)
		*/
		
		// h2-console 페이지 표시 안될 경우 필요.
        http.headers().frameOptions().disable();

		return http.build();
	}
}
