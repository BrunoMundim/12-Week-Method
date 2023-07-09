package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private final String senderEmail = "noreply.12weekmethod@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private AuthenticationService authenticationService;

    public String sendEmailWithTemplate(String subject, String templateName, Object data) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setTo(authenticationService.findUserByBearer().getEmail());

            Context context = new Context();
            context.setVariable("data", data);

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            addInlineImage(helper);

            mailSender.send(message);
            return htmlContent;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addInlineImage(MimeMessageHelper helper) throws MessagingException {
        ClassPathResource imageResource = new ClassPathResource("templates/images/logo.png");
        helper.addInline("logoImage", imageResource);
    }
}
