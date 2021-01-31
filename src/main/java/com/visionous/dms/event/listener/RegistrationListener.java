/**
 * 
 */
package com.visionous.dms.event.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
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
import com.visionous.dms.pojo.Role;
import com.visionous.dms.pojo.Verification;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.RoleRepository;
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
    
    private AccountRepository accountRepository;
    
    private RoleRepository roleRepository;

	/**
	 * @param verificationRepository
	 * @param messages
	 */
	@Autowired
	public RegistrationListener(VerificationRepository verificationRepository, MessageSource messages, 
			JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine, 
			AccountRepository accountRepository, RoleRepository roleRepository) {
		this.verificationRepository = verificationRepository;
		this.mailSender = mailSender;
		this.messages = messages;
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
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
        
        Optional<Role> roleAdmin = roleRepository.findByName("ADMIN");
        roleAdmin.ifPresent(role -> {
        	fromAddress.append(role.getAccounts().get(0).getEmail());
        });
        
        Optional<Verification> verification = verificationRepository.findByAccount_id(account.getId());
        
        if(verification.isPresent()) {
        	token = verification.get().getToken();
        	template = emailTemplatePath;
        }else {
        	token = UUID.randomUUID().toString();
        	template = firstEmailTemplatePath;
        	
            Verification verificationToken = new Verification(account, token);
            verificationRepository.saveAndFlush(verificationToken);
            account.setPassword(rawPass);
        }
        
		String domainPath = LandingPages.getDomainPathFromRequest(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()); 
		
		String confirmationUrl = domainPath + "/confirm?token=" + token;
		
		vars.put("account", account);
		vars.put("confirmationUrl", confirmationUrl);

	    thymeleafContext.setVariables(vars);
	    
	    String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);
	    
        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
            System.out.println(account.getPassword() + " <<LATER");
        	mailMessage.setSubject("Registration Confirmation", "UTF-8");
        	
        	MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
        	helper.setFrom(fromAddress.toString());
        	helper.setTo(recipientAddress);
            helper.setText(htmlBody, true);
        	
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        mailSender.send(mailMessage);
    }
	
}
