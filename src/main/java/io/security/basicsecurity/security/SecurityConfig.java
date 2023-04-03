package io.security.basicsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Bean
	public UserDetailsService users() {
		UserDetails user = User.builder()
			.username("user")
			.password("{noop}1111")
			.roles("USER")
			.build();
		
		UserDetails sys = User.builder()
			.username("sys")
			.password("{noop}1111")
			.roles("USER", "SYS")
			.build();
		
		UserDetails admin = User.builder()
			.username("admin")
			.password("{noop}1111")
			.roles("USER", "SYS", "ADMIN")
			.build();

		return new InMemoryUserDetailsManager(user, sys, admin);
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            
		http.authorizeHttpRequests()
            .requestMatchers("/user").hasRole("USER")
            .requestMatchers("/admin/pay").hasRole("ADMIN")
            .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SYS")
            .anyRequest().authenticated();
            
        http.formLogin();
        
        http.rememberMe()
        	.rememberMeParameter("remember")
        	.tokenValiditySeconds(3600) // 초 단위(3600=1시간), Default 14일
        	.alwaysRemember(false)
        	.userDetailsService(userDetailsService);

        http.sessionManagement()
        	.maximumSessions(1)
        	.maxSessionsPreventsLogin(false); // 동시 로그인 차단. Default는 false(기존 세션 만료)
            
        return http.build();
    }   
}
