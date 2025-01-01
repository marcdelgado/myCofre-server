package myCofre.server.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import myCofre.server.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MessageSource messageSource;

    @Value("${server.clienturl}")
    private String clientUrl;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, MessageSource messageSource){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
    }


    public void sendActivationEmail(User user, String token, String language) throws MessagingException {
        String activationLink = this.clientUrl + "/activate?" +
                "email=" + user.getEmail() + "&" +
                "token=" + token;
        Locale locale = new Locale(language);
        String subject = messageSource.getMessage("email.activation.subject", null, locale);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", user.getName());
        parameters.put("link", activationLink);

        String templatePath = "emails/activation." +language.toLowerCase()+".html";
        Context context = new Context();
        context.setVariables(parameters);
        String htmlContent = templateEngine.process(templatePath, context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("MyCofre <no-reply@mycofre.com>");

        mailSender.send(message);
    }


    public void sendDeleteEmail(User user, String token, String language) throws MessagingException {
        String activationLink = this.clientUrl + "/delete?" +
                "email=" + user.getEmail() + "&" +
                "token=" + token;
        Locale locale = new Locale(language);
        String subject = messageSource.getMessage("email.delete.subject", null, locale);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", user.getName());
        parameters.put("link", activationLink);

        String templatePath = "emails/delete." +language.toLowerCase()+".html";
        Context context = new Context();
        context.setVariables(parameters);
        String htmlContent = templateEngine.process(templatePath, context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("MyCofre <no-reply@mycofre.com>");

        mailSender.send(message);
    }

}
