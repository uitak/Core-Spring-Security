package io.security.basicsecurity.controller.login;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.security.basicsecurity.domain.entity.Account;
import io.security.basicsecurity.security.service.AccountContext;
import io.security.basicsecurity.security.token.AjaxAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

	@GetMapping(value={"/login", "/api/login"})
	public String login(@RequestParam(value = "error", required = false) String error,
						@RequestParam(value = "exception", required = false) String exception,
						Model model) {
		
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
		
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return "redirect:/login";
	}
	
	@GetMapping(value={"/denied", "/api/denied"})
	public String accessDenied(@RequestParam(value = "exception", required = false) String exception, Principal principal, Model model) throws Exception {
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Account account = null;
		
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			System.out.println("UsernamePasswordAuthenticationToken 토큰을 변환");
			account = (Account) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
			//AccountContext ac = (AccountContext) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
			//account = ac.getAccount();
		}
		else if (principal instanceof AjaxAuthenticationToken) {
			System.out.println("AjaxAuthenticationToken 토큰을 변환");
			account = (Account) ((AjaxAuthenticationToken) principal).getPrincipal();
		}
		
		model.addAttribute("username", account.getUsername());
		model.addAttribute("exception", exception);
		
		return "user/login/denied";
	}
}
