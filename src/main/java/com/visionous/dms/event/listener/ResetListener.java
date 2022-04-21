/**
 * 
 */
package com.visionous.dms.event.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnResetPasswordEvent;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Reset;
import com.visionous.dms.service.ResetService;

/**
 * @author delimeta
 *
 */
@Component
public class ResetListener implements ApplicationListener<OnResetPasswordEvent>{
	private SpringTemplateEngine thymeleafTemplateEngine;
	private ResetService resetService;
    private JavaMailSender mailSender;
    /**
	 * 
	 */
    @Autowired
	public ResetListener(SpringTemplateEngine thymeleafTemplateEngine, JavaMailSender mailSender, ResetService resetService) {
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
		this.resetService = resetService;
		this.mailSender = mailSender;
	}
	
	@Override
	public void onApplicationEvent(OnResetPasswordEvent event) {
		this.resetPassword(event);
	}

	/**
	 * @param event
	 */
	private void resetPassword(OnResetPasswordEvent event) {
		Context thymeleafContext = new Context(event.getLocale());
		Map<String, Object> vars = new HashMap<>();
		
		Account account = event.getAccount();
		String recipientAddress = account.getEmail();
        String token = UUID.randomUUID().toString();
                
        resetService.create(new Reset(account, token));
        
        String domainPath = LandingPages.getDomainPathFromRequest(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());

		String resetUrl = domainPath + "/confirmReset?token=" + token;

		vars.put("account", account);
		vars.put("resetUrl", resetUrl);
	    thymeleafContext.setVariables(vars);
	    String htmlBody = thymeleafTemplateEngine.process("demo_1/partials/emails/resetPassword.html", thymeleafContext);
	    
        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {

        	mailMessage.setSubject("Reset Password", "UTF-8");
        
        	MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            helper.setTo(recipientAddress);
            helper.setText(htmlBody, true);
        	
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        mailSender.send(mailMessage);
	}

}
