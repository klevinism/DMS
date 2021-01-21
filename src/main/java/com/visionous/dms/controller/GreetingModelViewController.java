package com.visionous.dms.controller;

/**
 * @author delimeta
 *
 */
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.repository.AccountRepository;

@Controller
@RequestMapping("/")
public class GreetingModelViewController {
	private final Log logger = LogFactory.getLog(GreetingModelViewController.class);
	
	@Autowired
	private AccountRepository userRepository;
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/error")
	public String error(Model model) {
		return "demo_1/pages/samples/error-404";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String greeting(Model model) {
		return "redirect:/home";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/logout")
	public String logout(Model model) {
		Boolean auth = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
		if(auth != null) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		
		return "demo_1/index";
	}
	
	@GetMapping("/index")
	public String indexx(Model model) {
		
		
		return "index";
	}
	
	/**
	 * @param error
	 * @param model
	 * @return
	 */
	@GetMapping("/login")
	public String login(@RequestParam(name="error", required=false) String error, Model model) {
		
		if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			if(error != null) {
				model.addAttribute("errorUsernamePassword", "Username or password is incorrect.");
			}
			
			return "demo_1/pages/samples/login";
		}else {
			System.out.println(SecurityContextHolder.getContext().getAuthentication());
			return "redirect:/home";
		}
	}

	/**
	 * @return
	 */
	@GetMapping("/register")
	public String register() {
		return "demo_1/pages/samples/register";
	}
	
	/**
	 * @return
	 */
	@GetMapping("/form-elements")
	public String formElements() {
		return "demo_1/pages/forms/basic_elements";
	}
	
	/**
	 * @return
	 */
	@GetMapping("/dropdowns")
	public String dropdowns() {
		return "demo_1/pages/ui-features/dropdowns";
	}

	/**
	 * @return
	 */
	@GetMapping("/buttons")
	public String buttons() {
		return "demo_1/pages/ui-features/buttons";
	}
	
	/**
	 * @return
	 */
	@GetMapping("/typography")
	public String typography() {
		
		return "demo_1/pages/booking";
	}
	
	/**
	 * @return
	 */
	@GetMapping("/chartjs")
	public String chartjs() {
		return "demo_1/pages/charts/chartjs";
	}
	
	/**
	 * @return
	 */
	@GetMapping("/tables")
	public String tables() {
		return "demo_1/pages/tables/basic-table";
	}
	
	/**
	 * @return
	 */
	@GetMapping("/icons")
	public String icons() {
		return "demo_1/pages/icons/font-awesome";
	}
	
	/**
	 * @param user
	 * @param model
	 * @return
	 */
	@PostMapping("/register")
	public String register(@Valid Account user,
			Model model) {
		
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);

	    Iterable<Account> accounts = userRepository.findAll();
		model.addAttribute("user",accounts);
		
		return "demo_1/pages/samples/register";
	}
}