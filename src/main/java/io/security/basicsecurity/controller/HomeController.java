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

	@GetMapping(value="/abc")
	@ResponseBody
	public String login() throws Exception {
		return "login";
	}

}
