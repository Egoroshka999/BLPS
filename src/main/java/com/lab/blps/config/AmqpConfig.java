package com.lab.blps.config;


import com.lab.blps.config.properties.RabbitProperties;
import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@RequiredArgsConstructor
public class AmqpConfig {
    private final RabbitProperties rabbitProperties;

    @Bean
    public ConnectionFactory amqpConnectionFactory() {
        var brokerUrl = "amqp://localhost:5672";
        return new JmsConnectionFactory(
                rabbitProperties.getUsername(),
                rabbitProperties.getPassword(),
                brokerUrl
        );
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }
}
