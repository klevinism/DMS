package com.visionous.dms.event.listener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.entity.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.visionous.dms.event.OnBusinessConfirmationEvent;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class BusinessConfirmationListener implements ApplicationListener<OnBusinessConfirmationEvent>{

    private SpringTemplateEngine springTemplateEngine;
    private JavaMailSender mailSender;
	private MessageSource messageSource;

	/**
	 *
	 * @param mailSender
	 * @param springTemplateEngine
	 * @param messageSource
	 */
	@Autowired
	public BusinessConfirmationListener(JavaMailSender mailSender, SpringTemplateEngine springTemplateEngine,
			MessageSource messageSource) {
		
		this.mailSender = mailSender;
		this.messageSource = messageSource;
		this.springTemplateEngine = springTemplateEngine;
	}

	/**
	 *
	 * @param event the event to respond to
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
	    
	    String htmlBody = springTemplateEngine.process(emailTemplatePath, thymeleafContext);
	     
	    MimeMessage mailMessage = mailSender.createMimeMessage();
	    
    	mailMessage.setSubject(yourDentalClinic +" \""+ business.getName() +"\" "+ created.toLowerCase() + "!", "UTF-8");
    	
    	MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
    	helper.setTo(recipientAddress);
        helper.setText(htmlBody, true);
        
        mailSender.send(mailMessage);
    }
	
}