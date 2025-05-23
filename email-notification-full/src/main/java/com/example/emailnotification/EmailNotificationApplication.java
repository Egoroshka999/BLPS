package com.example.emailnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.example.emailnotification.config.properties")
public class EmailNotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailNotificationApplication.class, args);
    }
}
