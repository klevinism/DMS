/**
 * 
 */
package com.visionous.dms.event.listener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.event.OnCustomerAppointmentBookEvent;

/**
 * @author delimeta
 *
 */
@Component
public class CustomerAppointmentBookListener implements ApplicationListener<OnCustomerAppointmentBookEvent>{
	private SpringTemplateEngine thymeleafTemplateEngine;
	private JavaMailSender mailSender;
	private MessageSource messages;

	private AccountService accountService;

	/**
	 *
	 * @param mailSender
	 * @param thymeleafTemplateEngine
	 * @param messages
	 */
	@Autowired
	public CustomerAppointmentBookListener(JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine,
										   MessageSource messages, AccountService accountService) {

		this.mailSender = mailSender;
		this.thymeleafTemplateEngine = thymeleafTemplateEngine;
		this.messages = messages;
		this.accountService = accountService;
	}

	/**
	 *
	 * @param event
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
		Context thymeleafContext = new Context(new Locale("en", "US"));
		Map<String, Object> vars = new HashMap<>();
		String emailTemplatePath = "demo_1/partials/emails/appointmentVerification.html";
		String dentalAppointment = messages.getMessage("DentalAppointment", null, event.getLocale());
		Optional<Account> customerAccount = accountService.findById(event.getAppointment().getCustomer().getId());
		customerAccount.ifPresentOrElse(c -> {
			String recipientAddress = c.getEmail();
			String fromAddress = AccountUtil.currentLoggedInBusinessSettings().getBusinessEmail();

			vars.put("appointment", event.getAppointment());
			vars.put("businessName", AccountUtil.currentLoggedInBusinessSettings().getBusinessName());

			thymeleafContext.setVariables(vars);
			String htmlBody = thymeleafTemplateEngine.process(emailTemplatePath, thymeleafContext);

			JavaMailSenderImpl jMailSender = (JavaMailSenderImpl)mailSender;
			jMailSender.setUsername(AccountUtil.currentLoggedInBusinessSettings().getBusinessEmail());
			jMailSender.setPassword(AccountUtil.currentLoggedInBusinessSettings().getBusinessPassword());

			MimeMessage mailMessage = mailSender.createMimeMessage();
			try {
				mailMessage.setSubject(dentalAppointment + " " + AccountUtil.currentLoggedInBusinessSettings().getBusinessName(), "UTF-8");

				MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
				helper.setFrom(new InternetAddress(fromAddress.toString()));
				helper.setTo(recipientAddress);
				helper.setText(htmlBody, true);

			}catch(Exception e) {
				e.printStackTrace();
			}

			mailSender.send(mailMessage);
		}, () ->{
			try {
				throw new Exception("Customer not found");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
}
