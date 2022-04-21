package com.visionous.dms.event.listener;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.visionous.dms.event.OnBusinessConfirmationEvent;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Business;

@Component
public class BusinessConfirmationListener implements ApplicationListener<OnBusinessConfirmationEvent>{

    private SpringTemplateEngine thymeleafTemplateEngine;
    private JavaMailSender mailSender;
	private MessageSource messageSource;
	/**
	 * @param verificationRepository
	 * @param messages
	 */
	@Autowired
	public BusinessConfirmationListener(JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine,
			MessageSource messageSource) {
		
		this.mailSender = mailSender;
		this.messageSource = messageSource;
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
	}
	
	/**
	 * @param OnRegistrationCompleteEvent event
	 */
	@Override
	public void onApplicationEvent(OnBusinessConfirmationEvent event) {
		try {
			this.confirmBusinessCreation(event);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sends a confirm email to the just created busines
	 * @param event
	 * @throws MessagingException 
	 */
	private void confirmBusinessCreation(OnBusinessConfirmationEvent event) throws MessagingException {
		Context thymeleafContext = new Context(event.getLocale());
		String emailTemplatePath = "demo_1/partials/emails/businessCreateConfirmation.html";
        Account account = event.getAccount();
        Business business = event.getBusiness();
        String recipientAddress = account.getEmail();
		
        String yourDentalClinic = messageSource.getMessage("YourDentalClinic", null, event.getLocale());
        String created = messageSource.getMessage("alert.createdSuccessfully", null, event.getLocale());
        
		thymeleafContext.setVariable("account", account);
		
		thymeleafContext.setVariable("business", business);
	    
	    String htmlBody = thymeleafTemplateEngine.process(emailTemplatePath, thymeleafContext);
	     
	    MimeMessage mailMessage = mailSender.createMimeMessage();
	    
    	mailMessage.setSubject(yourDentalClinic +" \""+ business.getName() +"\" "+ created.toLowerCase() + "!", "UTF-8");
    	
    	MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
    	helper.setTo(recipientAddress);
        helper.setText(htmlBody, true);
        
        mailSender.send(mailMessage);
    }
	
}