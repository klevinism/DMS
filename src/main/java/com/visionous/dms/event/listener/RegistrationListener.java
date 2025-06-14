package com.visionous.dms.event.listener;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import com.o2dent.lib.accounts.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.pojo.Verification;
import com.visionous.dms.service.VerificationService;

/**
 * @author delimeta
 *
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>{

    private SpringTemplateEngine thymeleafTemplateEngine;
    private VerificationService verificationService;
    private JavaMailSender mailSender;

	/**
	 *
	 * @param verificationService
	 * @param mailSender
	 * @param thymeleafTemplateEngine
	 */
	@Autowired
	public RegistrationListener(VerificationService verificationService, 
			JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine) {
		
		this.mailSender = mailSender;
		this.verificationService = verificationService;
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
	}

	/**
	 *
	 * @param event
	 */
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		try {
			this.confirmRegistration(event);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sends a confirm email to the just-signed-up user
	 * @param event
	 * @throws MessagingException 
	 */
	private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException {
		Context thymeleafContext = new Context(new Locale("en", "US"));
		Map<String, Object> vars = new HashMap<>();
		String emailTemplatePath = "demo_1/partials/emails/registrationConfirmation.html";
        Account account = event.getAccount();
        String recipientAddress = account.getEmail();
        String token = null;
        
        Optional<Verification> verification = verificationService.findByAccount_idAndExpirationDateAfter(account.getId(), LocalDateTime.now());
        
        if(verification.isPresent()) {
        	token = verification.get().getToken();
        }else {
        	token = UUID.randomUUID().toString();
        	
            verificationService.create(new Verification(account, token));
        }
        
		String domainPath = LandingPages.getDomainPathFromRequest(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()); 
		
		String confirmationUrl = domainPath + "/confirm?token=" + token;
		
		thymeleafContext.setVariable("account", account);
		thymeleafContext.setVariable("confirmationUrl", confirmationUrl);
		
	    thymeleafContext.setVariables(vars);
	    
	    String htmlBody = thymeleafTemplateEngine.process(emailTemplatePath, thymeleafContext);
	     
	    MimeMessage mailMessage = mailSender.createMimeMessage();
    	mailMessage.setSubject("Registration Confirmation", "UTF-8");
    	
    	MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
		helper.setTo(recipientAddress);
        helper.setText(htmlBody, true);
        	
        
        mailSender.send(mailMessage);
    }
	
}
