package com.lab.blps.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("artemis")
public class RabbitProperties {
    private String username;
    private String password;
    private String emailNotificationQueue;
}
