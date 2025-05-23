package com.example.emailnotification.listener;


import com.example.emailnotification.dto.ContractNotification;
import com.example.emailnotification.service.EmailListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JmsEmailListener {
    private final EmailListenerService emailService;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = "email-notification")
    public void receiveMessage(String message) {
        try {
            ContractNotification notification = objectMapper.readValue(message, ContractNotification.class);
            emailService.sendNotification(
                    notification.getText(),
                    notification.getAddressee());
        } catch (JsonProcessingException e) {
            log.error("Неверный формат сообщения: {}", message);
        }
    }
}
