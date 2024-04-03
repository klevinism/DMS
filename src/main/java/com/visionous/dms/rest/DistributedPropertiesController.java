package com.visionous.dms.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
public class DistributedPropertiesController {

    @Autowired
    private JavaMailSender mailSender;


    @GetMapping("/email")
    public String sendEmail() throws MessagingException {
        String htmlBody = "HTML BODY";


        MimeMessage mailMessage = mailSender.createMimeMessage();

        mailMessage.setSubject("SUBJECTTT", "UTF-8");

        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
        helper.setTo("klevindelimeta@hotmail.com");
        helper.setText(htmlBody, true);

        mailSender.send(mailMessage);

        return htmlBody;
    }
}