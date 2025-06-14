package com.visionous.dms.controller;

import java.time.LocalDateTime;
/**
 * @author delimeta
 *
 */
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.visionous.dms.pojo.GlobalSettings;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.persistence.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnResetPasswordEvent;
import com.visionous.dms.pojo.Reset;
import com.visionous.dms.pojo.Verification;
import com.visionous.dms.service.ResetService;
import com.visionous.dms.service.VerificationService;

@Controller
@RequestMapping("/")
public class GreetingModelViewController {
	private final Log logger = LogFactory.getLog(GreetingModelViewController.class);
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private VerificationService verificationService;
	@Autowired
    private MessageSource messages;
	@Autowired
	private ResetService resetService;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private HttpServletRequest httpServletRequest;
	@Autowired
	private HttpServletResponse httpServletResponse;
	
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
    @GetMapping("/admin")
    public String homeAdminDefault(Model model) {
        return "redirect:/home";
    }
	
//	/**
//	 * @param model
//	 * @return
//	 */
//	@GetMapping("/logout")
//	public String logout(Model model, HttpServletRequest req) throws ServletException {
//		Boolean auth = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
//		if(auth != null) {
//			SecurityContextHolder.clearContext();
//		}
//
//		return "demo_1/index";
//	}
	
	@GetMapping("/index")
	public String indexx(Model model) {
		return "index";
	}
    
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/confirm")
	public String emailConfirmation(@RequestParam(name = "token", required = true) String token, Model model) {
		Verification verificationToken = verificationService.findByToken(token);
	    if (verificationToken == null) {
	        String message = messages.getMessage("alert.invalidToken", null, LocaleContextHolder.getLocale());

	        model.addAttribute("errorMessage", message);
	        return "demo_1/pages/confirmation";
	    }
		
	    Account user = verificationToken.getAccount();
	    if (!verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
	        String message = messages.getMessage("alert.tokenExpired", null, LocaleContextHolder.getLocale());
	        model.addAttribute("errorMessage", message);
	        return "demo_1/pages/confirmation";
	    }
	    user.setEnabled(true);
	    user.setActive(true);
	    accountService.createPlain(user);
		return "redirect:/login";
	}
	
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/reset")
	public String reset(Model model) {
        
		return "demo_1/pages/reset_password";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/subscription")
	public String expiredSubscriptionDefaultView(Model model, @RequestParam(name = "status", required = false) String status) {
		model.addAttribute("status", status);
		model.addAttribute("currentPage", LandingPages.SUBSCRIPTION.value());
		
		model.addAttribute("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		model.addAttribute("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		model.addAttribute("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		model.addAttribute("logo", (AccountUtil.currentLoggedInBusinessSettings()).getBusinessImage());
		model.addAttribute("settings", (AccountUtil.currentLoggedInBusinessSettings()));
		model.addAttribute("subscription", (AccountUtil.currentLoggedInBusinessSettings()).getActiveSubscription());
		
		return "demo_1/pages/expiredsubscription";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/appointmentVerification")
	public String appointmentVerification(Model model) {
        
		return "demo_1/partials/emails/appointmentVerification";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@PostMapping("/reset")
	public String resetPassword(@NotNull @NotEmpty @RequestParam(name = "username", required = true) String username, Model model) {
		String appUrl = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getContextPath();
		
		if(Objects.nonNull(username) && !StringUtils.isEmpty(username)) {
		    Optional<Account> user = accountService.findByUsernameOrEmail(username);
		    if(user.isPresent()) {
		    	System.out.println("publishing..");
		    	eventPublisher.publishEvent(
	        		new OnResetPasswordEvent(user.get(), LocaleContextHolder.getLocale(), appUrl)
	        		);
		    	String message = messages.getMessage("alert.checkInboxForEmail", null, LocaleContextHolder.getLocale());
		    	model.addAttribute("successMessage", message);
		    }else {
		    	String message = messages.getMessage("alert.tokenExpired", null, LocaleContextHolder.getLocale());
		        model.addAttribute("errorMessage", message);
		    }
		}else {
			String message = messages.getMessage("alert.fieldEmpty", null, LocaleContextHolder.getLocale());
	        model.addAttribute("errorMessage", message);
		}
		return "demo_1/pages/reset_password";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/confirmReset")
	public String resetConfirm(@RequestParam(name = "token", required = true) String token, Model model) {
		
		Reset resetToken = resetService.findByToken(token);
	    if (resetToken == null) {
	    	String message = messages.getMessage("alert.invalidToken", null, LocaleContextHolder.getLocale());
	        model.addAttribute("errorMessage", message);
	        return "demo_1/pages/reset_password";
	    }
		
	    if (!resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
	    	String message = messages.getMessage("alert.tokenExpired", null, LocaleContextHolder.getLocale());
	        model.addAttribute("errorMessage", message);
	        return "demo_1/pages/reset_password";
	    }
	    
	    model.addAttribute("token", token);
	    return "demo_1/pages/update_password";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@PostMapping("/confirmReset")
	public String resetConfirmPost(@RequestParam(name = "password", required = true) String password, 
			@RequestParam(name = "token", required = true) String token, Model model) {
		
		Reset resetToken = resetService.findByToken(token);
	    if (resetToken == null) {
	    	String message = messages.getMessage("alert.invalidToken", null, LocaleContextHolder.getLocale());
	        model.addAttribute("errorMessage", message);
	        return "demo_1/pages/reset_password";
	    }
		
	    Account user = resetToken.getAccount();
	    if (!resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
	    	String message = messages.getMessage("alert.tokenExpired", null, LocaleContextHolder.getLocale());
	        model.addAttribute("errorMessage", message);
	        return "demo_1/pages/reset_password";
	    }
	    
	    user.setPassword(new BCryptPasswordEncoder().encode(password));
	    if(accountService.createPlain(user) != null) {
	    	String message = messages.getMessage("alert.passwordChangedSuccessfully", null, LocaleContextHolder.getLocale());
	    	model.addAttribute("successReset", message);
	    	return "redirect:/login";
	    }else {
	    	String message = messages.getMessage("alert.couldNotResetPassword", null, LocaleContextHolder.getLocale());
	    	model.addAttribute("errorMessage", message);
	        return "demo_1/pages/reset_password";
	    }
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
	 * @return
	 */
	@GetMapping("/email-template")
	public String emailTemplate() {
		
		return "demo_1/partials/emails/template.html";
	}
	
	
	@GetMapping("/error")
	public String handleError(HttpServletRequest request) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	    
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	            return "demo_1/pages/samples/error-404";
	        }else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	            return "demo_1/pages/samples/error-500";
	        }else if(statusCode == HttpStatus.FORBIDDEN.value()) {
	            return "demo_1/pages/samples/error-500";
	        }
	        
	    }
	    return "demo_1/pages/samples/error-500";
	}
	
	@GetMapping("/new_appointment")
	public String new_appointment(Model model, @RequestParam(name = "redirectUri", required = false) String redirectUrl) {
	    
		localeResolver.setLocale(httpServletRequest, httpServletResponse, new Locale("al","sq"));
		
		model.addAttribute("disabledDays", AccountUtil.currentLoggedInBusinessSettings().getNonBusinessDays());
		model.addAttribute("bookingSplit", AccountUtil.currentLoggedInBusinessSettings().getAppointmentTimeSplit());
		model.addAttribute("startTime", AccountUtil.currentLoggedInBusinessSettings().getBusinessStartTimes()[0]);
		model.addAttribute("startMinute", AccountUtil.currentLoggedInBusinessSettings().getBusinessStartTimes()[1]);
		model.addAttribute("endTime", AccountUtil.currentLoggedInBusinessSettings().getBusinessEndTimes()[0]);
		model.addAttribute("endMinute", AccountUtil.currentLoggedInBusinessSettings().getBusinessEndTimes()[1]);
		model.addAttribute("businessName", AccountUtil.currentLoggedInBusinessSettings().getBusinessName());
		model.addAttribute("redirect", redirectUrl);
		
		return "demo_1/pages/new_appointment";
	}
	
}