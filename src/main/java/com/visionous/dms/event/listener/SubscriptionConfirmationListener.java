package com.visionous.dms.event.listener;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.visionous.dms.event.OnSubscriptionConfirmationEvent;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Business;

@Component
public class SubscriptionConfirmationListener implements ApplicationListener<OnSubscriptionConfirmationEvent>{

    private SpringTemplateEngine thymeleafTemplateEngine;
    private JavaMailSender mailSender;
    
	/**
	 * @param verificationRepository
	 * @param messages
	 */
	@Autowired
	public SubscriptionConfirmationListener(JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine) {
		
		this.mailSender = mailSender;
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
	}
	
	/**
	 * @param OnRegistrationCompleteEvent event
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
						
		thymeleafContext.setVariable("account", account);

		thymeleafContext.setVariable("business", business);
	    
	    String htmlBody = thymeleafTemplateEngine.process(emailTemplatePath, thymeleafContext);
	     
	    MimeMessage mailMessage = mailSender.createMimeMessage();
	    
    	mailMessage.setSubject("Subscription Receipt Confirmation!", "UTF-8");
    	
    	MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
    	helper.setTo(recipientAddress);
        helper.setText(htmlBody, true);
        
        mailSender.send(mailMessage);
    }
	
}