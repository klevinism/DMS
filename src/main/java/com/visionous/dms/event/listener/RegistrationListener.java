/**
 * 
 */
package com.visionous.dms.event.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.pojo.Verification;
import com.visionous.dms.service.RoleService;
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
    private RoleService roleService;

    private GlobalSettings globalSettings;
	/**
	 * @param verificationRepository
	 * @param messages
	 */
	@Autowired
	public RegistrationListener(VerificationService verificationService, 
			JavaMailSender mailSender, RoleService roleService,
			SpringTemplateEngine thymeleafTemplateEngine,
			GlobalSettings globalSettings) {
		
		this.mailSender = mailSender;
		this.roleService = roleService;
		this.verificationService = verificationService;
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
		this.globalSettings = globalSettings;
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
		String emailTemplatePath = "demo_1/partials/emails/registrationConfirmation.html";
		String firstEmailTemplatePath = "demo_1/partials/emails/firstRegistrationConfirmation.html";
		String template= null;
        Account account = event.getAccount();
        String recipientAddress = account.getEmail();
        StringBuilder fromAddress= new StringBuilder();
        String token = null;
        String rawPass = account.getPassword();
         
        Optional<Role> roleAdmin = roleService.findByName("ADMIN");
        roleAdmin.ifPresent(role -> {
        	fromAddress.append(role.getAccounts().get(0).getEmail());
        });
        
        Optional<Verification> verification = verificationService.findByAccount_id(account.getId());
        
        if(verification.isPresent()) {
        	token = verification.get().getToken();
        	template = emailTemplatePath;
        }else {
        	token = UUID.randomUUID().toString();
        	template = firstEmailTemplatePath;
        	
            verificationService.create(new Verification(account, token));
            account.setPassword(rawPass);
        }
        
		String domainPath = LandingPages.getDomainPathFromRequest(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()); 
		
		String confirmationUrl = domainPath + "/confirm?token=" + token;
		
		vars.put("account", account);
		vars.put("confirmationUrl", confirmationUrl);
	    thymeleafContext.setVariables(vars);
	    String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);
	     
	    JavaMailSenderImpl jMailSender = (JavaMailSenderImpl)mailSender;
	    jMailSender.setUsername(this.globalSettings.getBusinessEmail());
	    jMailSender.setPassword(this.globalSettings.getBusinessPassword());
	    
	    MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
        	mailMessage.setSubject("Registration Confirmation", "UTF-8");
        	
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
