package com.visionaus.dms.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.visionaus.dms.pojo.User;

@Controller
public class GreetingController {

	@GetMapping("/")
	public String greeting(@RequestParam(name="email", required=false) String email,
			@RequestParam(name="password", required=false) String password,
			Model model) {
		
		return "demo_1/index";
	}
//	
//	@PostMapping("/")
//	public String auth(@RequestParam(name="username", required=true) String username,
//			@RequestParam(name="password", required=true) String password,
//			Model model) {
//		
//		model.addAttribute("username", username);
//		
//		System.out.println(username);
//		
//		return "demo_1/index";
//	}
//	
	@GetMapping("/login")
	public String login() {
		return "demo_1/pages/samples/login";
	}

	@GetMapping("/register")
	public String register() {
		return "demo_1/pages/samples/register";
	}
	
	@PostMapping("/register")
	public String register(@Valid User user,
			Model model) {
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getTerms());
		
		return "demo_1/pages/samples/register";
	}
}