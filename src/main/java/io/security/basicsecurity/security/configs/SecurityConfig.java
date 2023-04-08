package io.security.basicsecurity.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

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
			.requestMatchers("/").permitAll()
			.requestMatchers("/mypage").hasRole("USER")
			.requestMatchers("/messages").hasRole("MANAGER")
			.requestMatchers("/config").hasRole("ADMIN")
			// .requestMatchers("/config").hasAnyRole("ADMIN", "SYS")
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

		return http.build();
	}
}
