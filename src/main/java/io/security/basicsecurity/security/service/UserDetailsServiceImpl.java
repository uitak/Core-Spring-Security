package io.security.basicsecurity.security.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.security.basicsecurity.domain.entity.Account;
import io.security.basicsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;

	@Override
	@Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println("커스텀: loadUserByUsername() 실행 ------------------------------");
		
        Account account = userRepository.findByUsername(username);
        if (account == null) {
        	throw new UsernameNotFoundException("No user found with username: " + username);
        }
        
        Set<String> userRoles = account.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRoleName())
                .collect(Collectors.toSet());
        
        List<GrantedAuthority> collect = userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new AccountContext(account, collect);
    }
}
