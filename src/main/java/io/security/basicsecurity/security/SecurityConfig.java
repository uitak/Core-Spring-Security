package io.security.basicsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            
		http.authorizeHttpRequests()
            .anyRequest().authenticated();
            
        http.formLogin();
        
        http.rememberMe()
        	.rememberMeParameter("remember")
        	.tokenValiditySeconds(3600) // 초 단위(3600=1시간), Default 14일
        	.alwaysRemember(false)
        	.userDetailsService(null);

        http.sessionManagement()
        	.maximumSessions(1)
        	.maxSessionsPreventsLogin(false); // 동시 로그인 차단. Default는 false(기존 세션 만료)
        
        
        return http.build();
    }   
}
