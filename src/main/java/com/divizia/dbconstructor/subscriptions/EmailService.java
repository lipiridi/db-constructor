package com.divizia.dbconstructor.subscriptions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private JavaMailSender emailSender;

    public void sendMessageWithAttachment(String to, String subject, String text, Resource file) {
        if (to == null || to.isEmpty()) return;

        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            emailSender.send(message);

        } catch (MessagingException e) {
            log.error(e.getMessage(), this);
        }

    }

}
