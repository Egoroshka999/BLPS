package com.lab.blps.notification.service;


import com.lab.blps.notification.dto.EmailNotificationPayload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailNotificationClient {
    private final RestTemplate rest;

    public EmailNotificationClient(RestTemplate rest) {
        this.rest = rest;
    }

    public void send(EmailNotificationPayload req) {
        rest.postForEntity(
                "http://localhost:9999/email",
                req,
                Void.class
        );
    }
}
