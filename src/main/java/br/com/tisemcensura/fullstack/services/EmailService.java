package br.com.tisemcensura.fullstack.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	private final static org.jboss.logging.Logger LOGGER = LoggerFactory
            .logger(EmailService.class);
 	
 	@Autowired
    private JavaMailSender mailSender;


    @Async
    public void send( String to, String subject, String text) {
    	
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(text, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("lucasrodrigodes@gmail.com");
            mailSender.send(mimeMessage);
            
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }
}