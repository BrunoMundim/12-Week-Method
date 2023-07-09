package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.entity.Mail;
import com.mundim.WeekMethod.repository.MailRepository;
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

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final AuthenticationService authenticationService;
    private final MailRepository mailRepository;

    @Autowired
    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine, AuthenticationService authenticationService, MailRepository mailRepository) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.authenticationService = authenticationService;
        this.mailRepository = mailRepository;
    }

    public void sendEmailWithTemplate(String subject, String templateName, Object data) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            String to = authenticationService.findUserByBearer().getEmail();
            helper.setTo(to);

            Context context = new Context();
            context.setVariable("data", data);

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            addInlineImage(helper);

            mailSender.send(message);
            saveMailInDatabase(to, subject, htmlContent);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void addInlineImage(MimeMessageHelper helper) throws MessagingException {
        ClassPathResource imageResource = new ClassPathResource("templates/images/logo.png");
        helper.addInline("logoImage", imageResource);
    }

    private void saveMailInDatabase(String to, String subject, String message) {
        Mail mail = new Mail(senderEmail, to, subject, message);
        mailRepository.save(mail);
    }
}
