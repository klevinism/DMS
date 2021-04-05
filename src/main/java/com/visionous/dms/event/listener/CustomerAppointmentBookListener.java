/**
 * 
 */
package com.visionous.dms.event.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.visionous.dms.event.OnCustomerAppointmentBookEvent;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.service.RoleService;

/**
 * @author delimeta
 *
 */
@Component
public class CustomerAppointmentBookListener implements ApplicationListener<OnCustomerAppointmentBookEvent>{
	 	private SpringTemplateEngine thymeleafTemplateEngine;
	    private JavaMailSender mailSender;
	    private RoleService roleService; 
	    private GlobalSettings globalSettings;
	    private MessageSource messages;
	    
		/**
		 * @param verificationRepository
		 * @param messages
		 */
		@Autowired
		public CustomerAppointmentBookListener(JavaMailSender mailSender, 
				SpringTemplateEngine thymeleafTemplateEngine, RoleService roleService,
				GlobalSettings globalSettings, MessageSource messages) {
			
			this.mailSender = mailSender;
			this.thymeleafTemplateEngine = thymeleafTemplateEngine;
			this.globalSettings = globalSettings;
			this.roleService = roleService;
			this.messages = messages;
		}
		
		/**
		 * @param OnRegistrationCompleteEvent event
		 */
		@Override
		public void onApplicationEvent(OnCustomerAppointmentBookEvent event) {
			this.confirmRegistration(event);
		}
		
		
		/**
		 * Sends a confirm email to the just-signed-up user
		 * @param event
		 */
		private void confirmRegistration(OnCustomerAppointmentBookEvent event) {
			Context thymeleafContext = new Context();
			Map<String, Object> vars = new HashMap<>();
			String emailTemplatePath = "demo_1/partials/emails/appointmentVerification.html";
	    	String dentalAppointment = messages.getMessage("DentalAppointment", null, LocaleContextHolder.getLocale());

	        String recipientAddress = event.getAppointment().getCustomer().getAccount().getEmail();
	        String fromAddress = this.globalSettings.getBusinessEmail();
	        
			vars.put("appointment", event.getAppointment());
			vars.put("businessName", globalSettings.getBusinessName());
			
		    thymeleafContext.setVariables(vars);
		    String htmlBody = thymeleafTemplateEngine.process(emailTemplatePath, thymeleafContext);
		     
		    JavaMailSenderImpl jMailSender = (JavaMailSenderImpl)mailSender;
		    jMailSender.setUsername(this.globalSettings.getBusinessEmail());
		    jMailSender.setPassword(this.globalSettings.getBusinessPassword());
		    
		    MimeMessage mailMessage = mailSender.createMimeMessage();
	        try {
	        	mailMessage.setSubject(dentalAppointment + " " + globalSettings.getBusinessName(), "UTF-8");
	        	
	        	MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
	        	helper.setFrom(new InternetAddress(fromAddress.toString()));
	        	helper.setTo(recipientAddress);
	            helper.setText(htmlBody, true);
	        	
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        
	        mailSender.send(mailMessage);
	    }
}
