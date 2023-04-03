package io.security.basicsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@GetMapping(value="/")
	@ResponseBody
	public String home() throws Exception {
		return "home";
	}

	@GetMapping(value="/user")
	@ResponseBody
	public String user() throws Exception {
		return "user";
	}
	
	@GetMapping(value="/admin/pay")
	@ResponseBody
	public String adminPay() throws Exception {
		return "adminPay";
	}
	
	@GetMapping(value="/admin/**")
	@ResponseBody
	public String admin() throws Exception {
		return "admin";
	}

}
