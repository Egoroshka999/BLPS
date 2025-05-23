package com.example.emailnotification.service;

import com.example.emailnotification.config.properties.EmailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailListenerService {
    private final JavaMailSender javaMailSender;
    private final EmailProperties emailProperties;

    public void sendNotification(String text, String addressee) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailProperties.getFrom());
        mailMessage.setTo(addressee);
        mailMessage.setText(String.format("Вам пришло новое сообщение: %s", text));
        mailMessage.setSubject(emailProperties.getSubject());

        javaMailSender.send(mailMessage);
    }
}
