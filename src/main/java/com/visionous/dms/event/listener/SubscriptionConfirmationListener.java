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
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.visionous.dms.event.OnSubscriptionConfirmationEvent;

@Component
public class SubscriptionConfirmationListener implements ApplicationListener<OnSubscriptionConfirmationEvent>{

    private SpringTemplateEngine thymeleafTemplateEngine;
    private JavaMailSender mailSender;
	private MessageSource messageSource;

	/**
	 *
	 * @param mailSender
	 * @param thymeleafTemplateEngine
	 * @param messageSource
	 */
	@Autowired
	public SubscriptionConfirmationListener(JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine,
			MessageSource messageSource) {
		
		this.mailSender = mailSender;
		this.messageSource = messageSource;
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
	}

	/**
	 *
	 * @param event
	 */
	@Override
	public void onApplicationEvent(OnSubscriptionConfirmationEvent event) {
		try {
			this.confirmSubscriptionCreation(event);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sends a subscription confirm email to the just created busines
	 * @param event
	 * @throws MessagingException 
	 */
	private void confirmSubscriptionCreation(OnSubscriptionConfirmationEvent event) throws MessagingException {
		Context thymeleafContext = new Context(event.getLocale());
		String emailTemplatePath = "demo_1/partials/emails/subscriptionConfirmation.html";
        Account account = event.getAccount();
        Business business = event.getBusiness();
        String recipientAddress = account.getEmail();

        String emailSubscriptionConfirmation = messageSource.getMessage("email.subject.subscriptionConfirmation", null, event.getLocale());

		thymeleafContext.setVariable("account", account);

		thymeleafContext.setVariable("business", business);
	    
	    String htmlBody = thymeleafTemplateEngine.process(emailTemplatePath, thymeleafContext);
	     
	    MimeMessage mailMessage = mailSender.createMimeMessage();
	    
    	mailMessage.setSubject(emailSubscriptionConfirmation + "!", "UTF-8");
    	
    	MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
    	helper.setTo(recipientAddress);
        helper.setText(htmlBody, true);
        
        mailSender.send(mailMessage);
    }
	
}