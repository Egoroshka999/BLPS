package com.example.emailnotification.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("rabbit")
public class RabbitProperties {
    private String username;
    private String password;
    private String emailNotificationQueue;
}
