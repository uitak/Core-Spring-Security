package io.security.basicsecurity.controller.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.security.basicsecurity.domain.Account;
import io.security.basicsecurity.domain.AccountDto;
import io.security.basicsecurity.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping(value="/users")
	public String createUser() {
		return "user/login/register";
	}
	
	@PostMapping(value="/users")
	public String createUser(AccountDto accountDto) {
		
		ModelMapper modelMapper = new ModelMapper();
		Account account = modelMapper.map(accountDto, Account.class);
		account.setPassword(passwordEncoder.encode(account.getPassword()));

		account.setRole("ROLE_USER");
		
		userService.createUser(account);
		
		return "redirect:/";
	}
	
	@GetMapping(value="/mypage")
	public String myPage() throws Exception {
		return "user/mypage";
	}
}
