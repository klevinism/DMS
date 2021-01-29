/**
 * 
 */
package com.visionous.dms.event.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Verification;
import com.visionous.dms.repository.VerificationRepository;

/**
 * @author delimeta
 *
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>{

    private MessageSource messages;
    
	private VerificationRepository verificationRepository;
	
    private JavaMailSender mailSender;

    private SpringTemplateEngine thymeleafTemplateEngine;

	/**
	 * @param verificationRepository
	 * @param messages
	 */
	@Autowired
	public RegistrationListener(VerificationRepository verificationRepository, MessageSource messages, 
			JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine) {
		this.verificationRepository = verificationRepository;
		this.mailSender = mailSender;
		this.messages = messages;
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
	}
	
	/**
	 * @param OnRegistrationCompleteEvent event
	 */
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}
	
	
	/**
	 * Sends a confirm email to the just-signed-up user
	 * @param event
	 */
	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		Context thymeleafContext = new Context();
		Map<String, Object> vars = new HashMap<>();
		
        Account account = event.getAccount();
        String recipientAddress = account.getEmail();
        String token = UUID.randomUUID().toString();
        
        Verification verificationToken = new Verification(account, token);
        verificationRepository.saveAndFlush(verificationToken);
        
		String domainPath = LandingPages.getDomainPathFromRequest(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()); 
		
		String confirmationUrl = domainPath + "/confirm?token=" + token;
		
		vars.put("account", account);
		vars.put("confirmationUrl", confirmationUrl);

	    thymeleafContext.setVariables(vars);
	    
	    String htmlBody = thymeleafTemplateEngine.process("demo_1/partials/emails/registrationConfirmation.html", thymeleafContext);
	    
        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {

        	mailMessage.setSubject("Registration Confirmation", "UTF-8");
        
        	MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            helper.setTo(recipientAddress);
            helper.setText(htmlBody, true);
        	
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        
        mailSender.send(mailMessage);
    }
	
}
