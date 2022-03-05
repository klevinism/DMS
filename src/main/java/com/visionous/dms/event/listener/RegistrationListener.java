/**
 * 
 */
package com.visionous.dms.event.listener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.pojo.Account;
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
	 * @param verificationRepository
	 * @param messages
	 */
	@Autowired
	public RegistrationListener(VerificationService verificationService, 
			JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine) {
		
		this.mailSender = mailSender;
		this.verificationService = verificationService;
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
	}
	
	/**
	 * @param OnRegistrationCompleteEvent event
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
        String rawPass = account.getPassword();
         
        
        Optional<Verification> verification = verificationService.findByAccount_idAndExpirationDateAfter(account.getId(), LocalDateTime.now());
        
        if(verification.isPresent()) {
        	token = verification.get().getToken();
        }else {
        	token = UUID.randomUUID().toString();
        	
            verificationService.create(new Verification(account, token));
            account.setPassword(rawPass);
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
